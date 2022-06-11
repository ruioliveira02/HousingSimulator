package com.housingsimulator.model;

import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.serialization.*;

import java.util.Objects;

/**
 * The data of a smartBulb
 */
public class SmartBulb extends SmartDevice implements AutoSerializable {
    public static enum LampColour { /*! the possible colours of the lamo*/
        Warm,
        Neutral,
        Cold
    }


    private float dimension;/*! the lamp dimensions*/
    private LampColour colour;/*! the colour of the lamp*/

    /**
     * Gets the dimension of the lightbulb
     * @return the dimension of the lightbulb
     */
    public float getDimension() {
        return this.dimension;
    }

    /**
     * sets the dimensions of the lightbulb
     * @param dimension the dimension of the bulb
     */
    public void setDimension(float dimension) {
        this.dimension = dimension;
    }

    /**
     * Gets the colour of the lightbulb
     * @return the colour of the lightbulb
     */
    public LampColour getColour() {
        return this.colour;
    }

    /**
     * sets the colour of the lightbulb
     * @param colour the colour of the bulb
     */
    public void setColour(LampColour colour) {
        this.colour = colour;
    }

    /**
     * Creates a new clean Smartbulb
     */
    public SmartBulb() {
        super();
        this.dimension=0;
        this.colour=LampColour.Neutral;
    }

    /**
     * Creates a copy of a Smartbulb
     * @param bulb the bulb to copy
     */
    public SmartBulb(SmartBulb bulb) {
        super(bulb);
        this.dimension = bulb.getDimension();
        this.colour = bulb.getColour();
    }

    /**
     * Creates a new Smartbulb
     * @param on the state of the bulb
     * @param name the name of the bulb
     * @param dimension the dimensions of the bulb
     * @param colour the color to set the bulb as
     */
    public SmartBulb(boolean on, String name, float dimension, LampColour colour) {
        super(on, name);
        this.dimension = dimension;
        this.colour = colour;
    }




    @Override
    public double energyOutput() {
        return this.getDimension() * SmartBulb.getColourOutput(this.colour) * (this.getOn() ? 1 : 0);
    }

    @Override
    public double installationPrice() {
        return this.getDimension() * 10;
    }

    /**
     * Checks if an object is equal to a SmartBulb
     * @param o object to compare to
     * @return true if they are equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmartBulb smartBulb = (SmartBulb) o;
        return Float.compare(smartBulb.getDimension(), this.dimension) == 0 && this.colour == smartBulb.getColour();
    }


    @Override
    public SmartBulb clone() {
        return new SmartBulb(this);
    }

    @Override
    public String toString() {
        return String.format("%s; colour: %s; dimension: %f", super.toString(), this.colour.toString(), this.dimension);
    }

    @Override
    public void copy(AbstractEntity ent) throws WrongEntityTypeException {
        if(!(ent instanceof SmartBulb))
            throw new WrongEntityTypeException("Entity cannot be of type " + ent.getClass().getName());

        super.copy(ent);

        SmartBulb bulb = (SmartBulb)ent;
        this.setColour(bulb.getColour());
        this.setDimension(bulb.getDimension());
    }



    /**
     * Returns the energy output multiplier of a colour
     * @param colour the given colour
     * @return the energy output
     */
    private static double getColourOutput(LampColour colour) {
        return switch (colour) {
            case Warm -> 3.0;
            case Neutral -> 2.0;
            case Cold -> 1.0;
        };
    }
}
