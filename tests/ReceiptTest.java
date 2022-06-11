import com.housingsimulator.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ReceiptTest {
    @Test
    public void EmptyConstructorTest() {

        Receipt r = new Receipt();
        Assert.assertEquals(r.getPrice(), 0.0F, 1e-6);
        Assert.assertEquals(r.getStartTime(), 0.0F, 1e-6);
        Assert.assertEquals(r.getEndTime(), 0.0F, 1e-6);
        Assert.assertEquals(r.getInstallations().size(), 0);
        Assert.assertEquals(r.getConsumptions().size(), 0);

    }
    @Test
    public void constructorTest(){

        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h = new SmartHouse("a1", "Rui", 548212037, e.getId());

        List<Installation> installations = new ArrayList<>();
        installations.add(new Installation("d1","sala",10.0));
        installations.add(new Installation("d2","quarto",20.0));

        Map<String, Double> consumptions = new HashMap<>();
        consumptions.put("d1",25.0);
        consumptions.put("d2",25.0);


        Receipt r = new  Receipt(0.0F,10.0F,e,h,installations,consumptions,40);
        Assert.assertEquals(r.getStartTime(),0.0F,1e-6);
        Assert.assertEquals(r.getEndTime(),10.0F,1e-6);
        Assert.assertEquals(r.getSupplier(),e);
        Assert.assertEquals(r.getCustomer(),h);
        Assert.assertEquals(r.getInstallations(),installations);
        Assert.assertEquals(r.getConsumptions(),consumptions);
        Assert.assertEquals(r.getPrice(),40.0,1e-6);
        Assert.assertNotSame(r.getSupplier(),e);
        Assert.assertNotSame(r.getCustomer(),h);
        Assert.assertNotSame(r.getInstallations(),installations);
        Assert.assertNotSame(r.getConsumptions(),consumptions);

    }
    @Test
    public void copyConstructorTest(){
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h = new SmartHouse("a1", "Rui", 548212037, e.getId());

        List<Installation> installations = new ArrayList<>();
        installations.add(new Installation("d1","sala",10.0));
        installations.add(new Installation("d2","quarto",20.0));

        Map<String, Double> consumptions = new HashMap<>();
        consumptions.put("d1",25.0);
        consumptions.put("d2",25.0);


        Receipt r1 = new  Receipt(0.0F,10.0F,e,h,installations,consumptions,40);
        Receipt r2 = new Receipt(r1);
        Assert.assertEquals(r2.getStartTime(),r1.getStartTime(),1e-6);
        Assert.assertEquals(r2.getEndTime(),r1.getEndTime(),1e-6);
        Assert.assertEquals(r2.getSupplier(),r1.getSupplier());
        Assert.assertEquals(r2.getCustomer(),r1.getCustomer());
        Assert.assertEquals(r2.getInstallations(),r1.getInstallations());
        Assert.assertEquals(r2.getConsumptions(),r1.getConsumptions());
        Assert.assertEquals(r2.getPrice(),r1.getPrice(),1e-6);
        Assert.assertNotSame(r2.getSupplier(),r1.getSupplier());
        Assert.assertNotSame(r2.getCustomer(),r1.getCustomer());
        Assert.assertNotSame(r2.getInstallations(),r1.getInstallations());
        Assert.assertNotSame(r2.getConsumptions(),r1.getConsumptions());

    }

    @Test
    public void cloneTest(){
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h = new SmartHouse("a1", "Rui", 548212037, e.getId());

        List<Installation> installations = new ArrayList<>();
        installations.add(new Installation("d1","sala",10.0));
        installations.add(new Installation("d2","quarto",20.0));

        Map<String, Double> consumptions = new HashMap<>();
        consumptions.put("d1",25.0);
        consumptions.put("d2",25.0);


        Receipt r1 = new  Receipt(0.0F,10.0F,e,h,installations,consumptions,40);
        Receipt r2 = r1.clone();
        Assert.assertEquals(r2.getStartTime(),r1.getStartTime(),1e-6);
        Assert.assertEquals(r2.getEndTime(),r1.getEndTime(),1e-6);
        Assert.assertEquals(r2.getSupplier(),r1.getSupplier());
        Assert.assertEquals(r2.getCustomer(),r1.getCustomer());
        Assert.assertEquals(r2.getInstallations(),r1.getInstallations());
        Assert.assertEquals(r2.getConsumptions(),r1.getConsumptions());
        Assert.assertEquals(r2.getPrice(),r1.getPrice(),1e-6);
        Assert.assertNotSame(r2.getSupplier(),r1.getSupplier());
        Assert.assertNotSame(r2.getCustomer(),r1.getCustomer());
        Assert.assertNotSame(r2.getInstallations(),r1.getInstallations());
        Assert.assertNotSame(r2.getConsumptions(),r1.getConsumptions());

    }







    @Test
    public void settersTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h = new SmartHouse("a1", "Rui", 548212037, e.getId());

        List<Installation> installations = new ArrayList<>();
        installations.add(new Installation("d1","sala",10.0));
        installations.add(new Installation("d2","quarto",20.0));

        Map<String, Double> consumptions = new HashMap<>();
        consumptions.put("d1",25.0);
        consumptions.put("d2",25.0);

        Receipt r1 = new Receipt();
        r1.setCustomer(h);
        r1.setSupplier(e);
        r1.setStartTime(42.0);
        r1.setEndTime(45.0);
        r1.setPrice(55.0);
        r1.setInstallations(installations);
        r1.setConsumptions(consumptions);
        Assert.assertEquals(r1.getCustomer(),h);
        Assert.assertEquals(r1.getStartTime(), 42.0, 1e-6);
        Assert.assertEquals(r1.getEndTime(), 45.0, 1e-6);
        Assert.assertEquals(r1.getPrice(), 55.0, 1e-6);
        Assert.assertEquals(r1.getSupplier(),e);
        Assert.assertNotSame(r1.getCustomer(), h);
        Assert.assertNotSame(r1.getSupplier(),e);
        Assert.assertNotSame(r1.getConsumptions(), consumptions);
        Assert.assertNotSame(r1.getInstallations(),installations);
    }

    @Test
    public void toStringTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h = new SmartHouse("a1", "Rui", 548212037, e.getId());

        List<Installation> installations = new ArrayList<>();
        installations.add(new Installation("d1","sala",10.0));
        installations.add(new Installation("d2","quarto",20.0));

        Map<String, Double> consumptions = new HashMap<>();
        consumptions.put("d1",25.0);
        consumptions.put("d2",25.0);


        Receipt r1 = new  Receipt(0.0F,10.0F,e,h,installations,consumptions,40);

        Assert.assertEquals(r1.toString(),"supplier: {id: "+e.getId()+"; name: 'EDP'; baseValue: 1000000.000000; tax: 100.000000; priceFormula: '10000'}; customer: {id: "+h.getId()+"; name: 'a1'; owner name: 'Rui'; owner NIF: 548212037; supplier: "+e.getId()+"; devices: {{}}; rooms: {{}}}; time-frame: [0.000000-10.000000]; price: 40.000000;\n" +
                "Consumptions:\n" +
                "     Device:Consumption\n" +
                "      d1 : 25.0\n" +
                "      d2 : 25.0\n" +
                "Installations:\n" +
                "     Device:Room:Price\n" +
                "      d1 : sala : 10.0\n" +
                "      d2 : quarto : 20.0\n" +
                "\n");
    }


    @Test
    public void equalTest(){
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h = new SmartHouse("a1", "Rui", 548212037, e.getId());

        List<Installation> installations = new ArrayList<>();
        installations.add(new Installation("d1","sala",10.0));
        installations.add(new Installation("d2","quarto",20.0));

        Map<String, Double> consumptions = new HashMap<>();
        consumptions.put("d1",25.0);
        consumptions.put("d2",25.0);


        Receipt r1 = new  Receipt(0.0F,10.0F,e,h,installations,consumptions,40);
        Receipt r2 = r1.clone();
        Assert.assertTrue(r2.equals(r1));

    }


}
