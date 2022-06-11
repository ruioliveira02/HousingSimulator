package com.housingsimulator.model;

import com.housingsimulator.serialization.AutoSerializable;

/**
 *Data of a SpeakerBrand
 */
public class SpeakerBrand extends AbstractEntity implements AutoSerializable {
    private int dailyConsumption;/*! the daily consumption of the speakers of a given brand*/
    private double installationCost; /*! the cost of installation */

    /**
     * Create a new clean speakerBrand
     */
    public SpeakerBrand(){
        super("");
        this.dailyConsumption=0;
        this.installationCost = 0.0;
    }

    /**
     * Creates a new speaker brand setting its attributes
     * @param name the name of the brand
     * @param dailyConsumption the daily consumption of the speakers of the brand
     */
    public SpeakerBrand(String name, int dailyConsumption, double installationCost) {
        super(name);
        this.dailyConsumption = dailyConsumption;
        this.installationCost = installationCost;
    }

    /**
     * Create a new brand copying the data of another
     * @param s the brand to copy the data from
     */
    public SpeakerBrand(SpeakerBrand s){
        super(s);
        this.dailyConsumption=  s.getDailyConsumption();
        this.installationCost = s.getInstallationPrice();
    }

    /**
     * Gets the daily consumption of the speakers of the brand
     * @return the daily consumption of the speakers of the brand
     */
    public int getDailyConsumption() {
        return this.dailyConsumption;
    }

    /**
     * sets the value of the daily consumption of a speaker's brand
     * @param dailyConsumption the value o set the daily consumption of a brand as
     */
    public void setDailyConsumption(int dailyConsumption) {
        this.dailyConsumption = dailyConsumption;
    }

    /**
     * Retrieves the price paid to install the speaker
     * @return The price
     */
    public double getInstallationPrice() {
        return this.installationCost;
    }

    /**
     * Sets the installation cost of a brand
     * @param installationCost the new installation cost
     */
    public void setInstallationCost(double installationCost) {
        this.installationCost = installationCost;
    }

    /**
     * Checks if an object is equal to a brand
     * @param o the object to compare
     * @return true if they are equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !super.equals(o)) return false;
        SpeakerBrand that = (SpeakerBrand) o;
        return this.dailyConsumption == that.getDailyConsumption()
                && Double.compare(this.installationCost, that.getInstallationPrice()) == 0;
    }

    /**
     * serializes a speaker brand
     * @return a string representing a speaker brand
     */
    @Override
    public String toString() {
        return String.format("name: '%s'; dailyConsumption: %d; installationPrice: %f", this.getName(),
                this.dailyConsumption, this.installationCost);
    }

    /**
     * creates a copy of the SpeakerBrand object
     * @return a copy of the SpeakerBrand object
     */
    public SpeakerBrand clone(){
        return new SpeakerBrand(this);
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

}
