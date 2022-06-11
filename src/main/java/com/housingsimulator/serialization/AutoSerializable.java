package com.housingsimulator.serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Marks types that can convert themselves to a text format and back.
 * Note, not all information about them must remain the same. What parts of the object are stored
 * is completely up to the implementation, as long as the deserialization is in accordance
 *
 * Classes that implement AutoSerializable are expected to define an empty constructor for reflection purposes.
 * The method deserializeData is then called on the constructed object
 */
public interface AutoSerializable
{
    //implementations are expected to define a public empty constructor

    /**
     * Returns a list of all instance fields of given class not marked with @SerializeIgnore
     *
     * @param clazz The given class
     * @return      The list of fields
     */
    private static List<Field> getAllFields(Class<?> clazz)
    {
        List<Field> fields = new ArrayList<>();

        for (Class<?> i = clazz; i != Object.class; i = i.getSuperclass())
            fields.addAll(Arrays.stream(i.getDeclaredFields())
                    .filter(f -> !Modifier.isStatic(f.getModifiers()))
                    .filter(f -> f.getAnnotation(SerializeIgnore.class) == null)
                    .toList());

        return fields;
    }

    /**
     * Retrieves the field with the given name from the specified class
     *
     * @param clazz     The specified class
     * @param fieldName The given name
     * @return          The field
     */
    private static Field findField(Class<?> clazz, String fieldName)
    {
        for (Class<?> i = clazz; i != Object.class; i = i.getSuperclass()) {
            try {
                return i.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException ignored) { }
        }

        return null;
    }

    /**
     * Casts a primitive to the desired type, if possible.
     * Possible casts: Long to short or int and Double to float
     *
     * @param o     The primitive
     * @param type  The desired type
     * @return      The cast object
     * @throws DeserializationException If the cast is not possible
     */
    private static Object castPrimitive(Object o, Class<?> type)
            throws DeserializationException
    {
        if (type == boolean.class)
            return o;
        else if (type == short.class)
            return (short)(long)o;
        else if (type == int.class)
            return (int)(long)o;
        else if (type == long.class)
            return o;
        else if (type == float.class)
            return (float)(double)o;
        else if (type == double.class)
            return o;
        else
            throw new DeserializationException("Unknown cast", 0, null);
        //TODO: char? e pôr isto bonito
    }

    /**
     * Serializes the contents of the AutoSerializable.
     * The default implementation uses reflection to store all instance fields not marked with @SerializeIgnore
     * by assuming all such fields are primitives or AutoSerializable
     *
     * @param s The serializer
     * @return  The text representation of the AutoSerializable
     */
    default CharSequence serializeData(Serializer s)
    {
        Map<String,Object> fields = new HashMap<>();

        try {
            for (Field f : getAllFields(this.getClass())) {
                f.setAccessible(true);
                fields.put(/*f.getDeclaringClass().getName() + "::" + */f.getName(), f.get(this));
            }
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("This should never happen. EVER"); //but just in case...
        }

        return s.serializeMap(fields);
    }

    /**
     * Deserializes the object, effectively reversing the operation at serializeData
     * The default implementation uses reflection to set all stored fields to the serialized values
     *
     * @param d The deserializer
     * @param s The text
     * @return  The length of text containing information about the object
     * @throws DeserializationException If the deserialization process fails
     */
    default int deserializeData(Deserializer d, String s)
            throws DeserializationException
    {
        DeserializationResult<? extends Map<?,?>> ans = d.deserializeMap(s);
        Map<?,?> fields = ans.ans();

        try {
            for (Map.Entry<?, ?> p : fields.entrySet())
            {
                String fieldName = (String)p.getKey();
                Class<?> clazz = this.getClass();//Class.forName(key.split("::")[0]);
                Field f = findField(clazz, fieldName);

                if (f == null)
                    throw new DeserializationException(String.format("Default deserialize mechanism failed: field %s::%s not found", this.getClass().getName(), fieldName), 0, s);

                f.setAccessible(true);

                if (f.getType().isArray())
                {
                    if (f.getType().getComponentType().isPrimitive()) {
                        Collection<?> col = (Collection<?>)p.getValue();
                        Object aux = Array.newInstance(f.getType().getComponentType(), col.size());
                        int i = 0;
                        for (Object o : col)
                            Array.set(aux, i++, castPrimitive(o, f.getType().getComponentType()));
                        f.set(this, aux);
                    }
                    else
                        f.set(this, ((Collection<?>)p.getValue()).toArray());
                }
                else if (f.getType().isPrimitive())
                    f.set(this, castPrimitive(p.getValue(), f.getType()));
                else
                    f.set(this, p.getValue());
            }

            return ans.consumedLength();
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("This should never happen. EVER"); //but just in case...
        }
    }
}
