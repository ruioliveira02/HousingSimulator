package com.housingsimulator.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Auxiliary class for pretty printing tables to the terminal
 */
public class TablePrinter {
    /**
     * Prints the table to the terminal
     * @param n the number of rows to print
     * @param headers the headers of the table
     * @param objs the columns of the table
     */
    public static void tablePrint(int n, List<String> headers, List<?>...objs) {
        List<Integer> lengths = getColumnLengths(headers, objs);
        List<List<String>> table = getRawStrings(n, headers.size(), objs);

        int length = 1 + lengths.stream().mapToInt(Integer::intValue).sum() + 3 * headers.size();


        printEdgeLine(length);
        printLine(headers, lengths);
        printEdgeLine(length);

        for(int i = 0; i < n; i++) {
            printLine(table.get(i), lengths);
        }

        printEdgeLine(length);
    }

    /**
     * Prints an edge (top / bottom) line of the table
     * @param length the length of the table
     */
    private static void printEdgeLine(int length) {
        System.out.print("+");

        for(int i = 2; i < length; i++)
            System.out.print("-");

        System.out.println("+");
    }

    /**
     * Prints a row to the terminal
     * @param line the columns
     * @param lengths the widths of each column
     */
    private static void printLine(List<String> line, List<Integer> lengths) {
        System.out.print("|");

        for(int i = 0; i < line.size(); i++) {
            int len = line.get(i).length();
            System.out.print(" ");

            System.out.print(line.get(i));

            for(int j = 0; j < lengths.get(i) - len; j++) {
                System.out.print(" ");
            }

            System.out.print(" |");
        }
        System.out.println();
    }

    /**
     * Gets the table cells as a list of lists
     * @param r the number of rows
     * @param c the number of columns
     * @param objs the objects corresponding to the columns of the table
     * @return the table cells
     */
    private static List<List<String>> getRawStrings(int r, int c, List<?>...objs) {
        List<List<String>> result = new ArrayList<>();

        for(int i = 0; i < r; i++) {
            result.add(new ArrayList<>());

            for(int j = 0; j < c; j++)
                result.get(i).add("");
        }


        int j = 0;
        for(List<?> item : objs) {

            for(int i = 0; i < r; i++) {
                result.get(i).set(j, item.get(i).toString());
            }
            j++;
        }

        return result;
    }

    /**
     * Gets the length of the columns of a table
     * @param headers the headers
     * @param objs the columns
     * @return the length of the columns (maximum amongst all Strings / cells)
     */
    private static List<Integer> getColumnLengths(List<String> headers, List<?>...objs) {

        List<Integer> result = new ArrayList<>(headers.stream().map(String::length).toList());

        if(objs.length != result.size())
            throw new IllegalArgumentException("Number of columns must match the number of headers");

        int c = -1;

        for(List<?> item : objs) {
            if(c == -1) {
                c = item.size();
            } else if(c != item.size()) {
                throw new IllegalArgumentException("All columns must have the same number of rows");
            }
        }

        c = 0;
        for(List<?> item : objs) {
            int max = 0;

            for(Object obj : item) {
                max = Math.max(max, obj.toString().length());
            }

            if(max > result.get(c))
                result.set(c, max);

            c++;
        }

        return result;
    }
}
