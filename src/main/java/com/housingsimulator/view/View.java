package com.housingsimulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Definition of a view (MVC)
 */
public abstract class View {
    /**
     * Displays an error to the console
     * @param message the error message
     */
    public void error(String message) {
        System.out.println(String.format("An error has occurred: %s", message));
    }

    /**
     * Display a warning to the console
     * @param message the warning message
     */
    public void warning(String message) {
        System.out.println(String.format("Warning: %s", message));
    }
}
