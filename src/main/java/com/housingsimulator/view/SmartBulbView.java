package com.housingsimulator.view;

import java.util.Arrays;
import java.util.List;

/**
 * The view for light bulbs
 */
public class SmartBulbView extends View{
    /**
     * Shows a bulb
     * @param id the id of the bulb
     * @param name the name of the bulb
     * @param on whether the bulb is on
     * @param colour the colour of the bulb
     * @param dimension the dimension of the bulb
     */
    public void show(int id, String name, boolean on, String colour, float dimension) {
        System.out.println("BULB INFO");
        System.out.println("===================");
        System.out.println();

        System.out.println("Id: " + id);
        System.out.println("Name: " + name);
        System.out.println("On: " + on);
        System.out.println("Colour: " + colour);
        System.out.println("Dimension: " + dimension);
    }

    /**
     * Shows a list of bulbs
     * @param n the number of bulbs
     * @param ids the ids of the bulbs
     * @param names the names of the bulbs
     * @param on whether the bulbs are on
     * @param colours the colours of the bulbs
     * @param dimensions the dimensions of the bulbs
     */
    public void showAll(int n, List<Integer> ids, List<String> names, List<Boolean> on,
                        List<String> colours, List<Float> dimensions) {
        System.out.println("BULBS INFO");
        System.out.println("===================");
        System.out.println();
        List<String> headers = Arrays.asList("Id", "Name", "On", "Colours", "Dimension");

        TablePrinter.tablePrint(n, headers, ids, names, on, colours, dimensions);
    }
}
