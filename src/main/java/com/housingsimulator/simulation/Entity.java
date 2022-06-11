package com.housingsimulator.simulation;

/**
 * Defines a simulation entity, able to be simulated inside a Simulator
 */
public interface Entity
{
    /**
     * Returns a clone of itself
     * @return  A clone of itself
     */
    Entity clone();

    /**
     * Advances the entity in time. By default, does nothing
     * @param time  The amount of time to advance the entity by
     */
    default void advanceBy(double time) { }
}