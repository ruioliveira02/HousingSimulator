package com.housingsimulator.serialization;

/**
 * Represents the result of a deserialization operation
 * @param ans               The deserialized object
 * @param consumedLength    The length of text containing relevant information to the deserialization
 * @param <T>               The deserialized type
 */
public record DeserializationResult<T>(T ans, int consumedLength) { }
