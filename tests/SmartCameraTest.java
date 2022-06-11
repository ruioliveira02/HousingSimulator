import com.housingsimulator.exceptions.IllegalResolutionException;
import com.housingsimulator.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


public class SmartCameraTest {
    @Test
    public void constructorTest() {
        SmartCamera c1 = new SmartCamera(true, "Camera",  new int[]{1920, 1080}, 4000);
        SmartCamera c2 = new SmartCamera(c1);
        SmartCamera c3 = c1.clone();

        Assert.assertEquals(c1, c2);
        Assert.assertEquals(c2, c3);
        Assert.assertEquals(c1, c3);
        Assert.assertEquals(c2, c1);
        Assert.assertEquals(c3, c2);
        Assert.assertEquals(c3, c1);
    }

    @Test
    public void settersTest() {
        SmartCamera c1 = new SmartCamera(true, "Camera", new int[]{1920, 1080}, 4000);
        SmartCamera c2 = new SmartCamera();

        long fileResolution = 4000;
        int[] cameraResolution = new int[]{1920,1080};

        c2.setFileResolution(fileResolution);
        c2.setCameraResolution(cameraResolution);

        int[] newCameraResolution = c2.getCameraResolution();

        Assert.assertTrue(cameraResolution!=newCameraResolution && Arrays.equals(cameraResolution, newCameraResolution));
        Assert.assertArrayEquals(c2.getCameraResolution(), c1.getCameraResolution());
        Assert.assertEquals(c2.getFileResolution(), c1.getFileResolution());
    }

    @Test
    public void energyOutputTest() {
        SmartCamera c1 = new SmartCamera(true, "Camera", new int[]{1920, 1080}, 4000);
        Assert.assertEquals(0.0082944, c1.energyOutput(),  1e-6);

        c1.setOn(false);
        Assert.assertEquals(0.0, c1.energyOutput(),  1e-6);
    }

    @Test
    public void toStringTest() {
        SmartCamera c1 = new SmartCamera(true,"Camera", new int[]{1920, 1080}, 4000);
        Assert.assertEquals(c1.toString(), "id: " + c1.getId() + "; name: 'Camera'; on: true; cameraResolution: 1920x1080; fileResolution: 4000");
    }

    @Test
    public void exceptionTest() {
        int[] valid = {0,0};
        int[] invalid = {0,0,0};

        Assert.assertThrows(IllegalResolutionException.class,
                ()->{
                    SmartCamera c = new SmartCamera(true, "Camera",  invalid, 4000);
                });

        Assert.assertThrows(IllegalResolutionException.class,
                ()->{
                    SmartCamera c = new SmartCamera();
                    c.setCameraResolution(invalid);
                });
    }
}