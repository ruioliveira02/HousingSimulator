package com.housingsimulator;

import com.housingsimulator.controller.*;
import com.housingsimulator.model.Model;

/**
 * Class containing the main application entry point
 */
public class Main {
    /**
     * Main entry point
     * @param args the program args (ignored)
     */
    public static void main(String[] args) {
        Model model = new Model();
        MainController controller = new MainController(model);
        controller.run();
    }
}
