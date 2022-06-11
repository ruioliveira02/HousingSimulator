package com.housingsimulator.exceptions;

/**
 * Exception indicating the user tried to delete an entity which cant be deleted (like a receipt)
 */
public class InvalidEntityDeletionException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public InvalidEntityDeletionException(String message) {
        super(message);
    }
}
