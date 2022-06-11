package com.housingsimulator.simulation;

/**
 * Defines a simulation entity that doesn't change by itself (i.e. advanceBy is empty) through the simulation
 * by wrapping a cloneable object.
 *
 * @param <T>   The underlying type of the entity. Must define clone
 */
public class ConstEntity<T extends Cloneable<? extends T>> implements Entity
{
    private final T obj; /*! The underlying object */

    /**
     * Creates a ConstEntity from an underlying object
     *
     * @param obj   The underlying object
     */
    public ConstEntity(T obj) {
        this.obj = obj.clone();
    }

    /**
     * Returns a copy of the underlying object
     *
     * @return  A copy of the underlying object
     */
    public T getObj() {
        return this.obj.clone();
    }

    /**
     * Returns a copy of the entity
     *
     * @return A copy of the entity
     */
    public ConstEntity<T> clone() {
        return new ConstEntity<>(this.obj);
    }
}