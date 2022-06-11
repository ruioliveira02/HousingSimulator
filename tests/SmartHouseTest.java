import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;


public class SmartHouseTest {
    @Test
    public void constructorTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(true, "ola", 1.0F, SmartBulb.LampColour.Warm);

        h1.addDevice(b,"sala");

        SmartHouse h2 = new SmartHouse(h1);
        SmartHouse h3 = h1.clone();
        Assert.assertEquals(h1, h2);
        Assert.assertEquals(h2, h3);
        Assert.assertEquals(h1, h3);
        Assert.assertEquals(h2, h1);
        Assert.assertEquals(h3, h2);
        Assert.assertEquals(h3, h1);
        Assert.assertEquals(h1,h1);
        Assert.assertNotEquals(h1,b);
    }

    @Test
    public void settersTest() {

        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        SmartHouse h1 = new SmartHouse();

        h1.setSupplierId(e1.getId());
        h1.setOwnerName("Rui");
        h1.setOwnerNif(123456789);


        Assert.assertEquals(h1.getSupplierId(), e1.getId());
        Assert.assertEquals(h1.getOwnerName(), "Rui");
        Assert.assertEquals(h1.getOwnerNif(), 123456789);
    }

    @Test
    public void switchAllTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartCamera c = new SmartCamera(false,"ola",new int[]{1920,1080},4096);
        SpeakerBrand brand = new SpeakerBrand("nome",10,5);
        SmartSpeaker s = new SmartSpeaker(true,"s1",10,"rfm",brand);

        h1.addDevice(b,"sala");
        h1.addDevice(c,"quarto");
        h1.addDevice(s,"cozinha");



        h1.switchAll(true);

        for(SmartDevice d : h1.getDevices().values()) {
            Assert.assertTrue(d.getOn());
        }
    }

    @Test
    public void containsDeviceTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartCamera c = new SmartCamera(false,"ola",new int[]{1920,1080},4096);
        SpeakerBrand brand = new SpeakerBrand("nome",10,5);
        SmartSpeaker s = new SmartSpeaker(true,"s1",10,"rfm",brand);

        h1.addDevice(b,"sala");
        h1.addDevice(c,"quarto");


        Assert.assertTrue(h1.containsDevice(b.getId()));
        Assert.assertTrue(h1.containsDevice(c.getId()));
        Assert.assertFalse(h1.containsDevice(s.getId()));
    }

    @Test
    public void removeDeviceTest() {

        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartCamera c = new SmartCamera(false,"ola",new int[]{1920,1080},4096);
        SpeakerBrand brand = new SpeakerBrand("nome",10,5);
        SmartSpeaker s = new SmartSpeaker(true,"s1",10,"rfm",brand);

        h1.addDevice(b,"sala");
        h1.addDevice(c,"quarto");
        h1.addDevice(s,"cozinha");

        h1.removeDevice(s);
        Assert.assertTrue(h1.containsDevice(b.getId()));
        Assert.assertTrue(h1.containsDevice(c.getId()));
        Assert.assertFalse(h1.containsDevice(s.getId()));
    }


    @Test
    public void roomTest() {

        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartCamera c = new SmartCamera(false,"ola",new int[]{1920,1080},4096);


        h1.addDevice(b,"sala");
        h1.addDevice(c,"sala");

        h1.moveToRoom(b.getId(), "S1");
        h1.moveToRoom(c.getId(), "S1");

        List<SmartDevice> devices = new ArrayList<>();
        devices.add(b);
        devices.add(c);

        Map<String, Set<SmartDevice>> rooms = new TreeMap<>();
        rooms.put("S1", new HashSet<>(devices));
        rooms.put("sala", new HashSet<>());

        Assert.assertEquals(rooms, h1.getDevicesByRoom());
    }

    @Test
    public void updateTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);

        h1.addDevice(b,"sala");
        b.setColour(SmartBulb.LampColour.Cold);
        h1.updateDevice(b.getId(), b);

        Assert.assertEquals(h1.getDevices().get(b.getId()), b);
    }



    @Test
    public void switchIdTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartCamera c = new SmartCamera(false,"ola",new int[]{1920,1080},4096);


        h1.addDevice(b,"sala");
        h1.addDevice(c,"sala");

        h1.switchById(b.getId(), true);

        Assert.assertTrue(h1.getDevices().get(b.getId()).getOn());
        Assert.assertFalse(h1.getDevices().get(c.getId()).getOn());
    }
    @Test
    public void switchIdFailureTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartCamera c = new SmartCamera(false,"ola",new int[]{1920,1080},4096);


        h1.addDevice(b,"sala");

        Assert.assertThrows(NoSuchEntityException.class,()->
            h1.switchById(c.getId(), true)
        );
    }








    @Test
    public void copyTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e1.getId());

        Map<Integer, SmartDevice> devices = new HashMap<>();
        SmartBulb b1 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b2 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b3 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);

        h1.addDevice(b1,"1");
        h1.addDevice(b2,"2");
        h1.addDevice(b3,"3");

        EnergySupplier e2 = new EnergySupplier("Galp", 1000000.0F, 100.0F, "10000");
        SmartHouse h2 = new SmartHouse("a1", "Rui", 548212037, e2.getId());

        Map<Integer, SmartDevice> devices2 = new HashMap<>();
        SmartBulb b4 = new SmartBulb(false, "ola", 2.0F, SmartBulb.LampColour.Cold);
        SmartBulb b5 = new SmartBulb(false, "ola", 2.0F, SmartBulb.LampColour.Cold);
        SmartBulb b6 = new SmartBulb(false, "ola", 2.0F, SmartBulb.LampColour.Cold);

        h1.addDevice(b4,"4");
        h1.addDevice(b5,"5");
        h1.addDevice(b6,"6");

        h1.copy(h2);

        Assert.assertNotEquals(h1, h2);
        Assert.assertEquals(h1.getDevices(), h2.getDevices());
        Assert.assertEquals(h1.getSupplierId(), h2.getSupplierId());
    }

    @Test
    public void switchRoomTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b1 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b2 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b3 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);


        h1.addDevice(b3,"super");
        h1.addDevice(b1,"max");
        h1.addDevice(b2,"max");

        h1.switchAllInRoom("max", true);

        Assert.assertTrue(h1.getDevices().get(b1.getId()).getOn());
        Assert.assertTrue(h1.getDevices().get(b2.getId()).getOn());
        Assert.assertFalse(h1.getDevices().get(b3.getId()).getOn());
    }

    @Test
    public void switchRoomFailureTest() {
        EnergySupplier e = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");

        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e.getId());


        SmartBulb b1 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b2 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b3 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);


        h1.addDevice(b3,"super");
        h1.addDevice(b1,"max");
        h1.addDevice(b2,"max");

        Assert.assertThrows(NoSuchEntityException.class,()->
                h1.switchAllInRoom("hamilton", true)
        );
    }



    @Test
    public void toStringTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e1.getId());
        Assert.assertEquals(h1.toString(), String.format("id: %d; name: 'a1'; owner name: 'Rui'; owner NIF: 548212037; supplier: %d; devices: {{}}; rooms: {{}}",
                h1.getId(),e1.getId()));
    }



    @Test
    public void compositionTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e1.getId());

        Map<Integer, SmartDevice> devices = new HashMap<>();
        SmartBulb b1 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);
        SmartBulb b2 = new SmartBulb(false, "ola", 1.0F, SmartBulb.LampColour.Warm);

        h1.addDevice(b1,"sala");
        h1.addDevice(b2,"sala");

        h1.switchAll(true);

        Assert.assertFalse(b1.getOn());
        Assert.assertFalse(b2.getOn());
        Assert.assertTrue(h1.getDevices().get(b1.getId()).getOn());
        Assert.assertTrue(h1.getDevices().get(b2.getId()).getOn());
    }

    @Test
    public void aggregationTest() {
        EnergySupplier e1 = new EnergySupplier("EDP", 1000000.0F, 100.0F, "10000");
        SmartHouse h1 = new SmartHouse("a1", "Rui", 548212037, e1.getId());
        e1.setName("GALP");

        Assert.assertEquals(e1.getId(), h1.getSupplierId());
    }



}