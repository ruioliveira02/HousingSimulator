package com.housingsimulator.controller.event;

import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.*;

public class SwitchHouseEvent extends Event {

    private boolean switchOn;
    private int houseId;

    @SerializeIgnore
    private SmartHouse house;


    public SwitchHouseEvent() { }

    public SwitchHouseEvent(double time, int houseId, boolean switchOn) {
        super(time);
        this.houseId = houseId;
        this.switchOn = switchOn;
    }

    public SwitchHouseEvent(SwitchHouseEvent e) {
        super(e);
        this.houseId = e.houseId;
        this.switchOn = e.switchOn;
        this.house = e.house;
    }


    @Override
    public Event clone() {
        return new SwitchHouseEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        this.house = (SmartHouse)state.getEntity(this.houseId);
        this.house.switchAll(switchOn);
        return Collections.singleton(this.houseId);
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}

