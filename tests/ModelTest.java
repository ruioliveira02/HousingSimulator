import com.housingsimulator.exceptions.InvalidFormulaException;
import com.housingsimulator.exceptions.NoHousesExistException;
import com.housingsimulator.model.*;
import com.housingsimulator.simulation.SetStateEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ModelTest {

    @Test
    public void timeTest() {
        Model m = new Model();
        try {
            m.setTime(24);
        }
        catch (InvalidFormulaException e) {
            Assert.fail();
        }
        Assert.assertEquals(24, m.getTime(), 0);
    }

    @Test
    public void consumptionTest()
    {
        Model m = new Model();
        Simulation s = new Simulation();

        EnergySupplier e = new EnergySupplier();
        s.addSupplier(e);

        SpeakerBrand b = new SpeakerBrand("JBL", 13, 2.0);
        s.addSpeakerBrand(b);

        SmartHouse h1 = new SmartHouse("h1", "gajo1", 12345, e.getId());
        h1.addDevice(new SmartBulb(true, "bulb1", 1, SmartBulb.LampColour.Neutral), "sala");
        h1.addDevice(new SmartBulb(false, "bulb2", 1.5f, SmartBulb.LampColour.Neutral), "cozinha");
        s.addHouse(h1);

        SmartHouse h2 = new SmartHouse("h2", "gajo2", 54321, e.getId());
        h2.addDevice(new SmartSpeaker(true, "speaker1", 5, "RFM", b), "sala");
        h2.addDevice(new SmartCamera(true, "camera1", new int[] { 1024, 2048 }, 1000000), "cozinha");
        s.addHouse(h2);

        m.addSimulatorEvent(new SetStateEvent(s, 0));

        try {
            m.setTime(24);
            List<SmartHouse> ans = m.orderHousesByEnergyConsumption(0, 24);

            Assert.assertEquals(h2.getId(), ans.get(0).getId());
            Assert.assertEquals(h1.getId(), ans.get(1).getId());
        }
        catch (InvalidFormulaException | NoHousesExistException ex) {
            Assert.fail();
        }
    }
}
