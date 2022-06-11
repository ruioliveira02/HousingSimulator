package com.housingsimulator.serialization;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The class that handles deserialization
 */
public class Deserializer
{
    private static final String escapedSequence = "(.*?[^\\\\](\\\\{2})*)??"; /*! Regex representing an escaped sequence of characters */

    private static final Pattern stringPat  = Pattern.compile("[\\s\\n]*\"(?<str>%s)\"".replace("%s",escapedSequence)); /*! Regex representing a serialized string */
    private static final Pattern longPat = Pattern.compile("[\\s\\n]*(?<long>-?\\d+)"); /*! Regex representing a serialized long */
    private static final Pattern doublePat  = Pattern.compile("[\\s\\n]*(?<double>-?\\d+\\.\\d+)"); /*! Regex representing a serialized double */
    private static final Pattern boolPat  = Pattern.compile("[\\s\\n]*(?<bool>true|false)"); /*! Regex representing a serialized boolean */
    private static final Pattern objPat     = Pattern.compile("[\\s\\n]*(?<class>%s)\\$(?<ref>null|\\d+)\\$(?<data>\\{)?".replace("%s",escapedSequence)); /*! Regex
                                                                                                                                representing a serialized AutoSerializable */
    //TODO: permitir autoobjs sem campo ref
    //TODO: longs/strings/doubles podem ser null ???
    //TODO: permitir doubles sem parte fracionaria

    /**
     * Creates a Pattern that matches with the specified word, preceded by any number of whitespaces and newlines
     *
     * @param word  The specified word
     * @return      The created Pattern
     */
    private static Pattern wordPattern(String word) {
        return Pattern.compile(String.format("[\\s\\n]*\\Q%s\\E", word));
    }

    Map<Integer,AutoSerializable> deserializedObjects; /*! All AutoSerializable objects already deserialized by the Deserializer */

    /**
     * Constructs a new Deserializer
     */
    public Deserializer() {
        this.deserializedObjects = new HashMap<>();
    }

    /**
     * Returns the character that corresponds to the escape sequence \c
     *
     * @param c The given character
     * @return  The corresponding character
     * @throws DeserializationException If no escape code exists with character c
     */
    private char unescapeChar(char c)
            throws DeserializationException
    {
        return switch (c) {
            case '\\', '"', '{', '}', '$' -> c;
            default -> throw new DeserializationException(String.format("Unknown escape code \\%c", c), 0, Character.toString(c));
        };
    }

    /**
     * Replaces all escape sequences in input text with the corresponding characters
     *
     * @param s The input text
     * @return  The unescaped text
     * @throws DeserializationException If there are invalid escape sequences or the input ends in an unfinished escape sequence
     */
    private String unescape(CharSequence s)
            throws DeserializationException
    {
        var escaping = new Object() { boolean value = false; };

        try {
            String ans = s.chars()
                    .mapToObj(i -> {
                        try {
                            if (escaping.value) {
                                escaping.value = false;
                                return Character.toString(unescapeChar((char)i));
                            }
                            else if ((char)i == '\\') {
                                escaping.value = true;
                                return "";
                            }
                            else
                                return Character.toString((char)i);
                        } catch (DeserializationException e) {
                            throw new IllegalArgumentException("Wrapper exception", e);
                        }
                    }).collect(Collectors.joining());

            if (escaping.value)
                throw new DeserializationException("Expected escape sequence", s.length(), s.toString());

            return ans;
        }
        catch (IllegalArgumentException e) {
            throw new DeserializationException("Error parsing escaped string", 0, (DeserializationException)e.getCause(), s.toString());
        }
    }

    /**
     * Attempts to deserialize a AutoSerializable object, given a match with AutoSerializable regex pattern
     *
     * @param s     The input
     * @param mat   The match
     * @return      The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    private DeserializationResult<AutoSerializable> deserializeAuto(CharSequence s, Matcher mat)
            throws DeserializationException
    {
        //reference or null
        if (mat.group("class").isEmpty())
        {
            if (mat.group("ref").equals("null"))
                return new DeserializationResult<>(null, mat.end());

            int id = Integer.parseInt(mat.group("ref"));

            if (!deserializedObjects.containsKey(id))
                throw new DeserializationException("Ref key not found. Is the deserialization occurring in the same order as the serialization?", mat.start("ref"), mat.group());

            return new DeserializationResult<>(deserializedObjects.get(id), mat.end() - mat.start());
        }

        if (mat.group("data") == null)
            throw new DeserializationException("{ expected", mat.end("ref" + 1), s);

        String className = unescape(mat.group("class"));
        AutoSerializable obj;

        //object
        try {
            Class<?> aux = Class.forName(className);

            if (!AutoSerializable.class.isAssignableFrom(aux))
                throw new DeserializationException("Class isn't AutoSerializable", mat.start("class"), mat.group());

            //warning checked by isAssignableFrom
            Class<? extends AutoSerializable> clazz = (Class<? extends AutoSerializable>)aux;

            int ref = Integer.parseInt(mat.group("ref"));
            if (deserializedObjects.containsKey(ref))
                throw new DeserializationException("Repeated ref key", mat.start("ref"), mat.group());

            obj = clazz.getConstructor().newInstance();
            deserializedObjects.put(ref, obj);
        }
        catch (ClassNotFoundException e) {
            throw new DeserializationException("Class not found", mat.start("class"), mat.group(), e);
        }
        catch (NoSuchMethodException | SecurityException e) {
            throw new DeserializationException("Empty constructor for class not available", mat.start("class"), mat.group(), e);
        }
        catch (ReflectiveOperationException e) {
            throw new DeserializationException("Error creating object of specified class", mat.start("class"), mat.group(), e);
        }

        int length = obj.deserializeData(this, s.subSequence(mat.end("data"), s.length()).toString());

        //TODO: allow empty space before }
        if (s.charAt(mat.end() + length) != '}')
            throw new DeserializationException("} expected", mat.end() + length, s);

        return new DeserializationResult<>(obj, mat.end() - mat.start() + length + 1);
    }

    //TODO: improve regex performance (search only at beginning of input)

    /**
     * Interprets given input as a serialized String and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<String> deserializeString(CharSequence s)
            throws DeserializationException
    {
        Matcher stringMat = stringPat.matcher(s);
        if (!stringMat.find() || stringMat.start() != 0)
            throw new DeserializationException("Serialized string expected", 0, s);

        return new DeserializationResult<>(stringMat.group("str"), stringMat.end());
    }

    /**
     * Interprets given input as a serialized Long and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<Long> deserializeLong(CharSequence s)
            throws DeserializationException
    {
        Matcher longMat = longPat.matcher(s);
        if (!longMat.find() || longMat.start() != 0)
            throw new DeserializationException("Integer expected", 0, s);

        return new DeserializationResult<>(Long.parseLong(longMat.group("long")), longMat.end());
    }

    /**
     * Interprets given input as a serialized Double and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<Double> deserializeDouble(CharSequence s)
            throws DeserializationException
    {
        Matcher doubleMat = doublePat.matcher(s);
        if (!doubleMat.find() || doubleMat.start() != 0)
            throw new DeserializationException("Double expected", 0, s);

        return new DeserializationResult<>(Double.parseDouble(doubleMat.group("double")),doubleMat.end());
    }

    /**
     * Interprets given input as a serialized Boolean and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<Boolean> deserializeBool(CharSequence s)
            throws DeserializationException
    {
        Matcher boolMat = boolPat.matcher(s);
        if (!boolMat.find() || boolMat.start() != 0)
            throw new DeserializationException("Boolean expected", 0, s);

        return new DeserializationResult<>(boolMat.group("bool").equals("true"), boolMat.end());
    }

    /**
     * Interprets given input as a serialized AutoSerializable and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<AutoSerializable> deserializeObj(CharSequence s)
            throws DeserializationException
    {
        Matcher objMat = objPat.matcher(s);
        if (!objMat.find() || objMat.start() != 0)
            throw new DeserializationException("Serialized object expected", 0, s);

        return deserializeAuto(s, objMat);
    }

    /**
     * Tries to interpret the input as any possible serialized type
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If no type matches, or if the deserialization process fails
     */
    private DeserializationResult<?> deserializeGenericObj(CharSequence s)
            throws DeserializationException
    {
        Matcher empty = wordPattern("").matcher(s);
        empty.find(); //will always return true, since matcher matches the empty string
        char firstChar = s.charAt(empty.end());

        if (firstChar == '[')
            return deserializeCollection(s);
        else if (firstChar == '{')
            return deserializeMap(s);

        Matcher stringMat = stringPat.matcher(s);
        if (stringMat.find() && stringMat.start() == 0)
            return new DeserializationResult<>(stringMat.group("str"), stringMat.end());

        try {
            Matcher doubleMat = doublePat.matcher(s);
            if (doubleMat.find() && doubleMat.start() == 0)
                return new DeserializationResult<>(Double.parseDouble(doubleMat.group("double")),doubleMat.end());

            Matcher longMat = longPat.matcher(s);
            if (longMat.find() && longMat.start() == 0)
                return new DeserializationResult<>(Long.parseLong(longMat.group("long")), longMat.end());
        }
        catch (NumberFormatException e) {
            //The format of the string was checked, so this shouldn't happen. Still, can't hurt to check
            throw new DeserializationException("Number parsing failed", 0, s, e);
        }

        Matcher boolMat = boolPat.matcher(s);
        if (boolMat.find() && boolMat.start() == 0)
            return new DeserializationResult<>(boolMat.group("bool").equals("true"), boolMat.end());

        Matcher objMat = objPat.matcher(s);
        if (objMat.find() && objMat.start() == 0)
            return deserializeAuto(s, objMat);

        throw new DeserializationException("Serialized object doesn't match any possible serialized object type", empty.end(), s);
    }

    /**
     * Interprets given input as a serialized Collection and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<? extends Collection<?>> deserializeCollection(CharSequence s)
            throws DeserializationException
    {
        Matcher begin = wordPattern("[").matcher(s);
        Matcher end = wordPattern("]").matcher(s);
        Matcher separator = wordPattern(",").matcher(s);

        if (!begin.find() || begin.start() != 0) {
            Matcher empty = wordPattern("").matcher(s);
            empty.find(); //will always return true, since matcher matches the empty string
            throw new DeserializationException("Serialized collections must begin with '['", empty.end(), s);
        }

        Collection<Object> ans = new ArrayList<>();
        boolean empty = true;
        int pos = begin.end();

        while (!end.find(pos) || end.start() != pos)
        {
            if (empty)
                empty = false;
            else {
                if (!separator.find(pos) || separator.start() != pos)
                    throw new DeserializationException("',' expected", pos, s);
                pos = separator.end();
            }

            try {
                DeserializationResult<?> aux = deserializeGenericObj(s.subSequence(pos, s.length()));
                ans.add(aux.ans());
                pos += aux.consumedLength();
            }
            catch (DeserializationException e) {
                throw new DeserializationException("Serialized object expected", pos, e, s);
            }
        }

        return new DeserializationResult<>(ans, end.end());
    }

    /**
     * Interprets given input as a serialized Map and attempts to deserialize it
     *
     * @param s The input
     * @return  The result of the deserialization
     * @throws DeserializationException If the deserialization process fails
     */
    public DeserializationResult<? extends Map<?, ?>> deserializeMap(CharSequence s)
            throws DeserializationException
    {
        Matcher begin = wordPattern("{").matcher(s);
        Matcher end = wordPattern("}").matcher(s);
        Matcher separator1 = wordPattern(":").matcher(s);
        Matcher separator2 = wordPattern(",").matcher(s);

        if (!begin.find() || begin.start() != 0) {
            Matcher empty = wordPattern("").matcher(s);
            empty.find(); //will always return true, since matcher matches the empty string
            throw new DeserializationException("Serialized maps must begin with '{'", empty.end(), s);
        }

        Map<Object,Object> ans = new HashMap<>();
        boolean empty = true;
        int pos = begin.end();

        while (!end.find(pos) || end.start() != pos)
        {
            if (empty)
                empty = false;
            else {
                if (!separator2.find(pos) || separator2.start() != pos)
                    throw new DeserializationException("',' expected", pos, s);
                pos = separator2.end();
            }

            DeserializationResult<?> key, value;

            try {
                key = deserializeGenericObj(s.subSequence(pos, s.length()));
            } catch (DeserializationException e) {
                throw new DeserializationException("Serialized object expected", pos, e, s);
            }

            if (ans.containsKey(key.ans()))
                throw new DeserializationException("Repeated key in map not allowed", pos, s);

            pos += key.consumedLength();

            if (!separator1.find(pos) || separator1.start() != pos)
                throw new DeserializationException("':' expected", pos, s);
            pos = separator1.end();

            try {
                value = deserializeGenericObj(s.subSequence(pos, s.length()));
            } catch (DeserializationException e) {
                throw new DeserializationException("Serialized object expected", pos, e, s);
            }

            pos += value.consumedLength();
            ans.put(key.ans(), value.ans());
        }

        return new DeserializationResult<>(ans, end.end());
    }
}
