package com.housingsimulator.controller.event;

import com.housingsimulator.model.EnergySupplier;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.Set;

public class AddSupplierEvent extends Event {
    private EnergySupplier supplier;


    public AddSupplierEvent() { }

    public AddSupplierEvent(double time, EnergySupplier supplier) {
        super(time);
        this.supplier = supplier;
    }

    public AddSupplierEvent(AddSupplierEvent e) {
        super(e);
        this.supplier = e.supplier;
    }


    @Override
    public Event clone() {
        return new AddSupplierEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {
        return Collections.singleton(this.supplier.getId());
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        EnergySupplier s = this.supplier.clone();
        s.advanceBy(time - this.getTime());
        return s;
    }
}
