package com.housingsimulator.view;

public class SimulationView extends View {
    public void successSet() {
        System.out.println("Successfully set the time of the simulation");
    }

    public void successGet(Double time){ System.out.println("The simulation is currently at: "+time.toString());}

    public void successAdd() {
        System.out.println("Successfully advanced the simulation");
    }
}
