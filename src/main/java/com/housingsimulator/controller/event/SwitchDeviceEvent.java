package com.housingsimulator.controller.event;

import com.housingsimulator.model.Simulation;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.*;

public class SwitchDeviceEvent extends Event {

    private boolean switchOn;
    private int deviceId;

    @SerializeIgnore
    private SmartHouse house;


    public SwitchDeviceEvent() { }

    public SwitchDeviceEvent(double time, int deviceId, boolean switchOn) {
        super(time);
        this.deviceId = deviceId;
        this.switchOn = switchOn;
    }

    public SwitchDeviceEvent(SwitchDeviceEvent e) {
        super(e);
        this.deviceId = e.deviceId;
        this.switchOn = e.switchOn;
        this.house = e.house;
    }


    @Override
    public Event clone() {
        return new SwitchDeviceEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        Simulation sim = new Simulation(state);
        List <SmartHouse> houses = sim.getHouses().stream().filter(h->h.containsDevice(deviceId)).toList();

        if (houses.size() != 1)
            throw new IllegalStateException(String.format("Expected exactly one device with id %d", this.deviceId));

        this.house = houses.get(0);
        this.house.switchById(deviceId, switchOn);
        return Collections.singleton(house.getId());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}