package com.housingsimulator.controller.event;

import com.housingsimulator.model.*;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.*;
import java.util.stream.Collectors;

public class DeleteEntityEvent extends Event {
    private int entityId;

    @SerializeIgnore
    private Entity target;


    public DeleteEntityEvent() { }

    public DeleteEntityEvent(double time, int entityId) {
        super(time);
        this.entityId = entityId;
    }

    public DeleteEntityEvent(DeleteEntityEvent e) {
        super(e);
        this.entityId = e.entityId;
        this.target = e.target;
    }


    @Override
    public Event clone() {
        return new DeleteEntityEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state)
    {
        Simulation sim = new Simulation(state);

        AbstractEntity e = sim.getEntity(this.entityId);
        int targetId;

        if (e instanceof SmartHouse || e instanceof SpeakerBrand) {
            targetId = e.getId();
            this.target = null;
        }
        else if (e instanceof SmartDevice) {
            List <SmartHouse> houses= sim.getHouses().stream().filter(h->h.containsDevice(entityId)).toList();

            if (houses.size() != 1)
                throw new IllegalStateException(String.format("Expected exactly one entity with id %d", entityId));

            SmartHouse h = houses.get(0);
            h.removeDevice((SmartDevice) e);
            targetId = h.getId();
            this.target = h;
        }
        else { //invalid deletion
            throw new IllegalStateException(String.format("Entity %d cannot be deleted", this.entityId));
        }

        return Collections.singleton(targetId);
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        if (this.target == null)
            return null;

        Entity e = this.target.clone();
        e.advanceBy(time - this.getTime());
        return e;
    }
}
