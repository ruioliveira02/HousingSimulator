package com.housingsimulator.controller.event;

import com.housingsimulator.model.SmartDevice;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.model.SpeakerBrand;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddBrandEvent extends Event {
    private SpeakerBrand brand;

    public AddBrandEvent() { }

    public AddBrandEvent(double time, SpeakerBrand brand) {
        super(time);
        this.brand = brand;
    }

    public AddBrandEvent(AddBrandEvent e) {
        super(e);
        this.brand = e.brand;
    }


    @Override
    public Event clone() {
        return new AddBrandEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        return Collections.singleton(this.brand.getId());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SpeakerBrand b = this.brand.clone();
        b.advanceBy(time - this.getTime());
        return b;
    }
}
