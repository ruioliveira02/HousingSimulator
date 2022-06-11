package com.housingsimulator.exceptions;

/**
 * Exception refering to the fact that an entity already exists and cannot be duplicated
 */
public class AlreadyExistingEntityException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public AlreadyExistingEntityException(String message) {
        super(message);
    }
}
