package com.housingsimulator.controller;

import com.housingsimulator.annotations.API;
import com.housingsimulator.annotations.Endpoint;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.Entity;
import com.housingsimulator.view.ReceiptView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller for receipt
 */
@API
public class ReceiptController extends Controller {


    /**
     * Default constructor
     *
     * @param model The model of the application
     */
    public ReceiptController(Model model) {
        this.setModel(model);
        this.setView(new ReceiptView());
    }

    /**
     * Gets all the Receipts in the simulation
     */
    @Endpoint(regex = "RECEIPT LIST ALL INITIALDATE=(\\d+) FINALDATE=(\\d+)")
    public void getReceipts(Double startTime,Double endTime) {
        if (!(startTime<endTime && endTime<=this.getModel().getTime())) {
            this.getView().warning("either the start time is smaller than the end or the end is bigger than the current: " + this.getModel().getTime());
        }
        else {
            Set<Integer> houseIds = this.getModel().getSimulation().getHouses().stream().map(SmartHouse::getId).collect(Collectors.toSet());
            List<Receipt> receipts = new ArrayList<>();

            for (Integer h : houseIds)
                receipts.addAll(this.getModel().getReceiptsByHouse(h, startTime, endTime));

            this.callViewOnReceiptCollection(receipts);
        }
    }

    /**
     * shows all receipts of a given supplier
     * @param id
     */
    @Endpoint(regex = "RECEIPT GET SUPPLIER=(\\d+)")
    public void getReceiptsbySupplier(Integer id){
        Entity ent = this.getModel().getSimulation().getEntity(id);
        if (ent instanceof EnergySupplier) {
            List<Receipt> receipts = this.getModel().getReceiptsBySupplier(id);
            callViewOnReceiptCollection(receipts);
        }
        else {
            this.getView().warning("no energy supplier exists with the given id");
        }
    }

    /**
     * shows all receipts of a given house
     * @param id
     */
    @Endpoint(regex = "RECEIPT GET HOUSE=(\\d+)")
    public void getReceiptsbyHouse(Integer id){
        Entity ent = this.getModel().getSimulation().getEntity(id);
        if (ent instanceof SmartHouse) {
            List<Receipt> receipts = this.getModel().getReceiptsByHouse(id);
            callViewOnReceiptCollection(receipts);
        }
        else {
            this.getView().warning("no house exists with the given id");
        }
    }

    /**
     * shows all receipts of a given house
     * @param id
     */
    @Endpoint(regex = "RECEIPT GET HOUSE=(\\d+) TIME=(\\d+\\.?\\d*)")
    public void getReceiptsbyHouseTime(Integer id,Double time){
        if (time>this.getModel().getTime()){
            this.getView().warning("the time is bigger than the current: " + this.getModel().getTime());
        }
        else {
            Entity ent = this.getModel().getSimulation().getEntity(id);
            if (ent instanceof SmartHouse) {
                List<Receipt> receipts = this.getModel().getReceiptsByHouse(id, time, time);
                callViewOnReceiptCollection(receipts);
            } else {
                this.getView().warning("no house exists with the given id");
            }
        }
    }




    /*
     * Calls the view on a list of Receipts
     *
     * @param Receipts the Receipts to print
     */
    private void callViewOnReceiptCollection(List<Receipt> receipts) {
        int n=0;
        List<Double> startTimes = new ArrayList<>();
        List<Double> endTimes = new ArrayList<>();
        List<Integer> houseIds = new ArrayList<>();
        List<String> houseNames = new ArrayList<>();
        List<String> houseOwnerNames = new ArrayList<>();
        List<Integer> houseCustomerNIFs = new ArrayList<>();
        List<Integer> supplierIds = new ArrayList<>();
        List<String> supplierNames = new ArrayList<>();
        List<Double> baseValues = new ArrayList<>();
        List<Double> taxes = new ArrayList<>();
        List<String> priceFormulas = new ArrayList<>();
        List<Double> installationPrices = new ArrayList<>();
        List<Double> totals = new ArrayList<>();
        List<Double> energyConsumed = new ArrayList<>();


        for (Receipt r:receipts){
            n++;
            SmartHouse h=r.getCustomer();
            EnergySupplier e=r.getSupplier();

            startTimes.add(r.getStartTime());
            endTimes.add(r.getEndTime());
            houseIds.add(h.getId());
            houseNames.add(h.getName());
            houseOwnerNames.add(h.getOwnerName());
            houseCustomerNIFs.add(h.getOwnerNif());
            supplierIds.add(e.getId());
            supplierNames.add(e.getName());
            baseValues.add(e.getBaseValue());
            taxes.add(e.getTax());
            priceFormulas.add(e.getPriceFormula());
            installationPrices.add(r.getInstallations().stream().mapToDouble(Installation::price).sum());
            totals.add(r.getPrice());
            energyConsumed.add(r.getConsumptions().values().stream().reduce(0.0,Double::sum));
        }

        this.getView().showReceipts(n, startTimes, endTimes, houseIds, houseNames, houseOwnerNames, houseCustomerNIFs,
                supplierIds, supplierNames, baseValues, taxes, priceFormulas, installationPrices, totals, energyConsumed);
    }

    /**
     * Returns the view associated with the controller in the correct type
     *
     * @return the view of the controller
     */
    @Override
    public ReceiptView getView() {
        return (ReceiptView) super.getView();

    }
}