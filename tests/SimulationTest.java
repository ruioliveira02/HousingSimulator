import com.housingsimulator.exceptions.AlreadyExistingEntityException;
import com.housingsimulator.exceptions.InvalidEntityDeletionException;
import com.housingsimulator.exceptions.NoSuchEntityException;
import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimulationTest {

    @Test
    public void addTest() {
        Simulation sim = new Simulation();
        SmartHouse house = new SmartHouse();
        EnergySupplier supplier = new EnergySupplier();

        sim.addHouse(house);
        sim.addSupplier(supplier);

        Assert.assertEquals(sim.getEntity(house.getId()), house);
        Assert.assertEquals(sim.getEntity(supplier.getId()), supplier);
        Assert.assertNull(sim.getEntity(-1));

        //TODO:: Get all

    }

    @Test
    public void brandsTest() {
        SpeakerBrand b1 = new SpeakerBrand("JBL", 23, 5.0);
        SpeakerBrand b2 = new SpeakerBrand("Marshall", 42, 3.0);
        SpeakerBrand b3 = new SpeakerBrand("Bose", 12, 1.0);

        Set<SpeakerBrand> brandSet = new HashSet<>();
        brandSet.add(b1);
        brandSet.add(b2);
        brandSet.add(b3);

        Simulation simulation = new Simulation();
        simulation.addSpeakerBrand(b1);
        simulation.addSpeakerBrand(b2);
        simulation.addSpeakerBrand(b3);

        Assert.assertThrows(AlreadyExistingEntityException.class, () -> {
            simulation.addSpeakerBrand(b1);
        });

        Assert.assertEquals(brandSet, simulation.getAllSpeakerBrands());
        Assert.assertEquals(simulation.getSpeakerBrand(b1.getName()), b1);

        brandSet.remove(b1);
        b1.setName("JBL da Wish");

        simulation.updateSpeakerBrand("JBL", b1);
        Assert.assertEquals(simulation.getSpeakerBrand(b1.getName()), b1);

        simulation.deleteSpeakerBrand(b2.getName());
        brandSet.remove(b2);
        brandSet.add(b1);
        Assert.assertEquals(brandSet, simulation.getAllSpeakerBrands());

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            simulation.updateSpeakerBrand("Wish", b1);
        });

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            simulation.deleteSpeakerBrand("Wish");
        });
    }

    @Test
    public void updateTest() throws NoSuchEntityException, WrongEntityTypeException {
        Simulation sim = new Simulation();
        SmartHouse house = new SmartHouse();
        EnergySupplier supplier = new EnergySupplier();

        sim.addHouse(house);
        sim.addSupplier(supplier);

        house.setOwnerName("Maria");
        supplier.setName("EDP");

        Assert.assertNotEquals(sim.getEntity(house.getId()), house);
        Assert.assertNotEquals(sim.getEntity(supplier.getId()), supplier);

        sim.updateHouse(house.getId(),house);
        sim.updateSupplier(supplier.getId(), supplier);

        Assert.assertNotSame(sim.getEntity(house.getId()), house);
        Assert.assertNotSame(sim.getEntity(supplier.getId()), supplier);

        Assert.assertEquals(sim.getEntity(house.getId()), house);
        Assert.assertEquals(sim.getEntity(supplier.getId()), supplier);
    }

    @Test
    public void entitiesTest() {
        Simulation simulation = new Simulation();
        EnergySupplier s1 = new EnergySupplier("EDP", 32.0, 12.0, "100");
        EnergySupplier s2 = new EnergySupplier("Galp", 42.0, 2.0, "log(12)");

        simulation.addSupplier(s1);
        simulation.addSupplier(s2);

        Set<EnergySupplier> suppliers = new HashSet<>();
        suppliers.add(s1);
        suppliers.add(s2);

        Assert.assertEquals(suppliers, simulation.getSuppliers());
        Assert.assertEquals(suppliers, simulation.getEntityByType(EnergySupplier.class));
        Assert.assertEquals(s1, simulation.getEntityWithType(EnergySupplier.class, s1.getId()));
        Assert.assertTrue(simulation.getEntitiesByName("EDP").contains(s1));

        Assert.assertThrows(InvalidEntityDeletionException.class, () -> {
            simulation.deleteEntity(s1.getId());
        });

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            simulation.deleteEntity(-1);
        });
    }

    @Test
    public void housesTest() {
        Simulation simulation = new Simulation();
        EnergySupplier s1 = new EnergySupplier("EDP", 32.0, 12.0, "100");
        SmartHouse h1 = new SmartHouse();

        simulation.addHouse(h1);
        Assert.assertEquals(simulation.getHouses().size(), 1);
        Assert.assertTrue(simulation.getHouses().contains(h1));
        h1.setSupplierId(s1.getId());
        simulation.updateHouse(h1.getId(),h1);
        Assert.assertTrue(simulation.getHouses().contains(h1));
        simulation.deleteEntity(h1.getId());
        Assert.assertFalse(simulation.getHouses().contains(h1));
    }

    @Test
    public void copyConstructorTest() {
        Simulation sim = new Simulation();
        SmartHouse house = new SmartHouse();
        EnergySupplier supplier = new EnergySupplier();

        sim.addHouse(house);
        sim.addSupplier(supplier);

        Simulation sim2 = sim.clone();

        Assert.assertEquals(sim, sim2);
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals(new Simulation().toString(), "entities: {{}}");
    }

    @Test
    public void exceptionsTest() {
        Simulation sim = new Simulation();
        SmartHouse house = new SmartHouse();
        SmartHouse house2 = new SmartHouse();
        EnergySupplier supplier = new EnergySupplier();
        EnergySupplier supplier2 = new EnergySupplier();

        sim.addHouse(house);
        sim.addSupplier(supplier);

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            sim.updateHouse(house2.getId(),house2);
        });

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            sim.updateSupplier(supplier2.getId(), supplier2);
        });

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            sim.deleteHouse(-1);
        });

        Assert.assertThrows(WrongEntityTypeException.class, () -> {
            sim.deleteHouse(supplier.getId());
        });

        Assert.assertThrows(NoSuchEntityException.class, () -> {
            sim.updateSupplier(supplier2.getId(), supplier2);
        });
    }
}
