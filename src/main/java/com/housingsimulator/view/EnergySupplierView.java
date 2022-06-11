package com.housingsimulator.view;

import com.housingsimulator.model.EnergySupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The view of the energy suppliers
 */
public class EnergySupplierView extends View{

    /**
     * Prints a single energy supplier to the console
     * @param id the id of the supplier
     * @param name the name of the supplier
     * @param baseValue the base value of the supplier
     * @param tax the tax of the supplier
     * @param priceFormula the price formula of the supplier
     */
    public void show(int id, String name, double baseValue, double tax, String priceFormula) {
        System.out.println("Id: " + id);
        System.out.println("Nome: " + name);
        System.out.println("Valor base: " + baseValue);
        System.out.println("Imposto: " + tax);
        System.out.println("Fórmula: " + priceFormula);
    }
    /**
     * Prints a single energy supplier to the console
     * @param id the id of the supplier
     * @param name the name of the supplier
     * @param baseValue the base value of the supplier
     * @param tax the tax of the supplier
     * @param priceFormula the price formula of the supplier
     * @param sold total money from selling electricity
     */
    public void showBest(int id, String name, double baseValue, double tax, String priceFormula,Double sold) {
        System.out.println("Id: " + id);
        System.out.println("Nome: " + name);
        System.out.println("Valor base: " + baseValue);
        System.out.println("Imposto: " + tax);
        System.out.println("Fórmula: " + priceFormula);
        System.out.println("Money made: " + sold);
    }

    /**
     * Prints the given energy suppliers' details to the console
     * @param n the number of suppliers
     * @param id the ids of the suppliers
     * @param name the names of the suppliers
     * @param baseValue the base value of the suppliers
     * @param tax the tax of the suppliers
     * @param priceFormula the price formula of the suppleirs
     */
    public void showAll(int n, List<Integer> id, List<String> name, List<Double> baseValue,
                        List<Double> tax, List<String> priceFormula) {

        List<String> headers = Arrays.asList("Id", "Nome", "Valor base", "Imposto", "Fórmula");
        TablePrinter.tablePrint(n, headers, id, name, baseValue, tax, priceFormula);
    }
}
