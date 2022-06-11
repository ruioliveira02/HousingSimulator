package com.housingsimulator.exceptions;

import java.io.IOException;

/**
 * Exception indicating an error whilst processing the formula for a supplier. Can either mean there is a missing
 * variable or the expression is invalid in itself
 */
public class InvalidFormulaException extends IOException {
    /**
     * Default constructor
     * @param message the exception message
     */
    public InvalidFormulaException(String message) {
        super(message);
    }
}
