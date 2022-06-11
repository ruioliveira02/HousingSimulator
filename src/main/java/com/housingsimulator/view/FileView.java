package com.housingsimulator.view;

public class FileView extends View
{
    public void loadSuccess() {
        System.out.println("Simulation successfully loaded");
    }

    public void saveSuccess() {
        System.out.println("Simulation successfully saved");
    }

    public void dumpString(String message) {
        System.out.println(message);
    }
}
