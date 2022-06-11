package com.housingsimulator.simulation;

import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.serialization.DeserializationException;
import com.housingsimulator.serialization.Deserializer;
import com.housingsimulator.serialization.Serializer;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Defines a simulation event
 */
public abstract class Event implements Comparable<Event>, AutoSerializable, Serializable
{
    private double time;    /*! The moment at which the event takes place */
    private double order; /*! Used to untie events that occur at the same time. the actual value doesn't matter,
                                it is only used to compare between events. If two events have the same time and
                                the same order, they are considered to be equal (i.e. one of them is ignored when
                                storing in a Set/Map) */

    /**
     * Returns the time at which the event takes place
     *
     * @return The time at which the event takes place
     */
    public final double getTime() {
        return time;
    }

    /**
     * Returns the order of the event
     * @return The order of the event
     */
    final double getOrder() {
        return order;
    }

    /**
     * Sets the time of the event. Should be used CAREFULLY, since events are not expected to move around once inside the timeline
     * @param time  The new time of the event
     */
    protected final void setTime(double time) {
        this.time = time;
    }

    /**
     * Sets the order
     * @param order The new order
     */
    final void setOrder(double order) {
        this.order = order;
    }

    /**
     * Empty constructor
     */
    public Event() { }

    /**
     * Sets the event's time to the specified value
     * @param time  The specified value
     */
    protected Event(double time) {
        this.time = time;
    }

    /**
     * Sets the event's time and order to the specified values
     * @param time  The specified value for time
     * @param order The specified value for order
     */
    Event(double time, double order) {
        this.time = time;
        this.order = order;
    }

    /**
     * Copy constructor
     *
     * @param e The object to copy
     */
    public Event(Event e) {
        this.time = e.time;
        this.order = e.order;
    }


    /**
     * Returns an exact clone of the object
     *
     * @return  An exact clone of the object
     */
    public abstract Event clone();

    /**
     * Compares two events, returning whether they happen at the same time and by the same order
     *
     * @param o The object to compare this event with
     * @return  Whether the two objects are "the same"
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event e = (Event) o;
        return this.time == e.time;
    }

    /**
     * Compares two events. An event is said to be "less than" another if it happens first,
     * i.e. has a smaller time, or the same time but a smaller order
     *
     * @param e The event to compare this event to
     * @return  0 if the two events happen at the exact same moment
     *          < 0 if this event happens first
     *          > 0 if the other event happens first
     */
    public final int compareTo(Event e) {
        int aux = Double.compare(this.time, e.time);
        return aux != 0 ? aux : Double.compare(this.order, e.order);
    }


    /**
     * Initializes the event. This may involve consulting the current state of the simulation
     * Returns a set of the indexes of entities affected by this event. Only indexes in this set
     * may be used in getEntity(). Entities whose index is not on this set will remain the same as
     * they were before the event
     *
     * @param state The current state of the simulation
     * @return      The set of affected indexes
     */
    public abstract Set<Integer> init(Simulator.State state);


    /**
     * Returns the affected entity, advanced to match the specified time
     *
     * @param entity_index  The queried index
     * @param time          The specified time
     * @return              The entity
     */
    public abstract Entity getEntity(int entity_index, double time);
}