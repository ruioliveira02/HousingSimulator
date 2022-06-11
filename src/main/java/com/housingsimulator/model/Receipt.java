package com.housingsimulator.model;

import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.simulation.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Data of a receipt
 */
public class Receipt implements AutoSerializable, Serializable {
    private double startTime;/*! the start time the receipt refers to */
    private double endTime; /*! the end time the receipt refers to */
    private EnergySupplier supplier; /*! the supplier who emitted the receipt */
    private SmartHouse customer; /*! the customer of the receipt*/

    private List<Installation> installations; /*! the names of installed  */
    private Map<String, Double> consumptions; /*! the consumptions in the house indexed by device */

    private double price; /*! That's the price to paaay */


    /**
     * Default constructor
     */
    public Receipt() {
        this.startTime = 0.0;
        this.endTime = 0.0;
        this.supplier = null;
        this.customer = null;
        this.price = 0.0;
        this.installations = new ArrayList<>();
        this.consumptions = new HashMap<>();
    }
    /**
     * creates a new receipt
     * @param startTime the start time the receipt refers to
     * @param endTime the end time the receipt refers to
     * @param supplier the energy supplier who emitted the receipt
     * @param customer the customer who the receipt was for
     */
    public Receipt(double startTime, double endTime, EnergySupplier supplier, SmartHouse customer, List<Installation> installations,
                   Map<String, Double> consumptions, double price) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.supplier = supplier.clone();
        this.customer = customer.clone();
        this.price = price;
        this.installations = new ArrayList<>(installations);
        this.consumptions = new HashMap<>(consumptions);
    }

    /**
     * creates a receipt copying the data from another
     * @param r the receipt to copy from
     */
    public Receipt(Receipt r) {
        this.startTime = r.getStartTime();
        this.endTime = r.getEndTime();
        this.supplier = r.getSupplier();
        this.customer = r.getCustomer();
        this.price = r.getPrice();
        this.consumptions = r.getConsumptions();
        this.installations = r.getInstallations();
    }

    /**
     * Gets the value charged to the customer
     * @return the value charged to the customer
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * gets the installation done in the receipt
     * @return the installations
     */
    public List<Installation> getInstallations() {return new ArrayList<>(this.installations);}

    /**
     * Gets the consumptions of the devices
     * @return the consumptions of the devices
     */
    public Map<String, Double> getConsumptions() {
        return new HashMap<>(this.consumptions);
    }

    /**
     * Gets the start time the receipt refers to
     * @return the start time the receipt refers to
     */
    public double getStartTime() {
        return this.startTime;
    }

    /**
     * Gets the end time the receipt refers to
     * @return the end time the receipt refers to
     */
    public double getEndTime() {
        return this.endTime;
    }

    /**
     * gets the energy supplier who emitted the receipt
     * @return the energy supplier who emitted the receipt
     */
    public EnergySupplier getSupplier() {
        return this.supplier.clone();
    }

    /**
     * gets the customer the receipt was emitted to
     * @return the customer the receipt was emitted to
     */
    public SmartHouse getCustomer() {
        return this.customer.clone();
    }

    /**
     * sets the start time the receipt refers to
     * @param time the start time the receipt refers to
     */
    public void setStartTime(double time) {
        this.startTime = time;
    }

    /**
     * sets the end time the receipt refers to
     * @param time the end time the receipt refers to
     */
    public void setEndTime(double time) {
        this.endTime = time;
    }

    /**
     * sets who emitted the receipt
     * @param supplier the new receipt emitter
     */
    public void setSupplier(EnergySupplier supplier) {
        this.supplier = supplier.clone();
    }

    /**
     * sets the customer the receipt is emitted to
     * @param customer the new recipient of the receipt
     */
    public void setCustomer(SmartHouse customer) {
        this.customer = customer.clone();
    }

    /**
     * Sets the price charged to the customer
     * @param price the price charged to the customer
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the devices' consumption values
     * @param consumptions the devices' consumption values
     */
    public void setConsumptions(Map<String, Double> consumptions) {
        this.consumptions = new HashMap<>(consumptions);
    }

    /**
     * Sets the device installation values
     * @param ins the installation values
     */
    public void setInstallations(List<Installation> ins){this.installations=new ArrayList<>(installations);}


    /**
     * serializes the receipt
     * @return the string representation of a reciept
     */
    @Override
    public String toString() {
        StringBuilder instalationAndConsumptions = new StringBuilder();
        instalationAndConsumptions.append("Consumptions:\n");
        instalationAndConsumptions.append("     Device:Consumption\n");
        this.consumptions.forEach((d,c)-> instalationAndConsumptions.append("      ").append(d).append(" : ").append(c).append("\n"));
        instalationAndConsumptions.append("Installations:\n");
        instalationAndConsumptions.append("     Device:Room:Price\n");
        this.installations.forEach(i-> instalationAndConsumptions.append("      ").append(i.deviceName()).append(" : ").append(i.room()).append(" : ").append(i.price()).append("\n"));


        return String.format("supplier: {%s}; customer: {%s}; time-frame: [%f-%f]; price: %f;\n%s\n",
                this.supplier.toString(), this.customer.toString(),this.startTime, this.endTime,  this.price,instalationAndConsumptions.toString());
    }

    /**
     * checks if an object is equal to a receipt
     * @param o object to compare to
     * @return true if they are equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Math.abs(this.startTime - receipt.getStartTime()) <= 1e-6 &&  Math.abs(this.endTime - receipt.getEndTime()) <= 1e-6 &&
                Math.abs(this.price - receipt.getPrice()) <= 1e-6 &&
                this.supplier.equals(receipt.getSupplier()) && this.customer.equals(receipt.getCustomer())
                && receipt.getConsumptions().entrySet().stream().map(e->this.consumptions.containsKey(e.getKey()) && this.consumptions.get(e.getKey())==e.getValue()).reduce(true,(ac,b)->ac && b)
                && receipt.getInstallations().size() == this.installations.size() && new HashSet<>(receipt.getInstallations()).containsAll(this.installations);
    }

    /**
     * creates a copy of a receipt
     * @return the copy of a receipt
     */
    @Override
    public Receipt clone() {
        return new Receipt(this);
    }
}
