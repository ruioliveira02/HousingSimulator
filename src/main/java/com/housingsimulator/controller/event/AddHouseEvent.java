package com.housingsimulator.controller.event;

import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddHouseEvent extends Event {
    private SmartHouse house;


    public AddHouseEvent() { }

    public AddHouseEvent(double time, SmartHouse house) {
        super(time);
        this.house = house;
    }

    public AddHouseEvent(AddHouseEvent e) {
        super(e);
        this.house = e.house;
    }


    @Override
    public Event clone() {
        return new AddHouseEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        return Collections.singleton(this.house.getId());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}
