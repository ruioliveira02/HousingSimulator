package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.exceptions.InvalidFormulaException;
import com.housingsimulator.model.Model;
import com.housingsimulator.view.SimulationView;

/**
 * Controller for high level simulation commands
 */
@API
public class SimulationController extends Controller {
    /**
     * Default constructor
     * @param model the model of the application
     */
    public SimulationController(Model model) {
        this.setModel(model);
        this.setView(new SimulationView());
    }

    /**
     * Sets the current moment in the simulation
     * @param time the time to move to
     */
    @Endpoint(regex="SIMULATION MOMENT SET=(\\d+\\.?\\d*)")
    public void setMoment(Double time) {
        try {
            this.getModel().setTime(time);
            getView().successSet();
        } catch (InvalidFormulaException e) {
            getView().error("Time set failed: " + e.getMessage());
        }
    }

    /**
     * gets the current moment in the simulation
     */
    @Endpoint(regex="SIMULATION MOMENT GET")
    public void getMoment() {
        getView().successGet(this.getModel().getTime());
    }

    /**
     * Adds the specified time to the current moment in the simulation
     * @param time the time to add
     */
    @Endpoint(regex="SIMULATION MOMENT ADD=(\\d+\\.?\\d*)")
    public void addMoment(Double time) {
        try {
            this.getModel().setTime(this.getModel().getTime() + time);
            getView().successAdd();
        }
        catch (InvalidFormulaException e) {
            getView().error("Time add failed: " + e.getMessage());
        }
    }
    /**
     * Adds the specified time to the current moment in the simulation
     * @param days the time to add
     * @param hours the hours to add
     */
    @Endpoint(regex="SIMULATION MOMENT ADD DAYS=(\\d+) HOURS=(\\d+\\.?\\d*)")
    public void addMomentSpecified(Integer days,Double hours) {
        try {
            this.getModel().setTime(this.getModel().getTime() + days*24+hours);
            getView().successAdd();
        }
        catch (InvalidFormulaException e) {
            getView().error("Time add failed: " + e.getMessage());
        }
    }

    /**
     * Gets the view of the controller in the correct type
     * @return the view in the correct type
     */
    @Override
    public SimulationView getView() {
        return (SimulationView)super.getView();
    }
}
