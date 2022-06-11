package com.housingsimulator.simulation;

import com.housingsimulator.serialization.*;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Serves as a concrete implementation of the abstract class Event to use in searches in Sets and Maps
 */
class DummyEvent extends Event  //Used for searches in the indexed structures (tree, map) that compare Events
{
    /**
     * Creates a DummyEvent with specified time and order
     * @param time  The specified time
     * @param order The specified order
     */
    public DummyEvent(double time, double order) {
        super(time, order);
    }

    /**
     * Returns an exact clone of the object
     *
     * @return  An exact clone of the object
     */
    @Override
    public Event clone() {
        return new DummyEvent(getTime(), getOrder());
    }

    /**
     * Since DummyEvent won't ever enter the simulation, no functionality is required from this method
     *
     * @param state The current state of the simulation
     * @return      null
     */
    @Override
    public Set<Integer> init(Simulator.State state) {
        return null;
    }

    /**
     * Since DummyEvent won't ever enter the simulation, no functionality is required from this method
     *
     * @param entity_index  The queried index
     * @param time          The specified time
     * @return              null
     */
    @Override
    public Entity getEntity(int entity_index, double time) {
        return null;
    }
}

/**
 * Implements the behaviour of the simulation
 */
public class Simulator implements AutoSerializable, Serializable
{
    private TreeMap<Event, Set<Integer>> eventSet;      /*! The set of all events, each associated to the indexes they affect */
    private Map<Integer,TreeSet<Event>> entityEvents;   /*! A map that corresponds each entity index to the events it is affected by */

    /**
     * Defines the state of the simulation in a certain time
     */
    public class State implements SimState
    {
        //TODO: fail safe para alterações da simulacao
        private final double time;  /*! The time this state refers to */
        //TODO: add order?
        private Set<Integer> validIds; //*! Lazily calculated set of non-null entities */

        /**
         * Creates a State of the outer simulation at the specified time
         *
         * @param time  The specified time
         */
        private State(double time) {
            this.time = time;
        }

        /**
         * Retrieves the entity with the given index
         *
         * @param entity_index  The index of the entity
         * @return              The entity with the given index
         */
        public Entity getEntity(int entity_index)
        {
            TreeSet<Event> events = entityEvents.get(entity_index);
            Event event = events == null ? null : events.floor(new DummyEvent(this.time, Double.POSITIVE_INFINITY));
            //TODO: lazy
            return event == null ? null : event.getEntity(entity_index, this.time);
        }

        /**
         * Returns the validIds
         * If they haven't been calculated yet, calculates them first
         *
         * @return  The set of all indexes belonging to a non-null entity
         */
        public Set<Integer> getValidIds()
        {
            if (validIds == null)
                validIds = entityEvents.entrySet().stream()
                        .filter(p -> {
                            Event e = p.getValue().floor(new DummyEvent(this.time, Double.POSITIVE_INFINITY));
                            return e != null && e.getEntity(p.getKey(), time) != null; })
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

            return new TreeSet<>(validIds);
        }
    }

    /**
     * Creates an empty simulation, with all entities set to null from the infinite past to the infinite future
     */
    public Simulator() {
        this.eventSet = new TreeMap<>();
        this.entityEvents = new HashMap<>();
    }

    /**
     * Creates a simulation from an initial state at time 0. Before this time, all entities are null
     * @param initialState  The initial state (at time 0 and beyond)
     */
    public Simulator(SimState initialState) {
        this(initialState, 0);
    }

    /**
     * Creates a simulation from an initial state at the given time. Before this time, all entities are null
     * @param initialState  The initial state
     * @param initialTime   The given time
     */
    public Simulator(SimState initialState, double initialTime) {
        this.eventSet = new TreeMap<>();
        this.entityEvents = new HashMap<>();
        this.addEvent(new SetStateEvent(initialState, initialTime));
    }


    /**
     * Get the state of the simulation at the specified time
     * @param time  The specified time
     * @return      The state of the simulation
     */
    public State getState(double time) {
        return new State(time);
    }

    /**
     * Adds an event to the simulation.
     * If the event already exists, another is added (order comparison not supported yet)
     * @param event The event to add
     */
    public void addEvent(Event event)
    {
        //TODO: explicit control of the order of events at the same time

        Event e = event.clone();
        //always put added event after other events at the same time
        Event previous = eventSet.floorKey(new DummyEvent(e.getTime(), Double.POSITIVE_INFINITY));
        e.setOrder((previous == null || previous.getTime() < e.getTime()) ? 0 : previous.getOrder() + 1);

        eventSet.put(e, null);
        entityEvents.values().forEach(v -> v.tailSet(e, true).clear());

        for (Map.Entry<Event,Set<Integer>> entry = eventSet.ceilingEntry(e); entry != null; entry = eventSet.higherEntry(entry.getKey()))
        {
            //entry.setValue(entry.getKey().init(new State(entry.getKey().getTime())));
            Set<Integer> newVal = entry.getKey().init(new State(entry.getKey().getTime()));
            //TODO: check if newVal is null
            eventSet.put(entry.getKey(), newVal);

            for (int id : newVal)
                entityEvents.computeIfAbsent(id, k -> new TreeSet<>()).add(entry.getKey());
        }
    }

    //relies on .equals

    /**
     * Removes the event that are equal to the given event, if any
     * Note that equality test will include order
     *
     * @param event The event test against
     * @return      Whether any events were removed
     */
    public boolean removeEvent(Event event) {
        boolean removed = false;

        for (Map.Entry<Event, Set<Integer>> p = eventSet.firstEntry(); p != null; p = eventSet.higherEntry(p.getKey()))
        {
            Event e = p.getKey();

            if (removed)
            {
                //p.setValue(e.init(new State(e.getTime())));
                Set<Integer> newVal = e.init(new State(e.getTime()));
                eventSet.put(e, newVal);

                for (int id : newVal)
                    entityEvents.computeIfAbsent(id, k -> new TreeSet<>()).add(e);
            }
            else if (e.equals(event))
            {
                eventSet.remove(e);
                entityEvents.values().forEach(v -> v.tailSet(e, true).clear());
                removed = true;
            }
        }

        return removed;
    }


    /**
     * Removes all events that test true for the given condition
     * If an event is removed, the following events are initialized before being tested
     *
     * @param condition The given condition
     * @return          The number of events removed
     */
    public int removeEvents(Predicate<? super Event> condition) {
        int removed = 0;

        for (Map.Entry<Event, Set<Integer>> p = eventSet.firstEntry(); p != null; p = eventSet.higherEntry(p.getKey()))
        {
            Event e = p.getKey();

            if (removed > 0)
            {
                //p.setValue(e.init(new State(e.getTime())));
                Set<Integer> newVal = e.init(new State(e.getTime()));
                eventSet.put(e, newVal);

                for (int id : newVal)
                    entityEvents.computeIfAbsent(id, k -> new TreeSet<>()).add(e);
            }

            if (condition.test(e))
            {
                eventSet.remove(e);

                if (removed == 0) {
                    entityEvents.values().forEach(v -> v.tailSet(e, true).clear());
                    removed++;
                }
            }
        }

        return removed;
    }

    /**
     * Removes all events from the simulation, effectively clearing it.
     * All entities become null from the infinite past to the infinite future
     */
    public void removeAllEvents() {
        this.eventSet.clear();
        this.entityEvents.clear();
    }


    /**
     * Creates an object of the specified type from the state of the simulation at the given time,
     * mapping the entities of the simulation to the fields of the class annotated with @Simulation.SimEntity using reflection
     *
     * @param clazz The type of the created object
     * @param time  The given time
     * @return      The created object
     * @param <T>   The type of the created object
     * @throws ReflectiveOperationException If any reflective operation fails
     */
    public <T extends SimState> T getStateAs(Class<T> clazz, double time) throws ReflectiveOperationException
    {
        T ans = clazz.getConstructor().newInstance();
        State s = new State(time);

        for (Field f : clazz.getFields())
        {
            SimEntity e = f.getAnnotation(SimEntity.class);

            if (e != null)
                f.set(ans, f.getType().cast(s.getEntity(e.index())));
        }

        for (Method m : clazz.getMethods())
        {
            SimEntity e = m.getAnnotation(SimEntity.class);

            if (e != null)
                m.invoke(ans, m.getParameterTypes()[0].cast(s.getEntity(e.index())));
        }

        return ans;
    }

    /**
     * Serializes the contents of the simulator. Only the events are serialized.
     *
     * @param s The serializer
     * @return  A text representation of the simulator
     */
    @Override
    public CharSequence serializeData(Serializer s) {
        return s.serializeCollection(eventSet.keySet());
    }

    /**
     * Deserializes the simulator, effectively reversing the operation at serializeData
     * The events are re-initialized
     *
     * @param d The deserializer
     * @param s The text
     * @return  The length of text containing information about the simulator
     * @throws DeserializationException If the deserialization process fails
     */
    @Override
    public int deserializeData(Deserializer d, String s) throws DeserializationException {
        DeserializationResult<? extends Collection<?>> ans = d.deserializeCollection(s);
        this.eventSet = new TreeMap<>();
        this.entityEvents = new HashMap<>();

        for (var e : ans.ans()) {
            Event event = (Event)e;
            Set<Integer> entitySet = event.init(new State(((Event) e).getTime()));
            eventSet.put(event, entitySet);

            for (int id : entitySet)
                entityEvents.computeIfAbsent(id, k -> new TreeSet<>()).add(event);
        }
        return ans.consumedLength();
    }
}