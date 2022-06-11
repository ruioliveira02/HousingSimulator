package com.housingsimulator.model;

import com.housingsimulator.exceptions.AlreadyExistingEntityException;
import com.housingsimulator.exceptions.InvalidEntityDeletionException;
import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.SimState;
import com.housingsimulator.simulation.Simulator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Wrapper class for all objects of a simulation
 */
public class Simulation implements SimState, AutoSerializable, Serializable {

    //TODO:: Separate entities in House / Suppliers
    private Map<Integer, AbstractEntity> entities;/*! All the entities in the simulation*/
    private Map<String, SpeakerBrand> speakerBrands; /*! All the different speaker brands */


    /**
     * Creates an empty simulation
     */
    public Simulation() {
        this.entities = new HashMap<>();
        this.speakerBrands = new HashMap<>();
    }

    /**
     * Creates a copy of a simulation
     * @param sim The simulation to copy
     */
    public Simulation(Simulation sim) { //TODO: clonar o speakerBrands, talvez? idk, o vasques provavelmente esqueceu-se
        entities = new HashMap<>();
        for(AbstractEntity ent : sim.entities.values())
            entities.put(ent.getId(), ent.clone());
    }

    public Simulation(Simulator.State sim) {
        this();

        for (int id : sim.getValidIds()) {
            AbstractEntity e = (AbstractEntity)sim.getEntity(id);

            if (e instanceof SmartHouse house) {
                this.entities.put(house.getId(), house);
                house.getDevices().values().forEach(device -> this.entities.put(device.getId(), device));
            } else if (e instanceof EnergySupplier supplier) {
                this.entities.put(supplier.getId(), supplier);
            } else if (e instanceof SpeakerBrand brand) {
                this.speakerBrands.put(brand.getName(), brand);
            } else {
                //nao era suposto acontecer
                throw new IllegalArgumentException("Cenas");
            }
        }
    }

    @Override
    public Set<Integer> getValidIds() {
        return this.entities.entrySet().stream()
                .filter(e -> e.getValue() instanceof SmartHouse || e.getValue() instanceof EnergySupplier || e.getValue() instanceof SpeakerBrand)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }


    /**
     * Gets the speaker brand (the "cannonical" brand) with the given name
     * @param name the name of the brand
     */
    public SpeakerBrand getSpeakerBrand(String name) {
        SpeakerBrand brand = this.speakerBrands.get(name);

        if(brand != null)
            return brand.clone();

        return null;
    }

    /**
     * Gets a set with all speaker brands, including those inside the houses, meaning there can be multiple
     * instances of the same "brand", i.e., the same name.
     *
     * This is because a brand can change over time. However, that change should not apply to already existing
     * speakers. As a result, the same brand name can have many different objects associated with it
     *
     * @return the set of all brands
     */
    public Set<SpeakerBrand> getAllSpeakerBrands() {
        Set<SmartHouse> houses = this.getHouses();

        Set<SpeakerBrand> result = this.speakerBrands.values().stream().collect(Collectors.toSet());

        for(SmartHouse h : houses) {
            for(SmartDevice d : h.getDevices().values()) {
                if(d instanceof SmartSpeaker) {
                    result.add(((SmartSpeaker)d).getBrand().clone());
                }
            }
        }

        return result;
    }

    /**
     * Adds a speaker brand to the simulation
     * @param brand the brand to add
     * @throws AlreadyExistingEntityException if there already exists a brand with the same name
     */
    public void addSpeakerBrand(SpeakerBrand brand) throws AlreadyExistingEntityException {
        if(this.speakerBrands.containsKey(brand.getName()))
            throw new AlreadyExistingEntityException("There already exists a brand with the same name: " + brand.getName());
        this.speakerBrands.put(brand.getName(), brand.clone());
    }

    /**
     * Updates a speaker brand (does not affect previously defined speakers)
     * @param oldName the old name of the brand
     * @param brand the brand to update
     * @throws NoSuchEntityException if there is no brand with the same name
     */
    public void updateSpeakerBrand(String oldName, SpeakerBrand brand) throws NoSuchEntityException {
        if(!this.speakerBrands.containsKey(oldName))
            throw new NoSuchEntityException("No brand named: " + oldName);

        this.speakerBrands.remove(oldName);
        this.speakerBrands.put(brand.getName(), brand.clone());
    }

    /**
     * Deletes the brand with the given name
     * @param name the name of the brand to delete
     * @throws NoSuchEntityException If there is no brand with the given name
     */
    public void deleteSpeakerBrand(String name) throws NoSuchEntityException  {
        if(!this.speakerBrands.containsKey(name))
            throw new NoSuchEntityException("No brand named: " + name);

        this.speakerBrands.remove(name);
    }

    /**
     * Adds a device to the given room of the house
     * @param houseId the id of the house
     * @param room the room of the house
     * @param device the device to add
     * @throws NoSuchEntityException if there is no entity matching the given house id
     * @throws WrongEntityTypeException if there is an entity matching the given house id, but it isn't a house
     */
    public void addDevice(int houseId, String room, SmartDevice device)
            throws NoSuchEntityException, WrongEntityTypeException, AlreadyExistingEntityException {

        AbstractEntity ent = this.entities.get(houseId);

        if(ent == null)
            throw new NoSuchEntityException("No entity with id " + houseId);

        if(!(ent instanceof SmartHouse))
            throw new WrongEntityTypeException("Entity with given id exists but is not a SmartHouse");

        SmartHouse house = (SmartHouse)ent;
        house.addDevice(device, room);
    }

    /**
     * Updates the device with the given id
     * @param id the id of the device
     * @param device the new device
     * @throws NoSuchEntityException when there is no entity with the given id
     * @throws WrongEntityTypeException when the id points to an entity which is not a house
     */
    public void updateDevice(int id, SmartDevice device) throws NoSuchEntityException, WrongEntityTypeException {
        Entity ent = this.getEntity(id);

        if(ent == null)
            throw new NoSuchEntityException("Entity with id " + id + " doesn't exist");

        for(Entity h1 : this.entities.values()) {
            if(h1 instanceof SmartHouse) {
                SmartHouse house = (SmartHouse)h1;
                if(house.containsDevice(id)) {
                    house.updateDevice(id, device);
                }
            }
        }
    }

    /**
     * Adds a house to the simulation
     * @param house the house to add
     */
    public void addHouse(SmartHouse house) {
        SmartHouse copy = house.clone();
        entities.put(house.getId(), copy);
        entities.putAll(house.getDevices());
    }

    /**
     * Adds Adds a supplier to the simulation
     * @param supplier the supplier to add
     */
    public void addSupplier(EnergySupplier supplier) {
        EnergySupplier copy = supplier.clone();
        entities.put(copy.getId(), copy);
    }

    /**
     * Resets the counter of each house
     */
    public void resetCounters() {
        entities.values().parallelStream()
                .filter(ent -> ent instanceof SmartHouse)
                .forEach(ent -> ((SmartHouse)ent).resetCounters());
    }

    /**
     * Gets all the devices indexed by the room they are in
     * @param houseId the id of the house to get the devices from
     * @return a map of the set of devices indexed by the name of the room they are in
     */
    public Map<String, Set<SmartDevice>> getDevicesByRoom(int houseId) throws NoSuchEntityException, WrongEntityTypeException {

        Entity ent = this.getEntity(houseId);

        if(ent == null)
            throw new NoSuchEntityException("No entity with id " + houseId);
        if(!(ent instanceof SmartHouse))
            throw new WrongEntityTypeException("Entity with the given id is not a house");

        SmartHouse house = (SmartHouse)ent;


        return house.getDevicesByRoom();
    }

    /**
     * Gets all the devices indexed by the house/room they are in
     * @return a map of the devices indexed by their house id (first element of list (as string)) and room (second
     * element)
     */
    public Map<List<String>, Set<SmartDevice>> getDevicesByRoom() {
        Map<List<String>, Set<SmartDevice>> result = new HashMap<>();

        for(SmartHouse house : this.getHouses()) {
            String id = String.format("%d", house.getId());

            Map<String, Set<SmartDevice>> devicesByRoom = house.getDevicesByRoom();
            for(Map.Entry<String, Set<SmartDevice>> room : devicesByRoom.entrySet()) {
                List<String> index = List.of(new String[]{id, room.getKey()});
                result.put(index, room.getValue());
            }
        }

        return result;
    }

    /**
     * Updates the house.
     *
     * The house updated corresponds to the one with the same id as the provided one.
     *
     * @param house the new value of the house
     */
    public void updateHouse(int id,SmartHouse house) throws NoSuchEntityException, WrongEntityTypeException {
        Entity entity = entities.get(id);

        if(entity == null)
            throw new NoSuchEntityException("House with given id does not exist");

        if(entity.getClass() != SmartHouse.class)
            throw new WrongEntityTypeException("The provided id does not belong to a house");

        SmartHouse currentHouse = (SmartHouse)entity;
        currentHouse.setName(house.getName());
        currentHouse.setOwnerName(house.getOwnerName());
        currentHouse.setOwnerNif(house.getOwnerNif());
        currentHouse.setSupplierId(house.getSupplierId());
    }

    /**
     * changes the house energy supplier
     * @param id the id of the house
     * @param es the new energy supplier
     */
    public void changeHouseSupplier(int id,EnergySupplier es) throws NoSuchEntityException, WrongEntityTypeException {
        Entity entity = entities.get(id);

        if(entity == null)
            throw new NoSuchEntityException("House with given id does not exist");

        if(entity.getClass() != SmartHouse.class)
            throw new WrongEntityTypeException("The provided id does not belong to a house");

        SmartHouse currentHouse = (SmartHouse)entity;
        currentHouse.setSupplierId(es.getId());
    }

    /**
     * Updates the energy supplier.
     *
     * The supplier updated corresponds to the one with the same id as the provided one.
     *
     * @param id the id of the supplier to change
     * @param supplier the new value of the energy supplier
     */
    public void updateSupplier(int id, EnergySupplier supplier) throws NoSuchEntityException, WrongEntityTypeException {
        Entity entity = entities.get(id);

        if(entity == null)
            throw new NoSuchEntityException("Supplier with given id does not exist");

        if(entity.getClass() != EnergySupplier.class)
            throw new WrongEntityTypeException("The provided id does not belong to a supplier");

        EnergySupplier currentSupplier = (EnergySupplier) entity;

        //The reference does not change after update
        currentSupplier.copy(supplier);
    }

    /**
     * Deletes a house and all receipts associated with it
     *
     * @param id the id of the house to delete
     */
    public void deleteDevice(int id) throws NoSuchEntityException, WrongEntityTypeException  {

        for(Entity ent : this.entities.values().parallelStream().filter(e -> e instanceof SmartHouse).toList()) {
            SmartHouse house = (SmartHouse)ent;

            if(house.containsDevice(id))
                house.removeDevice(house.getDevices().get(id));
        }
    }

    /**
     * Deletes a house and all receipts associated with it
     *
     * @param id the id of the house to delete
     */
    public void deleteHouse(int id) throws NoSuchEntityException, WrongEntityTypeException  {
        Entity entity = entities.get(id);

        if(entity == null)
            throw new NoSuchEntityException("House with given id does not exist");

        if(entity.getClass() != SmartHouse.class)
            throw new WrongEntityTypeException("The provided id does not belong to a house");

        entities.remove(id);
    }

    /**
     * Gets the entity(ies) with the given name
     * @param name the name of the entity(ies)
     * @return the entity(ies)
     */
    public Set<AbstractEntity> getEntitiesByName(String name) {
        return this.entities.values().parallelStream().filter(e -> e.getName().equals(name)).map(AbstractEntity::clone)
                .collect(Collectors.toSet());
    }

    /**
     * Deletes the entity with the given id and all the entities depending on it
     * @param id the id of the entity
     */
    public void deleteEntity(int id) throws NoSuchEntityException, InvalidEntityDeletionException {
        Entity ent = this.getEntity(id);

        if(ent == null)
            throw new NoSuchEntityException("No entity with id " + id);

        if(ent instanceof Receipt || ent instanceof EnergySupplier)
            throw new InvalidEntityDeletionException("Cannot delete an instance of " + ent.getClass().getSimpleName());

        try {
            if(ent instanceof SmartHouse)
                this.deleteHouse(id);
            if(ent instanceof SmartDevice)
                this.deleteDevice(id);
        } catch(WrongEntityTypeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the entity with the given id
     * @param id the given id
     * @return the entity with the id
     */
    public AbstractEntity getEntity(int id) {
        AbstractEntity ent = this.entities.get(id);

        if(ent != null)
            ent = ent.clone();

        return ent;
    }

    /**
     * Gets an entity by id casted to the given type
     * @param id the given id
     * @return the entity
     * @param <T> the type of the entity
     * @throws WrongEntityTypeException there is an entity with the given id but is of the wrong type
     * @throws NoSuchEntityException there is no entity with the given id
     */
    public <T extends Entity> T getEntityWithType(Class<T> tClass, int id)
            throws WrongEntityTypeException, NoSuchEntityException {
        Entity ent = this.getEntity(id);

        if(ent == null)
            throw new NoSuchEntityException("No entity with id " + id);

        if(!(ent.getClass().equals(tClass)))
            throw new WrongEntityTypeException("Entity with id " + id + "exists but is of type " + ent.getClass() +
                    " instead of " + tClass);

        return (T)ent.clone();
    }


    /**
     * Gets the entity of the given type
     *
     * @return the entities of the given type
     */
    public <T extends Entity> Set<T> getEntityByType(Class<T> type) {
        return this.entities.values().parallelStream()
                .filter(e -> type.isAssignableFrom(e.getClass()))
                .map(e -> (T)e.clone()).collect(Collectors.toSet());
    }

    /**
     * gets the device with a given id
     * @param id the device to search for
     * @param tClass the class of the device to get
     * @return the device if found
     * @throws NoSuchEntityException if no entity found
     * @throws WrongEntityTypeException if the entity is of another type
     */
    public < T extends SmartDevice> T getDevice(Class<T> tClass,int id) throws NoSuchEntityException,WrongEntityTypeException {
        AbstractEntity e = this.entities.get(id);
        if (e==null) throw new NoSuchEntityException("No device with id " + id);
        if (tClass.isInstance(e)){
            return (T)e;
        }
        else{
            throw new WrongEntityTypeException("Entity with id " + id + "exists but is not of class: " + tClass.getName());
        }
    }

    /**
     * gets the devices of a given house
     * @param id the device to search for
     * @param tClass the class of the devices to get
     * @return the device if found
     * @throws NoSuchEntityException if no entity found
     * @throws WrongEntityTypeException if the entity is of another type
     */
    public < T extends SmartDevice> Set<T> getDevicesInHouse(Class<T> tClass,int id) throws NoSuchEntityException,WrongEntityTypeException {
        AbstractEntity e = this.entities.get(id);
        if (e==null) throw new NoSuchEntityException("No house with id " + id);
        if (e instanceof SmartHouse){
            SmartHouse house =(SmartHouse) e;
            return house.getDevices().values().stream().filter(tClass::isInstance).map(d -> (T)d).collect(Collectors.toSet());
        }
        else{
            throw new WrongEntityTypeException("Entity with id " + id + "exists but is not a house");
        }
    }






    /**
     * Gets all the houses in the simulation
     * @return the houses in the simulation
     */
    public Set<SmartHouse> getHouses() {
        return entities.values().parallelStream().filter(ent -> ent instanceof SmartHouse)
                .map(ent -> new SmartHouse((SmartHouse)ent)).collect(Collectors.toSet());
    }

    /**
     * Gets all the suppliers in the simulation
     * @return all the suppliers in the simulation
     */
    public Set<EnergySupplier> getSuppliers() {
        return entities.values().parallelStream().filter(ent -> ent instanceof EnergySupplier)
                .map(ent -> new EnergySupplier((EnergySupplier) ent)).collect(Collectors.toSet());
    }

    @Override
    public Simulation clone() {
        return new Simulation(this);
    }

    @Override
    public String toString() {
        return String.format("entities: {%s}", this.entities.toString());
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;

        if(o == null || o.getClass() != this.getClass())
            return false;

        Simulation sim = (Simulation)o;

        boolean result = this.getHouses().equals(sim.getHouses()) &&
                this.getSuppliers().equals(sim.getSuppliers());


        return result;
    }
}
