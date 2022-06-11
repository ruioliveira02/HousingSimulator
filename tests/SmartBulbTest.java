import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import org.junit.Assert;
import org.junit.Test;


public class SmartBulbTest {
    @Test
    public void constructorTest() {
        SmartBulb b1 = new SmartBulb(true, "bulb", 1.2F, SmartBulb.LampColour.Neutral);
        SmartBulb b2 = new SmartBulb(b1);
        SmartBulb b3 = b1.clone();

        Assert.assertEquals(b1, b2);
        Assert.assertEquals(b2, b3);
        Assert.assertEquals(b1, b3);
        Assert.assertEquals(b2, b1);
        Assert.assertEquals(b3, b2);
        Assert.assertEquals(b3, b1);
    }

    @Test
    public void settersTest() {
        SmartBulb b1 = new SmartBulb();
        SmartBulb b2 = new SmartBulb(true, "bulb", 1.2F, SmartBulb.LampColour.Warm);

        b1.setColour(SmartBulb.LampColour.Warm);
        b1.setDimension(1.2F);

        Assert.assertEquals(b1.getDimension(), b2.getDimension(), 1e-6);
        Assert.assertEquals(b1.getColour(), b2.getColour());
    }

    @Test
    public void energyTest() {
        SmartBulb b2 = new SmartBulb(true, "bulb", 1.2F, SmartBulb.LampColour.Warm);
        Assert.assertEquals(3.6, b2.energyOutput(), 1e-6);

        b2.setColour(SmartBulb.LampColour.Neutral);
        Assert.assertEquals(2.4, b2.energyOutput(), 1e-6);

        b2.setColour(SmartBulb.LampColour.Cold);
        Assert.assertEquals(1.2, b2.energyOutput(), 1e-6);

        b2.setOn(false);
        Assert.assertEquals(0.0, b2.energyOutput(), 1e-6);
    }

    @Test
    public void toStringTest() {
        SmartBulb b2 = new SmartBulb(true, "bulb", 1.2F, SmartBulb.LampColour.Warm);
        Assert.assertEquals(b2.toString(), "id: " + b2.getId() + "; name: 'bulb'; on: true; colour: Warm; dimension: 1.200000");
    }

    @Test
    public void copyTest(){
        SmartBulb b2 = new SmartBulb(true, "bulb", 1.2F, SmartBulb.LampColour.Warm);
        SmartBulb b3 = new SmartBulb();
        b3.copy(b2);
        Assert.assertEquals(b2.getDimension(),b3.getDimension(),1e-6);
        Assert.assertEquals(b2.getName(),b3.getName());
        Assert.assertEquals(b2.getOn(),b3.getOn());
        Assert.assertEquals(b2.getColour(),b3.getColour());
    }

    @Test
    public void copyTestFailure(){
        SmartBulb b2 = new SmartBulb(true, "bulb", 1.2F, SmartBulb.LampColour.Warm);
        SmartSpeaker b3 = new SmartSpeaker();
        Assert.assertThrows(WrongEntityTypeException.class,()->b3.copy(b2));
    }





}
