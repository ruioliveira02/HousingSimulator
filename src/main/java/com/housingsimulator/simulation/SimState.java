package com.housingsimulator.simulation;

import java.util.Set;

/**
 * Marks a class as a possible simulation state, in the sense that it can be divided into independent entities
 */
public interface SimState {
    /**
     * Gets a single entity. Can be a copy or the entity itself
     *
     * @param entityIndex   The index of the entity
     * @return              The entity
     */
    Entity getEntity(int entityIndex);

    /**
     * Gets a set of the non-null entities
     *
     * @return  A set containing all indexes that, when calling getEntity() on this SimState, return a non-null Entity
     */
    Set<Integer> getValidIds();
}