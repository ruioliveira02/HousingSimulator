package com.housingsimulator.model;

import com.housingsimulator.exceptions.IllegalSupplierValueException;
import com.housingsimulator.exceptions.InvalidFormulaException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.exceptions.WrongSupplierException;
import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.simulation.Entity;
import com.udojava.evalex.Expression;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data of a energy supplier
 */
public class EnergySupplier extends AbstractEntity implements AutoSerializable, Serializable {
    private double baseValue;/*! base price*/
    private  double tax;/*! tax percentage*/
    private String priceFormula;/*! the formula to calculate the rest of the money owed*/

    /**
     * gets the base price enforced by the energy supplier
     * @return the baseValue
     */
    public double getBaseValue() {
        return this.baseValue;
    }

    /**
     * gets the tax applied to the money asked by the energy supplier
     * @return the tax percentage
     */
    public double getTax() {
        return this.tax;
    }

    /**
     * gets the price formula the energy supplier uses to calculate the price
     * @return the price formul
     */
    public String getPriceFormula() {
        return this.priceFormula;
    }

    /**
     * sets the base price enforced by the energy supplier
     * @param baseValue the value to set the base values as
     */
    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    /**
     * sets the tax applied to the money asked by the energy supplier
     * @param tax the value to set the tax percentage as
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * sets the price formula the energy supplier uses to calculate the price
     * @param priceFormula the string used to calculate the money owed
     */
    public void setPriceFormula(String priceFormula) {
        this.priceFormula = priceFormula;
    }

    /**
     * creates a new clean EnergySupplier
     */
    public EnergySupplier() {
        super();
        this.baseValue=0;
        this.tax=0;
        this.priceFormula = "0";
    }

    /**
     * creates a new EnergySupplier setting the elements
     * @param name the name of the supplier
     * @param baseValue the value to set the base value as
     * @param tax the tax percentage to set the tax as
     * @param priceFormula the price formula to calculate the money owed
     */
    public EnergySupplier(String name, double baseValue, double tax, String priceFormula) {
        super(name);
        if(baseValue < 0.0F || tax < 0.0F)
            throw new IllegalSupplierValueException(
                    String.format("Base values and tax must be positive: received base value %f and tax %f", baseValue, tax));

        this.baseValue = baseValue;
        this.tax = tax;
        this.priceFormula = priceFormula;
    }

    /**
     * creates a new EnergySupplier copying the elemnts from another
     * @param e EnergySupplier from where to copy the data
     */
    public EnergySupplier(EnergySupplier e) {
        super(e);
        this.baseValue=e.getBaseValue();
        this.tax=e.getTax();
        this.priceFormula= e.getPriceFormula();
    }

    /**
     * gets the price to pay for the electricity based on the passed variables
     * @param variables the variables to fill in the expression
     * @return the price to pay
     */
    public double getPrice(Map<String, Double> variables)
            throws InvalidFormulaException
    {
        Expression formula = new Expression(this.priceFormula);

        for(Map.Entry<String, Double> entry : variables.entrySet()) {
            formula = formula.with(entry.getKey(), new BigDecimal(entry.getValue()));
        }

        try {
            return formula.eval().doubleValue();
        } catch(Expression.ExpressionException e) {
            throw new InvalidFormulaException(String.format("Unable to compute the formula for supplier %s",
                    this));
        }
    }

    /**
     * Gets all the billing variables needed for calculating the price
     * @param house the house to bill
     * @return the variables
     *
     *  <table>
     *   <tr>
     *     <th>Variable Name</th>
     *     <th>Value</th>
     *   </tr>
     *   <tr>
     *     <td>&lt; Id &gt; &lt; Name &gt;</td>
     *     <td>The consumption of the device</td>
     *   </tr>
     *   <tr>
     *     <td>Consumption</td>
     *     <td>The total consumption of the house</td>
     *   </tr>
     *   <tr>
     *       <td>NoDevices</td>
     *       <td>The number of devices of the house</td>
     *   </tr>
     *   <tr>
     *       <td>NoRooms</td>
     *       <td>The number of rooms in the house</td>
     *   </tr>
     *   <tr>
     *       <td>Base</td>
     *       <td>Base cost of energy unit</td>
     *   </tr>
     *   <tr>
     *      <td>Tax</td>
     *      <td>Tax applied to energy</td>
     *   </tr>
     *   <tr>
     *      <td>Random</td>
     *      <td>A random number between 0 and 1</td>
     *   </tr>
     * </table>
     */
    private Map<String, Double> getBillingVariables(SmartHouse house) {
        Map<String, Double> variables = house.getDevicesConsumptions();

        variables.put("Consumption", variables.values().stream().mapToDouble(Double::doubleValue).sum());
        variables.put("NoDevices", (double)house.getDevices().size());
        variables.put("NoRooms", (double)house.getRooms().size());
        variables.put("Base", this.baseValue);
        variables.put("Tax", this.tax);
        variables.put("Random", Math.random());
        return variables;
    }

    /**
     * Emits a receipt to the given house
     * @param house the house to bill
     * @return the receipt
     */
    public Receipt billHouse(SmartHouse house, double startTime, double endTime) throws InvalidFormulaException, WrongSupplierException {
        if(house.getSupplierId() != this.getId())
            throw new WrongSupplierException("Attempting to bill house " + house + " with wrong supplier");

        Map<String, Double> variables = this.getBillingVariables(house);
        List<Installation> installations = new ArrayList<>();
        for (Map.Entry<String, Set<SmartDevice>> e:house.getInstallations().entrySet()){
            e.getValue().forEach(p ->
                    installations.add(new Installation(p.getName(), e.getKey(), p.installationPrice()))
            );
        }

        double price = this.getPrice(variables) + installations.stream().mapToDouble(Installation::price).sum();
        return new Receipt(startTime, endTime, this, house, installations, house.getDevicesConsumptions(), price);
    }

    /**
     * checks if two EnergySuppliers are equal
     * @param o object to compare to
     * @return true if the objects are equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergySupplier that = (EnergySupplier) o;
        return super.equals(o) && Double.compare(that.getBaseValue(), this.baseValue) == 0 && Double.compare(that.getTax(),
                this.tax) == 0 && this.priceFormula.equals(that.getPriceFormula());
    }

    /**
     * serializes a EnergySupplier
     * @return string representing a EnergySupplier
     */
    @Override
    public String toString() {
        return String.format("%s; baseValue: %f; tax: %f; priceFormula: '%s'",
                super.toString(), this.baseValue, this.tax, this.priceFormula);
    }

    /**
     * creates a copy of the energySupplier object
     * @return the copy
     */
    @Override
    public EnergySupplier clone(){
        return new EnergySupplier(this);
    }

    @Override
    public void copy(AbstractEntity ent) throws WrongEntityTypeException {
        if(ent.getClass() != this.getClass())
            throw new WrongEntityTypeException("Argument is not an energy supplier");

        EnergySupplier sup = (EnergySupplier)ent;
        super.copy(ent);
        this.baseValue = sup.getBaseValue();
        this.priceFormula = sup.getPriceFormula();
        this.tax = sup.getTax();
    }
}
