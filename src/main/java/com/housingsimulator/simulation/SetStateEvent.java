package com.housingsimulator.simulation;

import com.housingsimulator.serialization.*;

import java.util.*;

/**
 * Event that sets the entire state to the provided state
 */
public class SetStateEvent extends Event
{
    private HashMap<Integer,Entity> entities;   /*! The entities of the provided state */


    /**
     * Empty constructor
     */
    public SetStateEvent() { }

    /**
     * Creates an event that, at the specified time, sets the whole state of the simulation to the provided state
     * If the provided state is null, the simulation is cleared (all entities are set to null)
     *
     * @param state The provided state
     * @param time  The specified time
     */
    public SetStateEvent(SimState state, double time) {
        super(time);
        this.entities = new HashMap<>();
        if (state != null) {
            state.getValidIds().forEach(i -> {
                Entity e = state.getEntity(i);
                this.entities.put(i, e == null ? null : e.clone());
            });
        }
    }

    /**
     * Copy constructor
     *
     * @param e The object to copy
     */
    public SetStateEvent(SetStateEvent e) {
        super(e);
        this.entities = new HashMap<>(e.entities);
    }


    /**
     * Returns an exact clone of the object
     *
     * @return  An exact clone of the object
     */
    public SetStateEvent clone() {
        return new SetStateEvent(this);
    }

    /**
     * Initializes the event. For CallMethodEvent, this means doing nothing
     *
     * @param state The state of the simulation
     * @return  A set containing all valid indexes of the provided state
     */
    public Set<Integer> init(Simulator.State state) {
        return entities.keySet();
    }

    /**
     * Returns the requested entity from the provided state, advanced to match the specified time
     *
     * @param entity_index  The index of the entity
     * @param time          The specified time
     * @return              The advanced entity
     */
    public Entity getEntity(int entity_index, double time)
    {
        Entity ans = entities.get(entity_index).clone();
        ans.advanceBy(time - this.getTime());
        return ans;
    }

    /**
     * Serializes the contents of the event. Assumes all entities contained by the provided state are AutoSerializable
     *
     * @param s The serializer
     * @return  A text representation of the event
     */
    @Override
    public CharSequence serializeData(Serializer s) {
        Map<String,Object> fields = new TreeMap<>();
        fields.put("time", getTime());
        fields.put("entities", entities);
        return s.serializeMap(fields);
    }

    /**
     * Deserializes the event, effectively reversing the operation at serializeData
     *
     * @param d The deserializer
     * @param s The text
     * @return  The length of text containing information about the event
     * @throws DeserializationException If the deserialization process fails
     */
    @Override
    public int deserializeData(Deserializer d, String s) throws DeserializationException {
        DeserializationResult<? extends Map<?,?>> ans = d.deserializeMap(s);
        Map<?,?> fields = ans.ans();
        this.setTime((double)fields.get("time"));
        this.entities = new HashMap<>((Map<Integer, Entity>) fields.get("entities"));
        return ans.consumedLength();
    }
}
