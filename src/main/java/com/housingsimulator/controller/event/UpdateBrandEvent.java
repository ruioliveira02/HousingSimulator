package com.housingsimulator.controller.event;

import com.housingsimulator.model.SpeakerBrand;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.Set;

public class UpdateBrandEvent extends Event {
    private String oldName;
    private SpeakerBrand brand;

    public UpdateBrandEvent() { }

    public UpdateBrandEvent(double time, String oldName, SpeakerBrand brand) {
        super(time);
        this.oldName = oldName;
        this.brand = brand;
    }

    public UpdateBrandEvent(UpdateBrandEvent e) {
        super(e);
        this.oldName = e.oldName;
        this.brand = e.brand;
    }


    @Override
    public Event clone() {
        return new UpdateBrandEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {

        for (int id : state.getValidIds())
            if (state.getEntity(id) instanceof SpeakerBrand b && b.getName().equals(this.oldName))
                return Collections.singleton(id);

        throw new IllegalStateException("No brands with the name " + oldName + "were found");
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SpeakerBrand b = this.brand.clone();
        b.advanceBy(time - this.getTime());
        return b;
    }
}
