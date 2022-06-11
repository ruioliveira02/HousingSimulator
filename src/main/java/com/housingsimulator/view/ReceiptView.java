package com.housingsimulator.view;








import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The view of the receipts
 */
public class ReceiptView extends View {

    /**
     * the view to show a Receipt
     * @param startTime the starting time of the receipt
     * @param endTime the ending Time of the receipt
     * @param houseId the id of the house the receipt is to
     * @param houseName the name of the house the receipt is to
     * @param houseOwnerName the name of the customer the receipt is to
     * @param houseCustomerNIF the NIF of the customer the receipt is to
     * @param supplierId the id of the supplier who emitted the receipt
     * @param supplierName the name of the supplier who emitted the receipt
     * @param baseValue the base value the supplier enforces
     * @param tax the tax paid
     * @param priceFormula the formula the supplier used to get the price
     * @param total the total to pay
     * @param energyConsumption the total consumption
     */
    public void showReceipt(double startTime, double endTime, int houseId,String houseName,
                            String houseOwnerName,int houseCustomerNIF,int supplierId,String supplierName,
                            double baseValue, double tax,String priceFormula,double total,double energyConsumption) {
        System.out.println("RECEIPT INFO");
        System.out.println("=============================");
        System.out.println("Starting time: " + startTime);
        System.out.println("Ending time: " + endTime);
        System.out.println("house Id: " + houseId);
        System.out.println("house Name: " + houseName);
        System.out.println("house owner's name: " + houseOwnerName);
        System.out.println("house owner's NIF: " + houseCustomerNIF);
        System.out.println("energy supplier id: " + supplierId);
        System.out.println("energy supplier name: " + supplierName);
        System.out.println("energy supplier's base value: " + baseValue);
        System.out.println("energy supplier's tax: " + tax);
        System.out.println("energy supplier's price formula: " + priceFormula);
        System.out.println("total to pay: " + total);
        System.out.println("energyConsumed: " + energyConsumption);
    }



    /**
     * Displays a list of speakers to the terminal
     * @param n the number of speakers to display
     * @param startTimes the starting time of the receipts
     * @param endTimes the ending time of the receipts
     * @param houseIds the id of the house the receipt is to
     * @param houseNames the name of the house the receipt is to
     * @param houseOwnerNames the name of the customer the receipt is to
     * @param houseCustomerNIFs the NIF of the customer the receipt is to
     * @param supplierIds the id of the supplier who emitted the receipt
     * @param supplierNames the name of the supplier who emitted the receipt
     * @param baseValues the base value the supplier enforces
     * @param taxes the tax paid
     * @param priceFormulas the formula the supplier used to get the price
     * @param totals the totals paid
     * @param energyConsumed the total energy consumed
     */
    public void showReceipts(int n, List<Double> startTimes, List<Double> endTimes, List<Integer> houseIds,
                             List<String> houseNames, List<String> houseOwnerNames, List<Integer> houseCustomerNIFs,
                             List<Integer> supplierIds, List<String> supplierNames, List<Double> baseValues,
                             List<Double> taxes, List<String> priceFormulas, List<Double> installationPrices,
                             List<Double> totals, List<Double> energyConsumed){

        System.out.println("RECEIPTS INFO");
        System.out.println("===========================");
        System.out.println();

        List<String> headers = Arrays.asList("Starting time", "Ending time", "house Id", "house Name",
                "house owner's name","house owner's NIF","energy supplier id","energy supplier name",
                "energy supplier's base value", "energy supplier's tax","energy supplier's price formula",
                "total cost of installations", "total to pay", "energyConsumed");

        TablePrinter.tablePrint(n,headers, startTimes, endTimes, houseIds, houseNames, houseOwnerNames, houseCustomerNIFs,
                supplierIds, supplierNames, baseValues, taxes, priceFormulas, installationPrices, totals, energyConsumed);
    }
}