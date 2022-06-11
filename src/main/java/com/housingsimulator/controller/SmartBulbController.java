package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.controller.event.AddDeviceEvent;
import com.housingsimulator.controller.event.UpdateDeviceEvent;
import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.SmartBulbView;

import java.util.*;

/**
 * Controller for smart bulbs
 */
@API
public class SmartBulbController extends Controller {
    /**
     * Default constructor
     * @param model the model of the application
     */
    public SmartBulbController(Model model) {
        this.setModel(model);
        this.setView(new SmartBulbView());
    }

    /**
     * Gets the bulb with the given id
     * @param id the id
     */
    @Endpoint(regex="BULB GET ID=(\\d+)")
    public void getBulb(Integer id) {
        try {
            SmartBulb bulb = this.getModel().getSimulation().getDevice(SmartBulb.class, id);
            this.getView().show(bulb.getId(), bulb.getName(), bulb.getOn(), bulb.getColour().toString(),
                    bulb.getDimension());

        }catch (NoSuchEntityException | WrongEntityTypeException e){
            this.getView().warning(e.getMessage());
        }
    }


    /**
     * Gets the bulb within the given house
     * @param id the id
     */
    @Endpoint(regex="BULB GET HOUSE=(\\d+)")
    public void getBulbInHouse(Integer id) {
        try {
            Set<SmartBulb> bulbs = this.getModel().getSimulation().getDevicesInHouse(SmartBulb.class,id);
            this.callViewOnCollection(bulbs);
        }catch (NoSuchEntityException | WrongEntityTypeException e){
            this.getView().warning(e.getMessage());
        }
    }








    /**
     * Adds a bulb to the simulation
     * @param houseId the id of the house
     * @param room the room of the house
     * @param name the name of the bulb
     * @param on whether the bulb is on
     * @param colour the colour of the bulb
     * @param dimension the dimension of the bulb
     */
    @Endpoint(regex="BULB ADD HOUSE=(\\d+) ROOM=(.+) NAME=(.+) ON=(TRUE|FALSE) COLOUR=(WARM|NEUTRAL|COLD) DIMENSION=(\\d+\\.?\\d*)")
    public void addBulb(Integer houseId, String room, String name, Boolean on, String colour, Float dimension) {
        Entity e = this.getModel().getSimulation().getEntity(houseId);

        if (!(e instanceof SmartHouse)){
           this.getView().warning("no such house with id: "+houseId);
        }
        else{
            SmartBulb bulb = new SmartBulb(on, name, dimension, this.colourFromString(colour));
            this.getModel().addSimulatorEvent(new AddDeviceEvent(this.getModel().getTime(), houseId, room, bulb));
            this.getModel().updateState();
        }
    }

    /**
     * Updates a bulb
     * @param id the id of the bulb to change
     * @param name the name of the bulb
     * @param on whether the bulb is on
     * @param colour the colour of the bulb
     * @param dimension the dimension of the bulb
     */
    @Endpoint(regex="BULB UPDATE ID=(\\d+) NAME=(.+) ON=(TRUE|FALSE) COLOUR=(WARM|NEUTRAL|COLD) DIMENSION=(\\d+\\.?\\d*)")
    public void updateBulb(Integer id, String name, Boolean on, String colour, Float dimension) {
        Entity e = this.getModel().getSimulation().getEntity(id);

        if (!(e instanceof SmartBulb)){
            this.getView().warning("no such bulb with id: "+id);
        }
        else {
            SmartBulb bulb = new SmartBulb(on, name, dimension, this.colourFromString(colour));

            this.getModel().addSimulatorEvent(new UpdateDeviceEvent(this.getModel().getTime(), id, bulb));
            this.getModel().updateState();
        }
    }







    /**
     * Gets the colour associated with a string
     * @param name the name of the colour
     * @return the colour
     */
    private SmartBulb.LampColour colourFromString(String name) {
        return switch(name) {
            case "WARM" -> SmartBulb.LampColour.Warm;
            case "NEUTRAL" -> SmartBulb.LampColour.Neutral;
            case "COLD" -> SmartBulb.LampColour.Cold;
            default -> SmartBulb.LampColour.Warm;
        };
    }

    /**
     * Auxiliary method for calling the view on a collection of bulbs
     * @param bulbs the bulbs
     */
    private void callViewOnCollection(Collection<SmartBulb> bulbs) {
        List<Integer> ids = bulbs.stream().map(SmartBulb::getId).toList();
        List<String> names = bulbs.stream().map(SmartBulb::getName).toList();
        List<Boolean> ons = bulbs.stream().map(SmartBulb::getOn).toList();
        List<String> colours = bulbs.stream().map(b -> b.getColour().toString()).toList();
        List<Float> dimensions = bulbs.stream().map(SmartBulb::getDimension).toList();

        this.getView().showAll(bulbs.size(), ids, names, ons, colours, dimensions);
    }


    /**
     * Gets the bulb view in the correct type
     * @return the bulb view in the correct type
     */
    @Override
    public SmartBulbView getView() {
        return (SmartBulbView)super.getView();
    }
}
