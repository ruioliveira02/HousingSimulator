package com.housingsimulator.controller.event;

import com.housingsimulator.model.EnergySupplier;
import com.housingsimulator.model.SmartHouse;
import com.housingsimulator.serialization.SerializeIgnore;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.simulation.Event;
import com.housingsimulator.simulation.Simulator;

import java.util.Collections;
import java.util.Set;

public class UpdateSupplierEvent extends Event {
    private int supplierId;
    private String name;
    private float baseValue;
    private float tax;
    private String formula;

    @SerializeIgnore
    private EnergySupplier supplier;


    public UpdateSupplierEvent() { }

    public UpdateSupplierEvent(double time, int id, String name, float baseValue, float tax, String formula) {
        super(time);
        this.supplierId = id;
        this.name = name;
        this.baseValue = baseValue;
        this.tax = tax;
        this.formula = formula;
    }

    public UpdateSupplierEvent(UpdateSupplierEvent e) {
        super(e);
        this.supplierId = e.supplierId;
        this.name = e.name;
        this.baseValue = e.baseValue;
        this.tax = e.tax;
        this.formula = e.formula;
    }


    @Override
    public Event clone() {
        return new UpdateSupplierEvent(this);
    }

    @Override
    public Set<Integer> init(Simulator.State state) {

        this.supplier = (EnergySupplier) state.getEntity(this.supplierId);
        supplier.setName(name);
        supplier.setBaseValue(baseValue);
        supplier.setTax(tax);
        supplier.setPriceFormula(formula);
        return Collections.singleton(supplierId);
    }

    @Override
    public Entity getEntity(int entity_index, double time) {
        EnergySupplier s = this.supplier.clone();
        s.advanceBy(time - this.getTime());
        return s;
    }
}
