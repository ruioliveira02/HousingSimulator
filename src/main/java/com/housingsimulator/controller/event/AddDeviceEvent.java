package com.housingsimulator.controller.event;

import com.housingsimulator.model.Simulation;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.*;

public class AddDeviceEvent extends Event {
    private SmartDevice device;
    private int houseId;
    private String room;

    @SerializeIgnore
    private SmartHouse house;


    public AddDeviceEvent() { }

    public AddDeviceEvent(double time, int houseId, String room, SmartDevice device) {
        super(time);
        this.houseId = houseId;
        this.room = room;
        this.device = device;
    }

    public AddDeviceEvent(AddDeviceEvent e) {
        super(e);
        this.houseId = e.houseId;
        this.room = e.room;
        this.device = e.device;
        this.house = e.house;
    }


    @Override
    public Event clone() {
        return new AddDeviceEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        this.house = (SmartHouse) state.getEntity(this.houseId);
        this.house.addDevice(device, room);
        return Collections.singleton(this.house.getId());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}
