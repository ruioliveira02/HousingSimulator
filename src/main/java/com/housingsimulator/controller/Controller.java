package com.housingsimulator.controller;

import com.housingsimulator.model.Model;
import com.housingsimulator.view.View;

/**
 * Generic class for a controller
 */

public abstract class Controller {
    private Model model; /*! The model of the application*/

    private View view; /*! The view handled by this controller */

    /**
     * Sets the view of the controller
     * @param view the new view of the controller
     */
    protected void setView(View view) {
        this.view = view;
    }

    /**
     * Gets the view the controller handles
     * @return the view the controller handles\
     */
    protected View getView() {
        return this.view;
    }

    /**
     * Gets the model of the application
     * @return the model of the application
     */
    protected Model getModel() {
        return this.model;
    }

    /**
     * Sets the model of the application
     * @param model the new model of the application
     */
    protected void setModel(Model model) {
        this.model = model;
    }
}
