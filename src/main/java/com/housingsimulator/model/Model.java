package com.housingsimulator.model;

import com.housingsimulator.controller.event.BillingEvent;
import com.housingsimulator.controller.event.UpdateHouseEvent;
import com.housingsimulator.exceptions.*;
import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.serialization.DeserializationException;
import com.housingsimulator.serialization.Deserializer;
import com.housingsimulator.serialization.Serializer;
import com.housingsimulator.simulation.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Main wrapper for all the business logic of the application
 */
public class Model implements Serializable {
    private Simulation simulation; /*! The simulation */

    private double time; /*! The current time in the simulation */

    private Simulator simulator; /*! The simulator */

    private TreeMap<Double, List<Receipt>> receipts; /* Emitted receipts, stored by the date of their emission (endDate) */

    /**
     * Default constructor
     */
    public Model() {
        this.simulation = new Simulation();
        this.simulator = new Simulator();
        this.receipts = new TreeMap<>();
    }

    /**
     * Gets the simulation
     * @return the simulation
     */
    public Simulation getSimulation() {
        return this.simulation;
    }

    /**
     * Gets the current time of the simulation
     * @return the current time of the simulation
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Sets the current time of the simulation
     * @param time the time (in seconds) to set the simulation to
     */
    public void setTime(double time) throws InvalidFormulaException
    {
        double oldTime=this.time;
        this.time = time;
        updateState();

        if (this.time > oldTime)
            this.receipts.put(this.time, emitReceipts(receipts.isEmpty() ? 0 : receipts.lastKey()));
        else if (this.time < oldTime)
        {
            Double lastBilling = this.receipts.floorKey(this.time);
            if (lastBilling == null)
                lastBilling = (double)0;

            if (this.time > lastBilling) {
                List<Receipt> aux = emitReceipts(lastBilling);
                this.receipts.put(this.time, aux);
            }

            this.receipts.tailMap(this.time, false).clear();
            simulator.removeEvents(e -> e instanceof BillingEvent && e.getTime() > this.getTime());
        }
    }

    public void addSimulatorEvent(Event e) {
        this.simulator.addEvent(e);
    }

    public void copy(Model model) {
        this.time = model.getTime();
        this.receipts = new TreeMap<>();
        model.receipts.entrySet().forEach(p -> this.receipts.put(p.getKey(), new ArrayList<>(p.getValue())));
        this.simulator = model.simulator;
        this.updateState();
    }

    public List<Receipt> getLastReceipts() {
        return receipts.isEmpty() ? null : receipts.lastEntry().getValue();
    }

    public List<Receipt> getReceiptsBySupplier(int supplierId) {
        return receipts.values().stream()
                .flatMap(List::stream)
                .filter(r -> r.getSupplier().getId() == supplierId)
                .collect(Collectors.toList());
    }

    public List<Receipt> getReceiptsByHouse(int houseId) {
        return receipts.values().stream()
                .flatMap(List::stream)
                .filter(r -> r.getCustomer().getId() == houseId)
                .collect(Collectors.toList());
    }

    /**
     * Returns all receipts emitted at or after startDate, and starting before endDate
     */
    public List<Receipt> getReceiptsBySupplier(int supplierId, double startDate, double endDate) {
        return receipts.tailMap(startDate, true)
                .headMap(receipts.ceilingKey(endDate), true)
                .values().stream()
                .flatMap(List::stream)
                .filter(r -> r.getSupplier().getId() == supplierId)
                .collect(Collectors.toList());
    }

    /**
     * Returns all receipts emitted at or after startDate, and starting before endDate
     */
    public List<Receipt> getReceiptsByHouse(int houseId, double startDate, double endDate) {
        return receipts.tailMap(startDate, true)
                .headMap(receipts.ceilingKey(endDate), true)
                .values().stream()
                .flatMap(List::stream)
                .filter(r -> r.getCustomer().getId() == houseId)
                .collect(Collectors.toList());
    }

    /**
     * Takes into account changes made to the simulator. If they affect the current state of the simulation, it is updated
     */
    public void updateState() {
        this.simulation = new Simulation(this.simulator.getState(time));
    }


    private List<Receipt> emitReceipts(double lastBilling) throws InvalidFormulaException {
        List<Receipt> receipts = new ArrayList<>();

        for (SmartHouse house : this.simulation.getHouses())
            receipts.add(((EnergySupplier)this.simulation.getEntity(house.getSupplierId())).billHouse(house, lastBilling, this.time));

        this.addSimulatorEvent(new BillingEvent(this.time));
        this.updateState();
        return receipts;
    }



    public SmartHouse houseSpentTheMost(double start,double end) throws NoHousesExistException{
        Map<SmartHouse,Double> receiptsByHouse = new HashMap<>();
        Set<SmartHouse> houses = this.simulation.getHouses();
        if (houses.isEmpty()) throw new NoHousesExistException("the are no houses in the simulation");
        for (SmartHouse h:houses){
            receiptsByHouse.put(h,this.getReceiptsByHouse(h.getId(),start,end).stream().map(Receipt::getPrice).
                    reduce(0.0,Double::sum));
        }
        List<Map.Entry<SmartHouse,Double>> pricesL = new ArrayList<>(receiptsByHouse.entrySet().stream().toList());
        pricesL.sort(Map.Entry.comparingByValue());
        return pricesL.get(pricesL.size()-1).getKey();
    }


    /**
     * Orders house by their energy consumption from biggest to smallest
     * @param start the starting date
     * @param end the ending date
     * @return a list of houses in decreasing order of energy consumption
     */
    public List<SmartHouse> orderHousesByEnergyConsumption(double start,double end) throws NoHousesExistException{
        Map<SmartHouse,Double> receiptsByHouse = new HashMap<>();
        Set<SmartHouse> houses = this.simulation.getHouses();
        if (houses.isEmpty()) throw new NoHousesExistException("the are no houses in the simulation");
        for (SmartHouse h:houses){
            receiptsByHouse.put(h,this.getReceiptsByHouse(h.getId(),start,end).stream().
                    map(r-> r.getConsumptions().values().stream().reduce(0.0,Double::sum)).
                    reduce(0.0,Double::sum));
        }
        List<Map.Entry<SmartHouse,Double>> pricesL = new ArrayList<>(receiptsByHouse.entrySet().stream().toList());
        pricesL.sort(Map.Entry.comparingByValue());
        Collections.reverse(pricesL);

        return pricesL.stream().map(Map.Entry::getKey).toList();
    }

    /**
     * selects the energy supplier which made the most money
     * @return the best selling supplier
     */
    public EnergySupplier bestSellingSupplier() throws NoEnergySuppliersExistException {
        Set<EnergySupplier> suppliers = this.getSimulation().getSuppliers();
        if (suppliers.isEmpty()) throw new NoEnergySuppliersExistException("the are no houses in the simulation");
        Map<EnergySupplier,Double> soldPerSupplier = new HashMap<>();
        for (EnergySupplier s:suppliers){
            soldPerSupplier.put(s,this.getReceiptsBySupplier(s.getId()).stream().map(Receipt::getPrice).reduce(0.0,Double::sum));
        }
        List<Map.Entry<EnergySupplier, Double>> pricesL = new ArrayList<>(soldPerSupplier.entrySet().stream().toList());
        pricesL.sort(Map.Entry.comparingByValue());
        return pricesL.get(pricesL.size()-1).getKey();
    }
}
