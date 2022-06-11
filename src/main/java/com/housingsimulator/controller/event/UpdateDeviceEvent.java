package com.housingsimulator.controller.event;

import com.housingsimulator.model.Simulation;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UpdateDeviceEvent extends Event {
    private int deviceId;
    private SmartDevice device;

    @SerializeIgnore
    private SmartHouse house;


    public UpdateDeviceEvent() { }

    public UpdateDeviceEvent(double time, int id, SmartDevice device) {
        super(time);
        this.deviceId = id;
        this.device = device;
    }

    public UpdateDeviceEvent(UpdateDeviceEvent e) {
        super(e);
        this.deviceId = e.deviceId;
        this.device = e.device;
        this.house = e.house;
    }


    @Override
    public Event clone() {
        return new UpdateDeviceEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        Simulation sim = new Simulation(state);

        List<SmartHouse> houses = sim.getHouses().stream().filter(h->h.containsDevice(this.deviceId)).toList();

        if (houses.size() != 1)
            throw new IllegalStateException(String.format("Exaclty 1 house should contain device with id %d", this.deviceId));

        this.house = houses.get(0);
        house.updateDevice(deviceId, device);
        return Collections.singleton(this.house.getId());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}
