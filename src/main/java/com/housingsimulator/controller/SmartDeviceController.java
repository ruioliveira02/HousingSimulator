package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.controller.event.*;
import com.housingsimulator.model.Model;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.SmartDeviceView;

import java.util.*;

/**
 * Controller for smart devices
 */
@API
public class SmartDeviceController extends Controller {
    /**
     * Default constructor
     * @param model application model
     */
    public SmartDeviceController(Model model) {
        this.setModel(model);
        this.setView(new SmartDeviceView());
    }

    /**
     * Gets all the devices in the simulation
     */
    @Endpoint(regex="DEVICE LIST ALL")
    public void getDevices() {
        Map<List<String>, Set<SmartDevice>> devices = this.getModel().getSimulation().getDevicesByRoom();

        this.callViewOnCollection(devices);
    }


    /**
     * Turns on/off all devices in the simulation
     * @param on whether to turn the devices on or off
     */
    @Endpoint(regex="DEVICE TURN ALL ON=(TRUE|FALSE)")
    public void turnAllDevices(Boolean on) {
        this.getModel().addSimulatorEvent(new SwitchAllEvent(this.getModel().getTime(), on));
        this.getModel().updateState();
    }

    /**
     * Turns all devices in a room on/off
     * @param houseId the house
     * @param room the room
     * @param on whether to turn the devices on or off
     */
    @Endpoint(regex="DEVICE TURN HOUSE=(\\d+) ROOM=(.+) ON=(TRUE|FALSE)")
    public void turnAllDevices(Integer houseId, String room, Boolean on) {
        Entity ent = this.getModel().getSimulation().getEntity(houseId);
        if (ent instanceof SmartHouse && ((SmartHouse) ent).getRooms().containsKey(room)) {
            this.getModel().addSimulatorEvent(new SwitchRoomEvent(this.getModel().getTime(), houseId, room, on));
            this.getModel().updateState();
        }
        else{
            this.getView().warning("a house with the given id doesn't exist or the room is not a part of the house");
        }
    }

    /**
     * Turns all the devices of the given house on/off
     * @param houseId the house id
     * @param on whether to turn the devices on or off
     */
    @Endpoint(regex="DEVICE TURN HOUSE=(\\d+) ON=(TRUE|FALSE)")
    public void turnAllDevices(Integer houseId, Boolean on) {
        Entity ent = this.getModel().getSimulation().getEntity(houseId);
        if (ent instanceof SmartHouse) {
            this.getModel().addSimulatorEvent(new SwitchHouseEvent(this.getModel().getTime(), houseId, on));
            this.getModel().updateState();
        }
        else{
            this.getView().warning("a house with the given id doesn't exist");
        }
    }

    /**
     * Turns on/off the device with the given id
     * @param deviceId the id of the device
     * @param on whether to turn the device on/offd
     */
    @Endpoint(regex="DEVICE TURN ID=(\\d+) ON=(TRUE|FALSE)")
    public void turnDevice(Integer deviceId, Boolean on) {
        Entity ent = this.getModel().getSimulation().getEntity(deviceId);
        if (ent instanceof SmartDevice) {
            this.getModel().addSimulatorEvent(new SwitchDeviceEvent(this.getModel().getTime(), deviceId, on));
            this.getModel().updateState();
        }
        else {
            this.getView().warning("no smartDevice with the given id exists");
        }
    }

    @Endpoint(regex="DEVICE MOVE ID=(\\d+) HOUSE=(\\d+) ROOM=(.+)")
    public void moveDevice(Integer deviceId, Integer houseId, String room) {
        Entity ent = this.getModel().getSimulation().getEntity(deviceId);
        if (ent instanceof SmartDevice) {
            if ( this.getModel().getSimulation().getEntity(houseId) instanceof SmartHouse) {
                this.getModel().addSimulatorEvent(new MoveDeviceEvent(this.getModel().getTime(), deviceId, houseId, room));
                this.getModel().updateState();
            }
            else {
                this.getView().warning("no house with the given id exists");
            }
        }else {
            this.getView().warning("no SmartDevice with the given id exists");
        }
    }


    /**
     * Calls the view on a collection of devices indexed by house / room
     * @param devices the devices
     */
    private void callViewOnCollection(Map<List<String>, Set<SmartDevice>> devices) {
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Boolean> on = new ArrayList<>();
        List<String> houses = new ArrayList<>();
        List<String> rooms = new ArrayList<>();

        for(Map.Entry<List<String>, Set<SmartDevice>> kp : devices.entrySet()) {
            for(SmartDevice d : kp.getValue()) {
                ids.add(d.getId());
                names.add(d.getName());
                on.add(d.getOn());
                houses.add(kp.getKey().get(0));
                rooms.add(kp.getKey().get(1));
            }
        }

        this.getView().showAll(ids, names, on, houses, rooms);
    }

    /**
     * Gets the used view in the correct type
     * @return the view
     */
    @Override
    public SmartDeviceView getView() {
        return (SmartDeviceView)super.getView();
    }
}