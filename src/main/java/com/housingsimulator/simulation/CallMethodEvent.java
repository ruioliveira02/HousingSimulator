package com.housingsimulator.simulation;

import com.housingsimulator.serialization.DeserializationException;
import com.housingsimulator.serialization.DeserializationResult;
import com.housingsimulator.serialization.Deserializer;
import com.housingsimulator.serialization.Serializer;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Event that calls the specified method of an entity through reflection
 */
public class CallMethodEvent extends Event
{
    private int entityIndex; /*! The index of the entity to act on */

    private String methodName; /*! The name of the method */
    private Method method; /*! The method to call */
    private Object[] params; /*! the parameters to pass to the method */

    private Entity entity; /*! The calculated entity */

    /**
     * Empty constructor
     */
    public CallMethodEvent() {
    }

    /**
     * Creates an event that calls the method with the specified name on the entity with the specified index, using the given parameters
     * The search for the method by its name is done every time the event is init'd. Slow.
     *
     * @param entity_index  The specified index
     * @param time          The time at which the event takes place
     * @param method        The name of the method to call
     * @param params        The parameters to call the method with. The elements must remain constant
     */
    //
    public CallMethodEvent(int entity_index, double time, String method, Object... params) {
        super(time);
        this.entityIndex = entity_index;
        this.methodName = method;
        this.params = params == null ? null : Arrays.copyOf(params, params.length); //TODO: copy members???
    }

    //

    /**
     * Creates an event that calls the method with the specified name on the entity with the specified index, using the given parameters
     * Searches for method only once in the constructor inside the given class. Faster.
     * If the target entity becomes null or of a different type, a runtime error will be thrown
     * If the target entity happens to implement the same method as entityClass (must be the same, not just a method
     * with the same name and signature), the method will be called
     *
     * @param entity_index  The specified index
     * @param time          The time at which the event takes place
     * @param entityClass   The class to find the method in
     * @param method        The name of the method
     * @param params        The parameters to call the method with
     * @throws NoSuchMethodException    If the method is not found in the specified class
     * @throws SecurityException        If the method is inaccessible
     */
    public CallMethodEvent(int entity_index, double time, Class<?> entityClass, String method, Object... params)
            throws NoSuchMethodException, SecurityException
    {
        super(time);
        this.entityIndex = entity_index;
        this.params = params == null ? null : Arrays.copyOf(params, params.length); //TODO: copy members???
        Class<?>[] paramTypes = this.params == null ? null : Arrays.stream(this.params).map(Object::getClass).toArray(Class[]::new);
        this.method = entityClass.getMethod(method, paramTypes);
    }

    /**
     * Creates an event that calls the specified method on the entity with the specified index, using the given parameters
     * If the target entity becomes null or of a different type, a runtime error will be thrown
     * If the target entity happens to implement the method (must be the same, not just a method
     * with the same name and signature), the method will be called
     *
     * @param entity_index  The specified index
     * @param time          The time at which the event takes place
     * @param method        The method
     * @param params        The parameters to call the method with
     */
    public CallMethodEvent(int entity_index, double time, Method method, Object... params)
    {
        super(time);
        this.entityIndex = entity_index;
        this.method = method; //TODO: checks?
        this.params = params == null ? null : Arrays.copyOf(params, params.length); //TODO: copy members???
    }

    /**
     * Copy constructor
     *
     * @param e The object to copy
     */
    public CallMethodEvent(CallMethodEvent e) {
        super(e);
        this.entityIndex = e.entityIndex;
        this.methodName = e.methodName;
        this.method = e.method;
        this.params = e.params;
        this.entity = e.entity;
    }

    /**
     * Returns an exact clone of the object
     *
     * @return  An exact clone of the object
     */
    public CallMethodEvent clone() {
        return new CallMethodEvent(this);
    }

    /**
     * Initializes the event. For CallMethodEvent, this means searching for the method if needed, taking a copy of
     * the targeted entity and calling the method. The entity is then stored to return in getEntity
     *
     * @param state The state of the simulation
     * @return  A set containing the targeted entity's index
     */
    public Set<Integer> init(Simulator.State state)
    {
        this.entity = state.getEntity(this.entityIndex);

        try {
            Method m = this.method;

            if (this.method == null) {
                Class<?>[] paramTypes = Arrays.stream(this.params).map(Object::getClass).toArray(Class[]::new);
                m = this.entity.getClass().getMethod(this.methodName, paramTypes);
            }

            m.invoke(this.entity, this.params);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Unable to find method %s for entity %d", this.methodName, this.entityIndex));
        } catch (SecurityException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Method %s for entity %d is inaccessible", this.method == null ? this.methodName : this.method.getName(), this.entityIndex));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(String.format("Unable to call method %s of entity %d", this.method.getName(), this.entityIndex));
        }

        return Collections.singleton(entityIndex);
    }

    /**
     * Returns the entity calculated in init, advanced to match the specified time
     *
     * @param entity_index  The index of the entity. Guaranteed to be the targeted entity
     * @param time          The specified time
     * @return              The advanced entity
     */
    public Entity getEntity(int entity_index, double time)
    {
        Entity ans = entity.clone();
        ans.advanceBy(time - this.getTime());
        return ans;
    }


    /**
     * Serializes the contents of the event. The calculated entity is not stored, as the simulation initializes all events after deserialization
     *
     * @param s The serializer
     * @return  A text representation of the event
     */
    @Override
    public CharSequence serializeData(Serializer s) {
        //TODO: (de)serialize Class<T> if constructed with explicit constructor
        Map<String,Object> fields = new TreeMap<>();
        fields.put("entityIndex", entityIndex);
        fields.put("time", getTime());
        fields.put("methodName", this.method == null ? this.methodName : this.method.getName());
        fields.put("params", Arrays.asList(this.params));
        return s.serializeMap(fields);
    }

    /**
     * Deserializes the event, effectively reversing the operation at serializeData
     * The calculated entity is not recovered
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
        this.entityIndex = (int)fields.get("entityIndex");
        this.setTime((double)fields.get("time"));
        this.methodName = (String)fields.get("methodName");
        this.params = ((Collection<?>)fields.get("params")).toArray();
        return ans.consumedLength();
    }
}