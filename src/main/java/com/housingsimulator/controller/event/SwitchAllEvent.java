package com.housingsimulator.controller.event;

import com.housingsimulator.model.AbstractEntity;
import com.housingsimulator.model.Simulation;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SwitchAllEvent extends Event {

    private boolean switchOn;
    @SerializeIgnore
    private Simulation state;


    public SwitchAllEvent() { }

    public SwitchAllEvent(double time, boolean switchOn) {
        super(time);
        this.switchOn = switchOn;
    }

    public SwitchAllEvent(SwitchAllEvent e) {
        super(e);
        this.switchOn = e.switchOn;
        this.state = e.state;
    }


    @Override
    public Event clone() {
        return new SwitchAllEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        this.state = new Simulation(state);

        Map<List<String>, Set<SmartDevice>> devicesPerHouse = this.state.getDevicesByRoom();
        Set<SmartDevice> devices = new HashSet<>();

        for (Set<SmartDevice> devicesInRoom:devicesPerHouse.values())
            devices.addAll(devicesInRoom);

        devices.parallelStream().forEach(d -> d.setOn(this.switchOn));
        for(SmartDevice d : devices)
                this.state.updateDevice(d.getId(), d);

        return this.state.getHouses().stream().map(AbstractEntity::getId).collect(Collectors.toSet());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        Entity e = this.state.getEntity(entity_index).clone();
        e.advanceBy(time - this.getTime());
        return e;
    }
}
