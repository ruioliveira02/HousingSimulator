import com.housingsimulator.exceptions.IllegalSupplierValueException;
import com.housingsimulator.exceptions.InvalidFormulaException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.exceptions.WrongSupplierException;
import com.housingsimulator.model.*;
import com.udojava.evalex.Expression;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class EnergySupplierTest {
    @Test
    public void constructorTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10 * log10(10) * x");
        EnergySupplier e2 = new EnergySupplier(e1);
        EnergySupplier e3 = e1.clone();
        Assert.assertEquals(e1, e2);
        Assert.assertEquals(e2, e3);
        Assert.assertEquals(e1, e3);
        Assert.assertEquals(e2, e1);
        Assert.assertEquals(e3, e2);
        Assert.assertEquals(e3, e1);
    }

    @Test
    public void settersTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        EnergySupplier e2 = new EnergySupplier();

        e2.setBaseValue(1000000.0F);
        e2.setTax(100.0F);
        e2.setPriceFormula("10000");


        Assert.assertEquals(e1.getBaseValue(), e2.getBaseValue(), 1e-6);
        Assert.assertEquals(e1.getTax(), e2.getTax(), 1e-6);
        Assert.assertEquals(e1.getPriceFormula(), e2.getPriceFormula());

    }

    @Test
    public void toStringTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        Assert.assertEquals(e1.toString(), "id: " + e1.getId() + "; name: 'EDP'; baseValue: 1000000.000000; tax: " +
                "100.000000; priceFormula: '10000'");
    }

    @Test
    public void copyTest() {
        EnergySupplier s1 = new EnergySupplier("EDP", 10.0f, 10.0f, "sqrt(27)");
        EnergySupplier s2 = s1.clone();

        s2.setTax(100.0f);

        s1.copy(s2);

        Assert.assertEquals(s1, s2);
        Assert.assertNotSame(s1, s2);
    }

    @Test
    public void formulaTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10 * log10(10) * x");
        Map<String, Double> values = new HashMap<>();
        values.put("x", 10.0);
        try {
            Assert.assertEquals(100.0, e1.getPrice(values), 1e-6);
        }
        catch (InvalidFormulaException e) {
            Assert.fail();
        }
    }



    @Test
    public void exceptionTest() {
        Assert.assertThrows(IllegalSupplierValueException.class,
                ()->{
                    EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, -100.0F, "10000");
                });

        Assert.assertThrows(IllegalSupplierValueException.class,
                ()->{
                    EnergySupplier e1 = new EnergySupplier("EDP", -1000000.0F, 100.0F, "10000");
                });

        Assert.assertThrows(InvalidFormulaException.class,
                () -> {
                    EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10 * log10(10) * x");
                    Map<String, Double> values = new HashMap<>();
                    values.put("y", 10.0);
                    Assert.assertEquals(100.0, e1.getPrice(values), 1e-6);
                });

        Assert.assertThrows(WrongEntityTypeException.class,
                () -> {
                    EnergySupplier s1 = new EnergySupplier();
                    SmartHouse h1 = new SmartHouse();
                    s1.copy(h1);
                });
    }


   @Test
    public void billing() {
       EnergySupplier e1 = new EnergySupplier("EDP", 3.0F, 10.0F, "Consumption+NoDevices+NoRooms+Base+Tax");
       SmartHouse h = new SmartHouse("house", "owner", 69, e1.getId());
       SmartBulb b = new SmartBulb(true, "bulb", 6.5F, SmartBulb.LampColour.Neutral);
       h.addDevice(b, "room");
       try {
           Receipt r = e1.billHouse(h, 0, 10);
           Assert.assertEquals(r.getSupplier(), e1);
           double consumption = h.getDevicesConsumptions().values().stream().mapToDouble(Double::doubleValue).sum();
           double devices = h.getDevices().size();
           double installation = 6.5F * 10;
           double rooms = h.getRooms().size();
           double base = 3.0F;
           double tax = 10.0F;
           Assert.assertEquals(r.getPrice(), consumption + devices + rooms + base + tax + installation, 1e-6);
       } catch (InvalidFormulaException e) {
           Assert.assertNotEquals(0, 0);
       }
   }


       @Test
       public void billingTrowsInvalidFormula(){
           EnergySupplier e = new EnergySupplier("EDP", 3.0F, 10.0F, "Consumption+NoDevices+NoRooms+Base+Tax+error");

           SmartHouse h = new SmartHouse("house","owner",69,e.getId());


           SmartBulb b = new SmartBulb(true,"bulb",6.5F, SmartBulb.LampColour.Neutral);
           h.addDevice(b,"room");

           Assert.assertThrows(InvalidFormulaException.class,()-> {
               Receipt r = e.billHouse(h, 0, 10);
           });
           }

    @Test
    public void billingTrowsWrongSupplier(){
        EnergySupplier e1 = new EnergySupplier("EDP", 3.0F, 10.0F, "Consumption+NoDevices+NoRooms+Base+Tax+error");
        EnergySupplier e2 = new EnergySupplier("EDP", 3.0F, 10.0F, "Consumption+NoDevices+NoRooms+Base+Tax");

        SmartHouse h = new SmartHouse("house","owner",69,e2.getId());

        SmartBulb b = new SmartBulb(true,"bulb",6.5F, SmartBulb.LampColour.Neutral);
        h.addDevice(b,"room");

        Assert.assertThrows(WrongSupplierException.class,()-> {
            Receipt r = e1.billHouse(h, 0, 10);
        });
    }
























}
