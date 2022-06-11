package com.housingsimulator.model;

import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.serialization.AutoSerializable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Data to store a house and the devices
 */
public class SmartHouse extends AbstractEntity implements AutoSerializable, Serializable {

    private String ownerName;/*! the name of the house's owner */
    private int ownerNif;/*! the nif of the house's name */
    private int supplierId;/*! the energy supplier to the house's id */
    private Map<Integer,SmartDevice> devices; /*! the devices inside the house */
    private Map<String, Set<Integer>> rooms; /*! the devices inside a given room */
    private Map<String, Set<SmartDevice>> installations; /*! a set of copies of the installed devices since last counter reset,
                                                        indexed by the room they were installed in */

    /**
     * gets the owners name of the house
     * @return the owners name
     */
    public String getOwnerName() {
        return this.ownerName;
    }

    /**
     * sets the owners name of the house
     * @param ownerName the house owners name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * gets the owner of the house's nif
     * @return the owners nif
     */
    public int getOwnerNif() {
        return this.ownerNif;
    }

    /**
     * sets the owner of the house's nif
     * @param ownerNif the house owners nif
     */
    public void setOwnerNif(int ownerNif) {
        this.ownerNif = ownerNif;
    }

    /**
     * gets the energy supplier for the house
     * @return the house energy supplier
     */
    public int getSupplierId() {
        return this.supplierId;
    }

    /**
     * sets the energy supplier for the house
     * @param supplierId the new house energy supplier
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * get the devices inside the house
     * @return the devices in the house
     */
    public Map<Integer, SmartDevice> getDevices() {
        Map<Integer,SmartDevice> novo= new HashMap<>();
        for(SmartDevice a:this.devices.values()){
            novo.put(a.getId(),a.clone());
        }
        return novo;
    }

    /**
     * get the devices inside each room
     * @return the devices id in each room
     */
    public Map<String, Set<Integer>> getRooms() {
        Map<String,Set<Integer>> novo= new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : this.rooms.entrySet()) {
            novo.put(entry.getKey(),new TreeSet<>(entry.getValue()));
        }
        return novo;
    }

    /**
     * get the installations
     * @return the installations
     */
    public Map<String,Set<SmartDevice>> getInstallations() {
        Map<String,Set<SmartDevice>> novo= new HashMap<>();
        for (Map.Entry<String, Set<SmartDevice>> entry : this.installations.entrySet()) {
            novo.put(entry.getKey(),entry.getValue().stream().map(SmartDevice::clone).collect(Collectors.toSet()));
        }
        return novo;
    }

    /**
     * Checks if the house contains a device with the given id
     * @param id the id of the device
     * @return whether the house contains a device with the given id
     */
    public boolean containsDevice(int id) {
        return this.devices.containsKey(id);
    }

    /**
     * Updates a device
     * @param id the id of the old device
     * @param device the new device
     */
    public void updateDevice(int id, SmartDevice device) throws NoSuchEntityException {
        SmartDevice dev = this.devices.get(id);

        if(dev == null)
            throw new NoSuchEntityException("No entity with id " + id);

        dev.copy(device);
        dev.setOn(device.getOn());
    }


    /**
     * Gets the consumption of all the devices in the house
     * @return the consumption of all devices in the house (indexed by "<device-id>:<device-name>"
     */
    public Map<String, Double> getDevicesConsumptions() {
        Map<String, Double> result = new HashMap<>();

        for(SmartDevice device : this.devices.values()) {
            String key = String.format("%d:%s", device.getId(), device.getName());
            result.put(key, device.getEnergyCounter());
        }

        return result;
    }

    /**
     * Adds a decive to a certain room of the house
     * @param device    The device to add
     * @param room      The name of the room to add it to
     */

    public void addDevice(SmartDevice device, String room) {

        devices.put(device.getId(), device.clone());

        if(!rooms.containsKey(room))
            rooms.put(room, new HashSet<>());

        rooms.get(room).add(device.getId());

        if (!installations.containsKey(room)) {
            Set<SmartDevice> devicesInRoom = new HashSet<>();
            devicesInRoom.add(device);
            installations.put(room,devicesInRoom);
        }
        else{
            Set<SmartDevice> devicesInRoom = installations.get(room);
            devicesInRoom.add(device);
        }
    }

    /**
     * Moves the device to the room
     * @param id the id of the device
     * @param room the new room
     */
    public void moveToRoom(int id, String room) {

        if (!installations.containsKey(room)) {
            Set<SmartDevice> devicesInRoom = new HashSet<>();
            devicesInRoom.add(devices.get(id).clone());
            installations.put(room,devicesInRoom);
        }
        else{
            Set<SmartDevice> devicesInRoom = installations.get(room);
            devicesInRoom.add(devices.get(id).clone());
        }

        for(Set<Integer> devs : this.rooms.values()) {
            if(devs.contains(id))
                devs.remove(id);
        }

        if(!this.rooms.containsKey(room))
            this.rooms.put(room, new HashSet<>());

        this.rooms.get(room).add(id);
    }

    /**
     * Default constructor
     */
    public SmartHouse() {
        super();
        this.ownerName = "";
        this.ownerNif = 0;
        this.supplierId = -1;
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
        this.installations = new HashMap<>();
    }

    /**
     * Copys a SmartHouse
     * @param house the house to copy
     */
    public SmartHouse(SmartHouse house) {
        super(house);
        this.ownerName = house.getOwnerName();
        this.ownerNif = house.getOwnerNif();
        this.supplierId = house.getSupplierId();
        this.devices = house.getDevices();
        this.rooms = house.getRooms();
        this.installations = house.getInstallations();
    }
    /**
     * creates a new house
     * @param name the name of the house
     * @param ownerName the name of the owner
     * @param ownerNif the nif of the owner
     * @param supplierId the energy supplier for the house
     */
    public SmartHouse(String name, String ownerName, int ownerNif, int supplierId) {
        super(name);
        this.ownerName = ownerName;
        this.ownerNif = ownerNif;
        this.supplierId = supplierId;
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
        this.installations = new HashMap<>();
    }

    /**
     * turns all devices in the house on or off
     * @param on the state to put the devices in
     */
    public void switchAll(boolean on){
        this.devices.values().forEach(f -> f.setOn(on));
    }

    /**
     * turn a device with a given id on or off
     * @param id the id of the device
     * @param on the state to put the device in
     */
    public void switchById(int id,boolean on) throws NoSuchEntityException {
        SmartDevice device = devices.get(id);

        if(device == null)
            throw new NoSuchEntityException("No device with id " + id);

        device.setOn(on);
    }

    /**
     * turn the devices in a room on or off
     * @param room the room of the devices to alter
     * @param on the state to put the devices in
     */
    public void switchAllInRoom(String room,boolean on) throws NoSuchEntityException {
        Set<Integer> roomDevices = this.rooms.get(room);

        if(roomDevices == null)
            throw new NoSuchEntityException("No room called " + room);

        for(Integer i : roomDevices){
            this.devices.get(i).setOn(on);
        }
    }

    /**
     * Returns all devices indexed by the room they are in
     * @return all devices indexed by the room they are in
     */
    public Map<String, Set<SmartDevice>> getDevicesByRoom() {
        Map<String, Set<SmartDevice>> devices = new TreeMap<>();

        for(Map.Entry<String, Set<Integer>> kp : this.rooms.entrySet()) {
            devices.put(kp.getKey(), new HashSet<>());

            for(Integer i : kp.getValue()) {
                SmartDevice d = this.devices.get(i);

                if(d != null) {
                    devices.get(kp.getKey()).add(d.clone());
                }
            }
        }

        return devices;
    }

    /**
     * Removes the device from the house
     * @param device the device
     */
    public void removeDevice(SmartDevice device) {
        this.devices.remove(device.getId());

        for(Set<Integer> it : this.rooms.values()) {
            if(it.contains(device.getId()))
                it.remove(device.getId());
        }
    }

    @Override
    public SmartHouse clone() {
        return new SmartHouse(this);
    }

    @Override
    public void copy(AbstractEntity ent) throws WrongEntityTypeException {
        if(ent.getClass() != this.getClass())
            throw new WrongEntityTypeException("Argument is not a SmartHouse");

        SmartHouse h = (SmartHouse)ent;

        super.copy(ent);

        this.setOwnerName(h.getOwnerName());
        this.setOwnerNif(h.getOwnerNif());
        this.setSupplierId(h.getSupplierId());

        this.devices = new HashMap<>();
        for(SmartDevice a : h.devices.values()){
            devices.put(a.getId(),a.clone());
        }

        this.rooms = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : h.rooms.entrySet()) {
            rooms.put(entry.getKey(),new TreeSet<>(entry.getValue()));
        }
    }

    public void resetCounters() {
        this.devices.values().forEach(SmartDevice::resetEnergyCounter);
        this.installations.clear();
    }

    @Override
    public void advanceBy(double time) {
        for(SmartDevice dev : this.devices.values()) {
            dev.advanceBy(time);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;

        if(o == null || o.getClass() != this.getClass())
            return false;

        boolean result = super.equals(o);

        SmartHouse h = (SmartHouse)o;

        result &= this.ownerName.equals(h.getOwnerName()) && this.ownerNif == h.getOwnerNif()
                && this.devices.equals(h.getDevices()) && this.rooms.equals(h.getRooms())
                && this.supplierId == h.supplierId;

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s; owner name: '%s'; owner NIF: %d; supplier: %s; devices: {%s}; rooms: {%s}",
                super.toString(), this.ownerName, this.ownerNif, this.supplierId, this.devices.toString(), this.rooms.toString());
    }
}
