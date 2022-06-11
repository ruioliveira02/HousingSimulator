package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.controller.event.DeleteEntityEvent;
import com.housingsimulator.model.AbstractEntity;
import com.housingsimulator.model.EnergySupplier;
import com.housingsimulator.model.Model;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.EntityView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Controller for base entities
 */
@API
public class EntityController extends Controller {
    /**
     * Default constructor
     * @param model the model of the application
     */
    public EntityController(Model model) {
        this.setModel(model);
        this.setView(new EntityView());
    }

    /**
     * Gets all entities of the simulation
     */
    @Endpoint(regex="ENTITY LIST ALL")
    public void getAll() {
        Set<AbstractEntity> entities = this.getModel().getSimulation().getEntityByType(AbstractEntity.class);

        this.callViewOnCollection(entities);
    }

    /**
     * Gets all the entities with the given name
     * @param name the name
     */
    @Endpoint(regex="ENTITY GET NAME=(.+)")
    public void get(String name) {
        Set<AbstractEntity> entities = this.getModel().getSimulation().getEntitiesByName(name);

        this.callViewOnCollection(entities);
    }

    /**
     * Deletes the entity with the given id
     * @param id the id
     */
    @Endpoint(regex="ENTITY DELETE ID=(\\d+)")
    public void delete(Integer id) {
        Entity entity = this.getModel().getSimulation().getEntity(id);
        if(entity instanceof EnergySupplier) {
            this.getView().warning("Cannot delete suppliers");
        } else if(entity == null) {
            this.getView().error("No entity found with the gine id");
        }
        else{
            this.getModel().addSimulatorEvent(new DeleteEntityEvent(this.getModel().getTime(), id));
            this.getModel().updateState();
        }

    }

    /**
     * Calls the entity view on a collection of entities
     * @param entities the collection of entities
     */
    private void callViewOnCollection(Collection<AbstractEntity> entities) {
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();

        for(AbstractEntity e : entities) {
            ids.add(e.getId());
            names.add(e.getName());
            types.add(e.getClass().getSimpleName());
        }

        this.getView().showAll(ids, names, types);
    }

    /**
     * Returns the view associated with the controller in the correct type
     * @return the view of the controller
     */
    @Override
    public EntityView getView() {
        return (EntityView)super.getView();
    }
}
