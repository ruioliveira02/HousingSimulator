package com.housingsimulator.simulation;

/**
 * Signals that a class defines a clone method, effectively overriding Object::clone.
 *
 * @param <T> The return type of the clone method, typically the implementing class itself
 */
public interface Cloneable<T> {
    T clone();
}
