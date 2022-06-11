package com.housingsimulator.model;

import com.housingsimulator.exceptions.IllegalVolumeException;
import com.housingsimulator.serialization.AutoSerializable;

import java.io.Serializable;

/**
 * Data of a SmartSpeaker
 */
public class SmartSpeaker extends SmartDevice implements AutoSerializable, Serializable {
    private int volume;/*! The current volume of the speaker*/
    private String radioStation;/*! The current radio station the speaker is playing*/
    private SpeakerBrand brand;/*! Brand of the speaker*/

    /**
     * Gets the current volume of the speaker
     * @return the volume of the speaker
     */
    public int getVolume() {
        return this.volume;
    }

    /**
     * Gets the current radio station the speaker is playing
     * @return the radio station of the speaker
     */
    public String getRadioStation() {
        return this.radioStation;
    }

    /**
     * Gets the  brand of the speaker
     * @return the brand of the speaker
     */
    public SpeakerBrand getBrand() {
        return this.brand.clone();
    }

    /**
     * Sets the volume to a given number
     * @param volume the volume to set the speaker to
     */
    public void setVolume(int volume) {
        if(volume >= 0 && volume <= 100)
            this.volume = volume;
        else
            throw new IllegalVolumeException("Volume cannot be equal to " + volume);
    }

    /**
     * sets the radio station the speaker is playing
     * @param radioStation the station to set the speaker to
     */
    public void setRadioStation(String radioStation) {
        this.radioStation = radioStation;
    }

    /**
     * sets the brand that made the speaker
     * @param brand the brand to set the speaker as
     */
    public void setBrand(SpeakerBrand brand) {
        this.brand = brand.clone();
    }

    /**
     * Creates a new clean Speaker of a given brand
     * @param brand the brand that makes the speaker
     */
    public SmartSpeaker(SpeakerBrand brand){
        super();
        this.volume=0;
        this.radioStation="";
        this.brand=brand.clone();
    }

    /**
     * Default constructor
     */
    public SmartSpeaker() {
        super();
        this.volume=0;
        this.radioStation="";
        this.brand = new SpeakerBrand();
    }
    /**
     * Creates a new Speaker with a given name and brand
     * @param name the name of the new speaker
     * @param brand the brand that makes the speaker
     */
    public SmartSpeaker(String name, SpeakerBrand brand) {
        super(name);
        this.volume=0;
        this.radioStation="";
        this.brand=brand.clone();
    }

    /**
     * Creates a new Speaker with a given name volume,name,brand and state
     * @param on the current state of the speaker
     * @param name the name of the speaker
     * @param volume the volume of the speaker
     * @param station the radio station
     * @param brand the brand that makes the speaker
     */
    public SmartSpeaker(boolean on, String name, int volume, String station, SpeakerBrand brand) {
        super(on, name);

        if(volume < 0 || volume > 100)
            throw new IllegalVolumeException("Volume cannot be equal to " + volume);

        this.volume = volume;
        this.radioStation = station;
        this.brand=brand.clone();
    }

    /**
     * Creates a new speaker copying the information another speaker
     * @param s the speaker to copy the information from
     */
    public SmartSpeaker(SmartSpeaker s) {
        super(s);
        this.volume = s.getVolume();
        this.radioStation=s.getRadioStation();
        this.brand=s.getBrand();
    }

    @Override
    public double energyOutput() {
        return this.getVolume() / 100.0 * this.brand.getDailyConsumption() / 24 * (this.getOn() ? 1 : 0);
    }

    @Override
    public double installationPrice() {
        return this.brand.getInstallationPrice();
    }

    /**
     * creates a copy of a SmartSpeaker instance
     * @return a SmartSpeaker in the form of a smartDevice
     */
    @Override
    public SmartDevice clone() {
        return new SmartSpeaker(this);
    }

    /**
     * Checks it two instances are equal to each other
     * @param o object to compare to
     * @return if the object are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmartSpeaker that = (SmartSpeaker) o;
        return this.volume == that.getVolume() && this.radioStation.equals(that.getRadioStation()) && this.brand.equals(that.getBrand());
    }

    @Override
    public String toString() {
        return String.format("%s; brand: {%s}; volume: %d; station: '%s'", super.toString(), this.brand.toString(), this.volume,
                this.radioStation);
    }
}

