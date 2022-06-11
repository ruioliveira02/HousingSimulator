package com.housingsimulator.exceptions;

/**
 * Exception noting the attempt of billing a house from a different supplier then the one serving the house
 */
public class WrongSupplierException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public WrongSupplierException(String message) {
        super(message);
    }
}
