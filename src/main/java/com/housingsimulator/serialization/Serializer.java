package com.housingsimulator.serialization;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Serializer
{
    private final IdentityHashMap<AutoSerializable,Integer> serializedObjects;  /*! AutoSerializable objects already serialized by the Serializer */
    private boolean indented;   /*! Whether the Serializer's output should be indented */

    private int tabDepth;   /*! Internal variable. Used to keep track of the depth of recursive serializations to print
                                the number of tabs accordingly (if indented is true) */

    /**
     * Creates a new Serializer, with no indentation
     */
    public Serializer() {
        this(false);
    }

    /**
     * Creates a new Serializer
     *
     * @param indented  Whether the output of the Serializer should be indented
     */
    public Serializer(boolean indented) {
        this.serializedObjects = new IdentityHashMap<>();
        this.indented = indented;
    }

    /**
     * @return Whether the output of the Serializer will be indented
     */
    public boolean getIndented() {
        return this.indented;
    }

    /**
     * Set whether the output of the Serializer should be indented
     *
     * @param indented  What I said above
     */
    public void setIndented(boolean indented) {
        this.indented = indented;
    }

    /**
     * Replaces a character with its escape sequence. If no such sequence exists, the character is returned
     * @param c The character
     * @return  The escaped version of the character
     */
    private String escapeChar(char c) {
        return switch (c) {
            case '\\' -> "\\\\";
            case '"' -> "\\\"";
            case '{' -> "\\{";
            case '}' -> "\\}";
            case '$' -> "\\$";
            default -> Character.toString(c);
        };
    }

    /**
     * Escapes the input text
     *
     * @param s The input text
     * @return  The escaped version
     */
    private String escape(CharSequence s) {
        return s.chars()
                .mapToObj(i -> escapeChar((char)i))
                .collect(Collectors.joining());
    }

    /**
     * Serializes an object
     *
     * @param o The object to serialize
     * @return  The serialized object
     */
    public String serializeObj(AutoSerializable o)
    {
        if (o == null)
            return "$null$";

        Integer id = serializedObjects.get(o);

        if (id == null) {
            int new_id = serializedObjects.size();
            serializedObjects.put(o, new_id);
            return String.format("%s$%d${%s}", escape(o.getClass().getName()), new_id, o.serializeData(this));
        }

        return String.format("$%d$", id);
    }

    /**
     * Serializes a string
     *
     * @param s The string to serialize
     * @return  The serialized string
     */
    public String serializeString(String s) {
        return s == null ? "$null$" : String.format("\"%s\"", escape(s));
    }

    /**
     * Serializes a long
     *
     * @param l The long to serialize
     * @return  The serialized long
     */
    public String serializeLong(Long l) {
        return l == null ? "$null$" : Long.toString(l);
    }

    /**
     * Serializes a double
     *
     * @param d The double to serialize
     * @return  The serialized double
     */
    public String serializeDouble(Double d) {
        return d == null ? "$null$" : Double.toString(d);
    }

    /**
     * Serializes a boolean
     *
     * @param b The boolean to serialize
     * @return  The serialized boolean
     */
    public String serializeBool(Boolean b) {
        return b == null ? "$null$" : (b ? "true" : "false");
    }

    /**
     * Serializes objects of any valid type.
     * Valid types: AutoSerializable, string, long, double, boolean, Collection and Map
     *
     * @param o The object to serialize
     * @return  The serialized object
     * @throws IllegalArgumentException If the object is not of a valid type
     */
    private String serializeGenericObj(Object o)
            throws IllegalArgumentException
    {
        if (o instanceof AutoSerializable) return serializeObj((AutoSerializable) o);
        else if (o instanceof String) return serializeString((String) o);
        else if (o instanceof Integer) return serializeLong(((Integer)o).longValue());
        else if (o instanceof Long) return serializeLong((Long)o);
        else if (o instanceof Float) return serializeDouble(((Float)o).doubleValue());
        else if (o instanceof Double) return serializeDouble((Double) o);
        else if (o instanceof Boolean) return serializeBool((Boolean) o);
        else if (o instanceof Collection<?>) return serializeCollection((Collection<?>) o);
        else if (o instanceof Object[]) return serializeCollection(Arrays.asList((Object[])o));
        else if (o.getClass().isArray()) {
            List<Object> aux = new ArrayList<>();
            for(int i = 0; i < Array.getLength(o); i++){
                aux.add(Array.get(o, i));
            }
            return serializeCollection(aux);
        }
        else if (o instanceof Map<?,?>) return serializeMap((Map<?, ?>) o);
        else throw new IllegalArgumentException(String.format("o (of type %s) is not a serializable primitive nor AutoSerializable", o.getClass().getName()));
    }

    /**
     * Serializes a collection
     *
     * @param col   The collection to serialize
     * @return      The serialized collection
     */
    public String serializeCollection(Collection<?> col)
    {
        this.tabDepth++;

        String ans = col.stream()
                .map(this::serializeGenericObj)
                .collect(Collectors.joining(
                        this.indented ? ",\n" + "\t".repeat(this.tabDepth) : ",",
                        this.indented ? "[\n" + "\t".repeat(this.tabDepth) : "[",
                        this.indented ? "\n" + "\t".repeat(this.tabDepth - 1) + "]" : "]"
                ));

        this.tabDepth--;
        return ans;
    }

    /**
     * Serializes a map
     *
     * @param map   The map to serialize
     * @return      The serialized map
     */
    public String serializeMap(Map<?, ?> map)
    {
        this.tabDepth++;

        String ans = map.entrySet().stream()
                .map(p -> String.format(this.indented ? "%s: %s" : "%s:%s", serializeGenericObj(p.getKey()), serializeGenericObj(p.getValue())))
                .collect(Collectors.joining(
                        this.indented ? ",\n" + "\t".repeat(this.tabDepth) : ",",
                        this.indented ? "{\n" + "\t".repeat(this.tabDepth) : "{",
                        this.indented ? "\n" + "\t".repeat(this.tabDepth - 1) + "}" : "}"));

        this.tabDepth--;
        return ans;
    }
}
