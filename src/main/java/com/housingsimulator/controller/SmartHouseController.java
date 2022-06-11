package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.controller.event.AddHouseEvent;
import com.housingsimulator.controller.event.DeleteEntityEvent;
import com.housingsimulator.controller.event.UpdateHouseEvent;
import com.housingsimulator.exceptions.NoHousesExistException;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.SmartHouseView;

import java.util.*;

@API
public class SmartHouseController extends Controller{
    /**
     * Default constructor
     * @param model application model
     */
    public SmartHouseController(Model model) {
        this.setModel(model);
        this.setView(new SmartHouseView());
    }
    /**
     * Gets all the houses in the simulation
     */
    @Endpoint(regex="HOUSE LIST ALL")
    public void getDevices() {
        Set<SmartHouse> houses = this.getModel().getSimulation().getHouses();

        this.callViewOnCollection(houses);
    }
    /**
     *  Creates a new house
     * @param name the name of the house
     * @param nif the nif of the owner
     * @param owner the owner's name
     * @param energySupplierId the id of the energy supplier of that house
     */
    @Endpoint(regex="HOUSE ADD NAME=(.+) NIF=(\\d+) OWNER=(.+) ENERGY_SUPPLIER_ID=(\\d+)")
    public void addHouse(String name, Integer nif, String owner, Integer energySupplierId) {
        Entity es =this.getModel().getSimulation().getEntity(energySupplierId);
        if(es == null) {
            this.getView().error("No such energy supplier");
        } else if(es instanceof EnergySupplier) {
            this.getModel().addSimulatorEvent(new AddHouseEvent(this.getModel().getTime(), new SmartHouse(name, owner, nif, energySupplierId)));
            this.getModel().updateState();
        } else {
            this.getView().warning("An entity with the supplied id exists but is not a Energy Supplier");
        }
    }
    /**
     *  Creates a new house
     * @param name the name of the house
     * @param nif the nif of the owner
     * @param owner the owner's name
     * @param energySupplierId the id of the energy supplier of that house
     */
    @Endpoint(regex="HOUSE UPDATE ID=(\\d+) NAME=(.+) NIF=(\\d+) OWNER=(.+) ENERGY_SUPPLIER_ID=(\\d+)")
    public void updateHouse(Integer id,String name, Integer nif, String owner, Integer energySupplierId) {
        Entity es =this.getModel().getSimulation().getEntity(energySupplierId);
        if(es == null) {
            this.getView().error("No such energy supplier");
        } else if(es instanceof EnergySupplier) {
            Entity h =this.getModel().getSimulation().getEntity(id);
            if(h == null) {
                this.getView().error("No such house");
            } else if(h instanceof SmartHouse) {
                this.getModel().addSimulatorEvent(new UpdateHouseEvent(this.getModel().getTime(), id, name, nif, owner, energySupplierId));
                this.getModel().updateState();
            }
        }
    }

    /**
     * changes the energy supplier of a house
     * @param id the id of the house
     * @param energySupplierId the new energy supplier
     */
    @Endpoint(regex = "HOUSE CHANGE_SUPPLIER ID=(\\d+) ENERGY_SUPPLIER_ID=(\\d+)")
    public void changeHouseSupplier(Integer id,Integer energySupplierId){
        Entity es =this.getModel().getSimulation().getEntity(energySupplierId);
        if(es == null) {
            this.getView().error("No such energy supplier");
        } else if(es instanceof EnergySupplier) {
            Entity h =this.getModel().getSimulation().getEntity(id);
            if(h == null) {
                this.getView().error("No such house");
            } else if(h instanceof SmartHouse) {
                this.getModel().addSimulatorEvent(new UpdateHouseEvent(this.getModel().getTime(), id, energySupplierId));
                this.getModel().updateState();
            }
        } else {
            this.getView().warning("Entity with id " + energySupplierId + " exists but is not a supplier");
        }
    }

    /**
     * deletes a house from a simulation
     * @param id the id of the house
     */
    @Endpoint(regex = "HOUSE DELETE ID=(\\d+)")
    public void deleteHouse(Integer id){
        Entity h =this.getModel().getSimulation().getEntity(id);
        if(h == null) {
            this.getView().error("No such house");
        } else if(h instanceof SmartHouse) {
            this.getModel().addSimulatorEvent(new DeleteEntityEvent(this.getModel().getTime(), id));
            this.getModel().updateState();
        }
    }




    /**
     *
     * @param id the id of the house to get
     */
    @Endpoint(regex = "HOUSE GET ID=(\\d+)")
    public void getHouseById(Integer id) {
        Entity e = this.getModel().getSimulation().getEntity(id);

        if(e == null) {
            this.getView().error("No such entity");
        } else if(e instanceof SmartHouse) {
            SmartHouse h = (SmartHouse)e;
            EnergySupplier es = (EnergySupplier) this.getModel().getSimulation().getEntity(h.getSupplierId());
            this.getView().showHouse(h.getId(), h.getName(),h.getOwnerName(),h.getOwnerNif(),es.getId(),es.getName(),
                    es.getBaseValue(), es.getTax(), es.getPriceFormula(),h.getRooms());
        } else {
            this.getView().warning("An entity with the supplied id exists but is not a house");
        }
    }

    /**
     * the house which spent the most in the particular time period
     * @param start the starting date of the time period
     * @param end the ending date of the time period
     */
    @Endpoint(regex = "HOUSE SPENT MOST START=(\\d+\\.?\\d*) END=(\\d+\\.?\\d*)")
    public void getHouseWhichSpentMost(Double start,Double end){
        try{
            if (start<end && end<=this.getModel().getTime()) {
            SmartHouse h = this.getModel().houseSpentTheMost(start,end);
            EnergySupplier es = (EnergySupplier) this.getModel().getSimulation().getEntity(h.getSupplierId());
            this.getView().showHouse(h.getId(), h.getName(),h.getOwnerName(),h.getOwnerNif(),es.getId(),es.getName(),
                    es.getBaseValue(), es.getTax(), es.getPriceFormula(),h.getRooms());
            }
            else{
                this.getView().warning("either the start time is smaller than the end or the end is bigger than the current: " + this.getModel().getTime());
            }
        } catch (NoHousesExistException e) {
            this.getView().warning(e.getMessage());
        }
    }

    /**
     * the house which consumed the most energy in the particular time period
     * @param start the starting date of the time period
     * @param end the ending date of the time period
     */
    @Endpoint(regex = "HOUSE GET BY ENERGY CONSUMPTION START=(\\d+\\.?\\d*) END=(\\d+\\.?\\d*)")
    public void getHouseByEnergyConsumptionDouble(Double start,Double end){
        try{
            if (start<end && end<=this.getModel().getTime()) {
                List<SmartHouse> houses = this.getModel().orderHousesByEnergyConsumption(start, end);
                callViewOnCollection(houses);
            }
            else{
                this.getView().warning("either the start time is smaller than the end or the end is bigger than the current: " + this.getModel().getTime());
            }
        } catch (NoHousesExistException e) {
            this.getView().warning(e.getMessage());
        }
    }


    /**
     * Calls the view on a collection of houses showing the consumption
     * @param houses the houses
     */
    private void callViewOnCollection(List<SmartHouse> houses, Map<Integer,Double> energyUsedbyHouse) {
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> ownerName = new ArrayList<>();
        List<Integer> ownerNif = new ArrayList<>();
        List<Integer> supplierId = new ArrayList<>();
        List<String> supplierName = new ArrayList<>();
        List<Double> supplierBaseValue = new ArrayList<>();
        List<Double> supplierTax = new ArrayList<>();
        List<String> supplierFormula = new ArrayList<>();
        List<Map<String, Set<Integer>>> devicesPerRoom = new ArrayList<>();
        List<Double> energyUsed = new ArrayList<>();




        for(SmartHouse h : houses) {
            ids.add(h.getId());
            names.add(h.getName());
            ownerName.add(h.getOwnerName());
            ownerNif.add(h.getOwnerNif());
            EnergySupplier supplier = (EnergySupplier) this.getModel().getSimulation().getEntity(h.getSupplierId());
            supplierId.add(supplier.getId());
            supplierName.add(supplier.getName());
            supplierBaseValue.add(supplier.getBaseValue());
            supplierTax.add(supplier.getTax());
            supplierFormula.add(supplier.getPriceFormula());
            devicesPerRoom.add(h.getRooms());
            energyUsed.add(energyUsedbyHouse.get(h.getId()));
        }

        this.getView().showHousesConsumption(houses.size(),ids, names,ownerName,ownerNif,supplierId,supplierName,supplierBaseValue,
                supplierTax,supplierFormula,devicesPerRoom,energyUsed);
    }


    /**
     * Calls the view on a collection of houses
     * @param houses the houses
     */
    private void callViewOnCollection(Collection<SmartHouse> houses) {
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> ownerName = new ArrayList<>();
        List<Integer> ownerNif = new ArrayList<>();
        List<Integer> supplierId = new ArrayList<>();
        List<String> supplierName = new ArrayList<>();
        List<Double> supplierBaseValue = new ArrayList<>();
        List<Double> supplierTax = new ArrayList<>();
        List<String> supplierFormula = new ArrayList<>();
        List<Map<String, Set<Integer>>> devicesPerRoom = new ArrayList<>();




        for(SmartHouse h : houses) {
            ids.add(h.getId());
            names.add(h.getName());
            ownerName.add(h.getOwnerName());
            ownerNif.add(h.getOwnerNif());
            EnergySupplier supplier = (EnergySupplier) this.getModel().getSimulation().getEntity(h.getSupplierId());
            supplierId.add(supplier.getId());
            supplierName.add(supplier.getName());
            supplierBaseValue.add(supplier.getBaseValue());
            supplierTax.add(supplier.getTax());
            supplierFormula.add(supplier.getPriceFormula());
            devicesPerRoom.add(h.getRooms());
        }

        this.getView().showHouses(houses.size(),ids, names,ownerName,ownerNif,supplierId,supplierName,supplierBaseValue,
                supplierTax,supplierFormula,devicesPerRoom);
    }

    /**
     * Gets the used view in the correct type
     * @return the view
     */
    @Override
    public SmartHouseView getView() {
        return (SmartHouseView) super.getView();
    }
}
