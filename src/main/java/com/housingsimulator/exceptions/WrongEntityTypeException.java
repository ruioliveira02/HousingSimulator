package com.housingsimulator.exceptions;

/**
 * Exception representing that the entity is not of the correct type
 */
public class WrongEntityTypeException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public WrongEntityTypeException(String message) {
        super(message);
    }
}
