package com.housingsimulator.view;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * View for a basic entity
 */
public class EntityView extends View {
    /**
     * Displays a list of entities to the terminal
     * @param ids the ids of the entities
     * @param names the names of the entities
     * @param types the types of the entities
     */
    public void showAll(List<Integer> ids, List<String> names, List<String> types) {
        int n = ids.size();

        List<String> headers = Arrays.asList("Id", "Name", "Type");

        TablePrinter.tablePrint(n, headers, ids, names, types);
    }
}
