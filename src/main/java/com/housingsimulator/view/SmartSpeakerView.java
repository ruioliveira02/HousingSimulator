package com.housingsimulator.view;

import java.util.Arrays;
import java.util.List;

/**
 * The view of the speakers
 */
public class SmartSpeakerView extends View {
    /**
     * Displays a speaker to the terminal
     * @param id the id of the speaker
     * @param speakerName the name of the speaker
     * @param on whether the speaker is on
     * @param speakerBrand the name of the brand of the speaker
     * @param dailyConsumption the daily consumption of the speaker
     * @param volume the volume of the speakers
     * @param radioStation the stations playing
     * @param installation the installation price
     */
    public void show(int id, String speakerName, boolean on, String speakerBrand, int dailyConsumption,
                            int volume, String radioStation, double installation) {
        System.out.println("SPEAKER INFO");
        System.out.println("=============================");
        System.out.println("Id: " + id);
        System.out.println("Name: " + speakerName);
        System.out.println("On: " + on);
        System.out.println("Brand: " + speakerBrand);
        System.out.println("Daily Consumption: " + dailyConsumption);
        System.out.println("Volume: " + volume);
        System.out.println("Radio Station: " + radioStation);
        System.out.println("Installation cost: " + installation);
    }

    /**
     * Displays a brand to the terminal
     * @param name the name of the brand
     * @param dailyConsumption the daily consumption of the brand
     */
    public void showBrand(String name, int dailyConsumption) {
        System.out.println("BRAND INFO");
        System.out.println("=============================");
        System.out.println("Name: " + name);
        System.out.println("Daily Consumption: " + dailyConsumption);
    }

    /**
     * Displays a list of speakers to the terminal
     * @param n the number of speakers to displau
     * @param id the ids of the speakers
     * @param speakerName the names of the speakers
     * @param on whether the speakers are on
     * @param speakerBrand the name of the brands of the speakers
     * @param dailyConsumption the daily consumption of the speakers
     * @param volume the volume of the speakers
     * @param radioStations the stations playing
     * @param installatioons the installations cost
     */
    public void showAll(int n, List<Integer> id, List<String> speakerName, List<Boolean> on,
                             List<String> speakerBrand, List<Integer> dailyConsumption,
                             List<Integer> volume, List<String> radioStations, List<Double> installatioons) {

        System.out.println("SPEAKERS INFO");
        System.out.println("===========================");
        System.out.println();

        List<String> headers = Arrays.asList("Id", "Name", "On", "Brand", "Daily Consumption", "Installation Cost");

        TablePrinter.tablePrint(n, headers, id, speakerName,on, speakerBrand,
                dailyConsumption, volume, radioStations, installatioons);
    }

    /**
     * Display a list of brands to the terminal
     * @param n the number of brands
     * @param names the names of the brands
     * @param dailyConsumptions the daily consumption of the brands
     */
    public void showBrands(int n, List<Integer> ids, List<String> names, List<Integer> dailyConsumptions,
                           List<Double> installations) {
        System.out.println("BRANDS INFO");
        System.out.println("===========================");
        System.out.println();

        List<String> headers = Arrays.asList("Id", "Name", "Daily Consumption", "Installation Cost");
        TablePrinter.tablePrint(n, headers, ids, names, dailyConsumptions, installations);
    }
}