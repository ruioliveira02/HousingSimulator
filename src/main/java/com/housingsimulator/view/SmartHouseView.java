package com.housingsimulator.view;
import java.util.*;

public class SmartHouseView extends View{



    public void showHouse(int id, String houseName,String ownerName,Integer ownerNif,Integer supplierId,
                          String supplierName,Double supplierBaseValue,Double supplierTax,String supplierFormula,
                          Map<String, Set<Integer>> devicesPerRoom) {
        System.out.println("HOUSE INFO");
        System.out.println("=============================");
        System.out.println("Id: " + id);
        System.out.println("Name: " + houseName);

        System.out.println("Owner's Name: " + ownerName);
        System.out.println("Owner's NIF: " + ownerNif);

        System.out.println("Energy Supplier Id: " + supplierId);
        System.out.println("Energy Supplier Name: " + supplierName);
        System.out.println("Energy Supplier base value: " + supplierBaseValue);
        System.out.println("Energy Supplier tax: " + supplierTax);
        System.out.println("Energy Supplier formula: " + supplierFormula);

        System.out.println();

        List<String> deviceRoom = new ArrayList<>();
        List<Integer> devicesId = new ArrayList<>();

        for (Map.Entry<String,Set<Integer>> e: devicesPerRoom.entrySet()){
            for (Integer d:e.getValue()) {
                deviceRoom.add(e.getKey());
                devicesId.add(d);
            }
        }

        List<String> headers = Arrays.asList("Room","DeviceId");
        TablePrinter.tablePrint(deviceRoom.size(),headers,deviceRoom,devicesId);


    }
    public void showMostSpentHouse(int id, String houseName,String ownerName,Integer ownerNif,Integer supplierId,
                                   String supplierName,Double supplierBaseValue,Double supplierTax,String supplierFormula,
                                   Map<String, Set<Integer>> devicesPerRoom,Double moneyPaid){
        System.out.println("HOUSE INFO");
        System.out.println("=============================");
        System.out.println("Id: " + id);
        System.out.println("Name: " + houseName);

        System.out.println("Owner's Name: " + ownerName);
        System.out.println("Owner's NIF: " + ownerNif);

        System.out.println("Energy Supplier Id: " + supplierId);
        System.out.println("Energy Supplier Name: " + supplierName);
        System.out.println("Energy Supplier base value: " + supplierBaseValue);
        System.out.println("Energy Supplier tax: " + supplierTax);
        System.out.println("Energy Supplier formula: " + supplierFormula);
        System.out.println("Money paid to the supplier in the Interval: "+ moneyPaid);

        System.out.println();

    List<String> deviceRoom = new ArrayList<>();
    List<Integer> devicesId = new ArrayList<>();

        for (Map.Entry<String,Set<Integer>> e: devicesPerRoom.entrySet()){
        for (Integer d:e.getValue()) {
            deviceRoom.add(e.getKey());
            devicesId.add(d);
        }
    }

    List<String> headers = Arrays.asList("Room","DeviceId");
        TablePrinter.tablePrint(deviceRoom.size(),headers,deviceRoom,devicesId);


}

    public void showHouses(int n,List<Integer> id, List<String> name,List<String> ownerName,List<Integer> ownerNif,
                           List<Integer> supplierId, List<String> supplierName,List<Double> supplierBaseValue,
                           List<Double> supplierTax,List<String> supplierFormula,
                           List<Map<String, Set<Integer>>> devicesPerRoom) {

        System.out.println("HOUSES INFO");
        System.out.println("===========================");
        System.out.println();
        List<String> headers = Arrays.asList("Room","DeviceId");
        for (int i=0;i<n;i++){
            System.out.println("=============================");
            System.out.println("Id: " + id.get(i));
            System.out.println("Name: " + name.get(i));

            System.out.println("Owner's Name: " + ownerName.get(i));
            System.out.println("Owner's NIF: " + ownerNif.get(i));

            System.out.println("Energy Supplier Id: " + supplierId.get(i));
            System.out.println("Energy Supplier Name: " + supplierName.get(i));
            System.out.println("Energy Supplier base value: " + supplierBaseValue.get(i));
            System.out.println("Energy Supplier tax: " + supplierTax.get(i));
            System.out.println("Energy Supplier formula: " + supplierFormula.get(i));
            System.out.println();

            List<String> deviceRoom = new ArrayList<>();
            List<Integer> devicesId = new ArrayList<>();

            for (Map.Entry<String,Set<Integer>> e: devicesPerRoom.get(i).entrySet()){
                for (Integer d:e.getValue()) {
                    deviceRoom.add(e.getKey());
                    devicesId.add(d);
                }
            }

            TablePrinter.tablePrint(deviceRoom.size(),headers,deviceRoom,devicesId);
        }
    }


    public void showHousesConsumption(int n,List<Integer> id, List<String> name,List<String> ownerName,List<Integer> ownerNif,
                                      List<Integer> supplierId, List<String> supplierName,List<Double> supplierBaseValue,
                                      List<Double> supplierTax,List<String> supplierFormula,
                                      List<Map<String, Set<Integer>>> devicesPerRoom,List<Double> energyUsed){
        System.out.println("HOUSES INFO");
        System.out.println("===========================");
        System.out.println();
        List<String> headers = Arrays.asList("Room","DeviceId");
        for (int i=0;i<n;i++){
            System.out.println("=============================");
            System.out.println("Id: " + id.get(i));
            System.out.println("Name: " + name.get(i));

            System.out.println("Owner's Name: " + ownerName.get(i));
            System.out.println("Owner's NIF: " + ownerNif.get(i));

            System.out.println("Energy Supplier Id: " + supplierId.get(i));
            System.out.println("Energy Supplier Name: " + supplierName.get(i));
            System.out.println("Energy Supplier base value: " + supplierBaseValue.get(i));
            System.out.println("Energy Supplier tax: " + supplierTax.get(i));
            System.out.println("Energy Supplier formula: " + supplierFormula.get(i));
            System.out.println("Energy consumed: "+ energyUsed.get(i));
            System.out.println();

            List<String> deviceRoom = new ArrayList<>();
            List<Integer> devicesId = new ArrayList<>();

            for (Map.Entry<String,Set<Integer>> e: devicesPerRoom.get(i).entrySet()){
                for (Integer d:e.getValue()) {
                    deviceRoom.add(e.getKey());
                    devicesId.add(d);
                }
            }

            TablePrinter.tablePrint(deviceRoom.size(),headers,deviceRoom,devicesId);
        }

    }


}
