package com.housingsimulator.view;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * View for smart devices
 */
public class SmartDeviceView extends View {

    /**
     * Displays a list of smart devices to the terminal
     * @param ids the ids of the devices
     * @param names the names of the devices
     * @param on whether the devices are on
     * @param houses the houses the devices are in
     * @param rooms the rooms the devices are in
     */
    public void showAll(List<Integer> ids, List<String> names, List<Boolean> on, List<String> houses,
                        List<String> rooms) {
        int n = ids.size();

        List<String> headers = Arrays.asList("Id", "Name", "On", "House","Room");

        TablePrinter.tablePrint(n, headers, ids, names, on, houses, rooms);
    }
}
