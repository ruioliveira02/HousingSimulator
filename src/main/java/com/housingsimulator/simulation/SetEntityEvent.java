package com.housingsimulator.simulation;

import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.serialization.DeserializationException;
import com.housingsimulator.serialization.Deserializer;
import com.housingsimulator.serialization.Serializer;

import java.util.*;

/**
 * Event that sets the specified entity index to the given entity
 */
public class SetEntityEvent extends Event
{
    private Entity entity; /*! The entity to set */
    private int index;  /*! The index to set it in */

    /**
     * Empty constructor
     */
    public SetEntityEvent() { }

    /**
     * Creates an event that, at the specified time, sets the entity with the specified index to the given entity
     *
     * @param index The index to set
     * @param time  The time at which the event takes place
     * @param e     The entity to set it as
     */
    public SetEntityEvent(int index, double time, Entity e) {
        super(time);
        this.entity = e == null ? null : e.clone();
        this.index = index;
    }

    /**
     * Copy constructor
     *
     * @param e The event to copy
     */
    public SetEntityEvent(SetEntityEvent e) {
        super(e);
        this.index = e.index;
        this.entity = e.entity;
    }

    /**
     * Returns an exact clone of the object
     *
     * @return  An exact clone of the object
     */
    public SetEntityEvent clone() {
        return new SetEntityEvent(this);
    }

    /**
     * Initializes the event. For SetEntityEvent, this means doing nothing
     *
     * @param state The state of the simulation
     * @return  A set containing the target entity index
     */
    public Set<Integer> init(Simulator.State state) {
        return Collections.singleton(index);
    }

    /**
     * Returns the entity received, advanced to match the specified time
     *
     * @param entity_index  The index of the entity. Guaranteed to be the received entity
     * @param time          The specified time
     * @return              The advanced entity
     */
    public Entity getEntity(int entity_index, double time)
    {
        if (this.entity == null)
            return null;

        Entity ans = this.entity.clone();
        ans.advanceBy(time - this.getTime());
        return ans;
    }
}
