package com.housingsimulator.model;

import com.housingsimulator.exceptions.IllegalResolutionException;
import com.housingsimulator.serialization.AutoSerializable;

import java.util.Arrays;

/**
 * A SmartCamera
 */
public class SmartCamera extends SmartDevice implements AutoSerializable{
    private int[] cameraResolution; /*! the resolution of the camera */
    private long fileResolution; /*! the fileResolution that the camera records */

    /**
     * gets the camera resolution
     * @return the camera resolution
     */
    public int[] getCameraResolution() {
        return Arrays.copyOf(this.cameraResolution, 2);
    }
    /**
     * gets the file resolution
     * @return the file resolution
     */
    public long getFileResolution() {
        return this.fileResolution;
    }

    /**
     * sets the cameraResolution to a new value
     * @param cameraResolution the new cameraResolution
     */
    public void setCameraResolution(int[] cameraResolution) throws IllegalResolutionException {

        if(cameraResolution.length != 2)
            throw new IllegalResolutionException("Attempted to set camera resolution with an array of length" +
                    cameraResolution.length);

        this.cameraResolution =  Arrays.copyOf(cameraResolution, 2);
    }
    /**
     * sets the fileResolution to a new value
     * @param fileResolution the new fileResolution
     */
    public void setFileResolution(long fileResolution){

        this.fileResolution = fileResolution;
    }

    /**
     * Creates a clean SmartCamera
     */
    public SmartCamera() {
        super();
        this.cameraResolution = new int[] { 0, 0 };
        this.fileResolution = 0;
    }

    /**
     * Parametrized constructor
     * @param on whether the camera is on
     * @param name the name of the camera
     * @param cameraResolution the resolution of the camera (X x Y)
     * @param fileResolution the resolution of the output file of the camera (X x Y)
     */
    public SmartCamera(boolean on, String name, int [] cameraResolution, long fileResolution)
            throws IllegalResolutionException {
        super(on,name);

        if(cameraResolution.length != 2)
            throw new IllegalResolutionException("Attempted to set camera resolution with an array of length" +
                    cameraResolution.length);


        this.cameraResolution=Arrays.copyOf(cameraResolution, cameraResolution.length);
        this.fileResolution=fileResolution;
    }

    /**
     * Creates a new SmartCamera copying the data from another one
     * @param s smartCamera from where to copy the data
     */
    public SmartCamera(SmartCamera s){
        super(s);
        this.cameraResolution = new int[2];
        this.fileResolution = s.getFileResolution();
        System.arraycopy(s.getCameraResolution(), 0, this.cameraResolution, 0, 2);
    }

    @Override
    public double energyOutput() {
        return (this.getOn() ? 1 : 0) * this.fileResolution
                * this.cameraResolution[0] * this.cameraResolution[1] / 1e12;
    }

    @Override
    public double installationPrice() {
        return this.cameraResolution[0] * this.cameraResolution[1] * 0.0001;
    }

    /**
     * serializes a SmartCamera
     * @return the representation in a string of a smartCamera
     */
    @Override
    public String toString() {
        return String.format("%s; cameraResolution: %dx%d; fileResolution: %d",super.toString(), this.cameraResolution[0],
                this.cameraResolution[1], this.fileResolution);
    }

    @Override
    public SmartCamera clone() {
        return new SmartCamera(this);
    }
}
