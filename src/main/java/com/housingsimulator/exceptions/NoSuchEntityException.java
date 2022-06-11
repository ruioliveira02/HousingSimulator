package com.housingsimulator.exceptions;

/**
 * Exception representing the lack of an entity matching the given criteria
 */
public class NoSuchEntityException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public NoSuchEntityException(String message) {
        super(message);
    }
}
