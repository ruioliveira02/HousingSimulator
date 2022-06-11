package com.housingsimulator.simulation;

//im not sure if we should do this, or if we just leave it to the user to create a wrapper class to stuff into a ConstEntity

/**
 * Defines a simulation entity that doesn't change by itself (i.e. advanceBy is empty) through the simulation
 * by wrapping an immutable object. This differs from ConstEntity since immutable objects CAN'T BE CLONED. Since
 * there is no standard Java way to check, any type is accepted by ImmutableEntity. However, only immutable types
 * (types that cannot be altered - such as String, Integer, etc - therefore don't need to be cloned) should be used
 *
 * @param <T>   The type of the underlying object
 */
public class ImmutableEntity<T> implements Entity
{
    private final T obj; /*! The underlying object */

    /**
     * Creates an ImmutableEntity with the specified underlying object
     *
     * @param obj   The underlying object
     */
    public ImmutableEntity(T obj) {
        this.obj = obj;
    }

    /**
     * Returns the underlying object
     *
     * @return The underlying object
     */
    public T getObj() {
        return this.obj;
    }

    /**
     * Returns a copy of the entity
     *
     * @return  A copy of the entity
     */
    public ImmutableEntity<T> clone() {
        return new ImmutableEntity<>(this.obj);
    }
}
