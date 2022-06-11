package com.housingsimulator.exceptions;

/**
 * Exception representing an attempt of passing an illegal value to a supplier (either a negative tax or base value
 * attributes)
 */
public class IllegalSupplierValueException extends RuntimeException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public IllegalSupplierValueException(String message) {
        super(message);
    }
}
