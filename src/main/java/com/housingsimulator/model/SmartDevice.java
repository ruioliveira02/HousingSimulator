package com.housingsimulator.model;

import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.simulation.Entity;

import java.io.Serializable;

/**
 * Data of an SmartDevice
 */
public abstract class SmartDevice extends AbstractEntity implements AutoSerializable {
    private boolean on; /*! whether a Smart Device is turned on*/
    private double energyCounter; /*! starts at 0 and increases until reset */

    /**
     * Retrieves the information if a device is turned one
     * @return the device state
     */
    public boolean getOn() {
        return this.on;
    }

    /**
     * Sets the state of a smartDevice
     * @param on value to set the smart Device state as
     */
    public void setOn(boolean on) {//@vasques n pode ser switch pq é reservado
        this.on = on;
    }

    /**
     * Retrieves the amount of energy spent since last counter reset
     * @return The amount of energy
     */
    public double getEnergyCounter() {
        return this.energyCounter;
    }

    /**
     * Resets the energy counter
     */
    public void resetEnergyCounter() {
        this.energyCounter = 0;
    }

    /**
     * Creates a new clean SmartDevice
     */
    public SmartDevice() {
        super("");
        this.on=false;
    }

    /**
     * Creates a new SmartDevice
     * @param name the name to set as the device's name as
     */
    public SmartDevice(String name) {
        super(name);
        this.on = false;
        this.energyCounter = 0;
    }

    /**
     * Creates a new SmartDevice
     * @param on the state to set the device's state as
     * @param name the name to set the device's name as
     */
    public SmartDevice(boolean on, String name) {
        super(name);
        this.on = on;
        this.energyCounter = 0;
    }

    /**
     * Creates a new SmartDevice
     * @param s the device from where to copy the information
     */
    public SmartDevice(SmartDevice s){
        super(s);
        this.on=s.getOn();
        this.energyCounter = s.getEnergyCounter();
    }

    /**
     * Gets the energy output (per unit of time) of the device
     * @return the energy output (per unit of time) of the device
     */
    public abstract double energyOutput();

    /**
     * Gets the price paid to install the device
     * @return the price
     */
    public abstract double installationPrice();

    /**
     * Compares if a object to the smart device intance
     * @param o object to compare to
     * @return true if the objects are equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SmartDevice that = (SmartDevice) o;
        return super.equals(o) && this.on == that.getOn();
    }

    /**
     * Serializes a smartDevice
     * @return a way to represent a smartDevice as a string
     */
    @Override
    public String toString() {
        return String.format("%s; on: %s", super.toString(), new Boolean(on).toString());
    }

    /**
     * calculates the energy output using the time sinse last cicle
     * @param time  The amount of time to advance the entity by
     */
    @Override
    public void advanceBy(double time) {
        this.energyCounter += time * this.energyOutput();
    }

    public abstract SmartDevice clone();

    /**
     * creates a copy of the device
     * @param ent the entity to copy
     * @throws WrongEntityTypeException
     */
    @Override
    public void copy(AbstractEntity ent) throws WrongEntityTypeException {
        if(!(ent instanceof SmartDevice))
            throw new WrongEntityTypeException("Entity cannot be of type " + ent.getClass().getName());

        super.copy(ent);

        SmartDevice d = (SmartDevice) ent;
        this.energyCounter=d.getEnergyCounter();
        this.on=d.getOn();
    }
}
