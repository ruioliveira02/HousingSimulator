package com.housingsimulator.controller.event;

import com.housingsimulator.model.AbstractEntity;
import com.housingsimulator.model.Simulation;
import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BillingEvent extends Event {
    @SerializeIgnore
    private Simulation state;


    public BillingEvent() { }

    public BillingEvent(double time) {
        super(time);
    }

    public BillingEvent(BillingEvent e) {
        super(e);
        this.state = e.state;
    }


    @Override
    public Event clone() {
        return new BillingEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        this.state = new Simulation(state);
        this.state.resetCounters();
        return this.state.getHouses().stream().map(AbstractEntity::getId).collect(Collectors.toSet());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        Entity e = this.state.getEntity(entity_index).clone();
        e.advanceBy(time - this.getTime());
        return e;
    }
}
