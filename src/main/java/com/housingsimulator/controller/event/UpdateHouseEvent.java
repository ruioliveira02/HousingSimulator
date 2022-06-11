package com.housingsimulator.controller.event;

import com.housingsimulator.model.EnergySupplier;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.Set;

public class UpdateHouseEvent extends Event {
    private int houseId;
    private String name;
    private int nif;
    private String owner;
    private int energySupplierId;
    private boolean onlyUpdateSupplier;

    @SerializeIgnore
    private SmartHouse house;


    public UpdateHouseEvent() { }

    public UpdateHouseEvent(double time, int id, int energySupplierId) {
        super(time);
        this.houseId = id;
        this.energySupplierId = energySupplierId;
        this.onlyUpdateSupplier = true;
    }

    public UpdateHouseEvent(double time, int id, String name, int nif, String owner, int energySupplierId) {
        super(time);
        this.houseId = id;
        this.name = name;
        this.nif = nif;
        this.owner = owner;
        this.energySupplierId = energySupplierId;
    }

    public UpdateHouseEvent(UpdateHouseEvent e) {
        super(e);
        this.houseId = e.houseId;
        this.name = e.name;
        this.nif = e.nif;
        this.owner = e.owner;
        this.energySupplierId = e.energySupplierId;
        this.house = e.house;
        this.onlyUpdateSupplier = e.onlyUpdateSupplier;
    }


    @Override
    public Event clone() {
        return new UpdateHouseEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {

        this.house = (SmartHouse) state.getEntity(this.houseId);
        if (!onlyUpdateSupplier) {
            house.setName(name);
            house.setOwnerNif(nif);
            house.setOwnerName(owner);
        }

        house.setSupplierId(energySupplierId);
        return Collections.singleton(houseId);
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        SmartHouse h = this.house.clone();
        h.advanceBy(time - this.getTime());
        return h;
    }
}
