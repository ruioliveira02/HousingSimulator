package com.housingsimulator.controller;

import com.housingsimulator.controller.event.AddSupplierEvent;
import com.housingsimulator.controller.event.UpdateSupplierEvent;
import com.housingsimulator.exceptions.NoEnergySuppliersExistException;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.EnergySupplierView;
import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;


import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller for the energy suppliers
 */
@API
public class EnergySupplierController extends Controller {

    /**
     * Default constructor
     * @param model The model of the application
     */
    public EnergySupplierController(Model model) {
        this.setView(new EnergySupplierView());
        this.setModel(model);
    }

    /**
     * Returns the view associated with the controller in the correct type
     * @return the view of the controller
     */
    @Override
    public EnergySupplierView getView() {
        return (EnergySupplierView)super.getView();
    }

    /**
     * Lists all the suppliers in the application
     */
    @Endpoint(regex="SUPPLIER LIST ALL")
    public void listSuppliers() {
        Set<EnergySupplier> suppliers = this.getModel().getSimulation().getEntityByType(EnergySupplier.class);

        this.callViewOnCollection(suppliers);
    }

    /**
     * Gets the supplier(s) with the given name
     * @param name the name
     */
    @Endpoint(regex="SUPPLIER GET NAME=(.+)")
    public void getSuppliersByName(String name) {
        Set<EnergySupplier> suppliers = this.getModel().getSimulation().getEntitiesByName(name).stream()
                .filter(e -> e instanceof EnergySupplier).map(e -> (EnergySupplier)e).collect(Collectors.toSet());

        this.callViewOnCollection(suppliers);
    }

    /**
     * Gets the supplier with the given id.
     *
     * If there is no entity with the given id, an error is raised. If there is and it is of the wrong type,
     * a warning is raised
     *
     * @param id the id
     */
    @Endpoint(regex="SUPPLIER GET ID=(\\d+)")
    public void getSupplierById(Integer id) {
        Entity entity = this.getModel().getSimulation().getEntity(id);
        if(entity == null) {
            this.getView().error("No supplier found");
        } else if(entity instanceof EnergySupplier) {
            EnergySupplier supplier = (EnergySupplier) entity;
            this.getView().show(supplier.getId(), supplier.getName(), supplier.getBaseValue(), supplier.getTax(), supplier.getPriceFormula());
        } else {
            this.getView().warning("An entity of different type was found with the given id");
        }
    }

    /**
     * Adds a supplier to the simulation
     * @param name the name of the supplier
     * @param baseValue the base value of the supplier
     * @param tax the tax of the supplier
     * @param priceFormula the price formula of the supplier
     */
    @Endpoint(regex="SUPPLIER ADD NAME=(.+) BASE_VALUE=(\\d+\\.?\\d*) TAX=(\\d+\\.?\\d*) FORMULA=(.+)")
    public void addSupplier(String name, Float baseValue, Float tax, String priceFormula) {
        this.getModel().addSimulatorEvent(new AddSupplierEvent(this.getModel().getTime(), new EnergySupplier(name, baseValue, tax, priceFormula)));
        this.getModel().updateState();
    }


    /**
     * Updates a supplier
     * @param id the id of the supplier to change
     * @param name the name of the supplier
     * @param baseValue the base value of the supplier
     * @param tax the tax of the supplier
     * @param priceFormula the price formula of the supplier
     */
    @Endpoint(regex="SUPPLIER UPDATE ID=(\\d+) NAME=(.+) BASE_VALUE=(\\d+\\.?\\d*) TAX=(\\d+\\.?\\d*) FORMULA=(.+)")
    public void updateSupplier(Integer id, String name, Float baseValue, Float tax, String priceFormula) {
        Entity entity = this.getModel().getSimulation().getEntity(id);
        if(entity == null) {
            this.getView().error("No supplier found");
        } else if(entity instanceof EnergySupplier) {
            this.getModel().addSimulatorEvent(new UpdateSupplierEvent(this.getModel().getTime(), id, name, baseValue, tax, priceFormula));
            this.getModel().updateState();
        } else {
            this.getView().warning("An entity of different type was found with the given id");
        }
    }

    /**
     * gets the supplier who made the most money since the start
     */
    @Endpoint(regex = "SUPPLIER GET MOST SELLER")
    public void bestSellingSupplier(){
        try {
            EnergySupplier supplier = this.getModel().bestSellingSupplier();
            this.getView().show(supplier.getId(),supplier.getName(),supplier.getBaseValue(),supplier.getTax(),supplier.getPriceFormula());

        } catch (NoEnergySuppliersExistException e) {
            this.getView().warning(e.getMessage());
        }
    }


    /**
     * Makes the view display the given collection of suppliers
     *
     * @param suppliers the collection of suppliers
     */
    private void callViewOnCollection(Collection<EnergySupplier> suppliers) {
        int n = suppliers.size();

        List<Integer> ids = suppliers.stream().map(EnergySupplier::getId).toList();
        List<String> names = suppliers.stream().map(EnergySupplier::getName).toList();
        List<Double> baseValues = suppliers.stream().map(EnergySupplier::getBaseValue).toList();
        List<Double> taxes = suppliers.stream().map(EnergySupplier::getTax).toList();
        List<String> formulas = suppliers.stream().map(EnergySupplier::getPriceFormula).toList();

        this.getView().showAll(n, ids, names, baseValues, taxes, formulas);
    }
}
