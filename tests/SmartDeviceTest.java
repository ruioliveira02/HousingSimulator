import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.EnergySupplier;
import com.housingsimulator.model.SmartDevice;
import org.junit.Assert;
import org.junit.Test;


public class SmartDeviceTest {
    private class TestDevice extends SmartDevice {
        public TestDevice() {
            super();
        }

        public TestDevice(TestDevice device) {
            super(device);
        }

        public TestDevice(String name) {
            super(name);
        }

        public TestDevice(boolean on, String name) {
            super(on, name);
        }

        @Override
        public double energyOutput() {
            return 2.0;
        }

        @Override
        public double installationPrice() {
            return 0;
        }

        public void copy(SmartDevice dev) {}

        //Dummy clone, no need for it to work
        public SmartDevice clone() {
            return this;
        }
    }
    @Test
    public void gettersTest() {
        TestDevice device = new TestDevice();
        Assert.assertFalse(device.getOn());
        device.setOn(true);
        Assert.assertTrue(device.getOn());

        Assert.assertNotEquals(device, null);
    }

    @Test
    public void constructorsTest() {
        SmartDevice device = new TestDevice("ola");
        SmartDevice device2 = new TestDevice(false, "ola");
        SmartDevice device3 = new TestDevice((TestDevice)device);
        Assert.assertEquals(device, device);
        Assert.assertNotEquals(null, device);
        Assert.assertEquals(device.getName(), device2.getName());
        Assert.assertEquals(device.getOn(), device2.getOn());
        Assert.assertEquals(device, device3);
    }

    @Test
    public void advanceByTest() {
        TestDevice t1 = new TestDevice("Ola");
        TestDevice t2 = new TestDevice(t1);


        Assert.assertEquals(t1, t2);


        Assert.assertEquals(t1, t2);


        Assert.assertEquals(t1, t2);
    }

    @Test
    public void toStringTest() {
        SmartDevice device = new TestDevice("ola");
        Assert.assertEquals(device.toString(), "id: " + device.getId() + "; name: 'ola'; on: false");
    }

    @Test
    public void exceptionTest() {
        SmartDevice device = new TestDevice("ola");
        Assert.assertThrows(WrongEntityTypeException.class, () -> {
            device.copy(new EnergySupplier());
        });
    }
}
