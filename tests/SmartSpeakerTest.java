import com.housingsimulator.exceptions.IllegalVolumeException;
import com.housingsimulator.model.SmartSpeaker;
import com.housingsimulator.model.SpeakerBrand;
import org.junit.Assert;
import org.junit.Test;


public class SmartSpeakerTest {
    @Test
    public void constructorsTest() {
        SpeakerBrand brand = new SpeakerBrand("Teste", 2, 4.0);
        SmartSpeaker s1 = new SmartSpeaker();
        SmartSpeaker s2 = new SmartSpeaker(brand);
        SmartSpeaker s3 = new SmartSpeaker("Ola", brand);
        SmartSpeaker s4 = new SmartSpeaker(true, "Ola", 42,"RUM", brand);
        SmartSpeaker s5 = new SmartSpeaker(s4);
        Assert.assertEquals(s4, s5);
        Assert.assertEquals(s3.getName(), "Ola");
        Assert.assertTrue(s4.getOn());
        Assert.assertEquals(s4.getVolume(), 42);
        Assert.assertEquals(s4.getRadioStation(), "RUM");
        Assert.assertEquals(s2.getBrand(), brand);
        Assert.assertEquals(s4, s4.clone());
    }

    @Test
    public void settersTest() {
        SpeakerBrand brand = new SpeakerBrand("Teste", 2, 4.0);
        SmartSpeaker s1 = new SmartSpeaker();
        s1.setBrand(brand);
        s1.setRadioStation("RUM");
        s1.setVolume(42);
        s1.setOn(true);
        Assert.assertEquals(s1.getRadioStation(), "RUM");
        Assert.assertEquals(s1.getBrand(), brand);
        Assert.assertEquals(s1.getVolume(), 42);
        Assert.assertTrue(s1.getOn());
    }

    @Test
    public void toStringTest() {
        SpeakerBrand brand = new SpeakerBrand("Teste", 2, 4.0);
        SmartSpeaker sp = new SmartSpeaker(true, "Ola", 42,"RUM", brand);
        Assert.assertEquals(sp.toString(), "id: " + sp.getId() + "; name: 'Ola'; on: true; "
                + "brand: {name: 'Teste'; dailyConsumption: 2; installationPrice: 4.000000}; volume: 42; station: 'RUM'");
    }

    @Test
    public void compositionTest() {
        SpeakerBrand brand = new SpeakerBrand("Teste", 2, 4.0);
        SmartSpeaker sp = new SmartSpeaker(true, "Ola", 42,"RUM", brand);
        sp.getBrand().setDailyConsumption(0);
        brand.setDailyConsumption(1);
        Assert.assertNotEquals(brand, sp.getBrand());
        Assert.assertNotEquals(sp.getBrand().getDailyConsumption(), 0);
    }

    @Test
    public void energyOutputTest() {
        SpeakerBrand brand = new SpeakerBrand("Teste", 43200, 5.0);
        SmartSpeaker sp = new SmartSpeaker(true, "Ola", 42,"RUM", brand);

        Assert.assertEquals(sp.energyOutput(), 756.0, 1e-6);

        sp.setOn(false);

        Assert.assertEquals(sp.energyOutput(), 0, 1e-6);
    }

    @Test
    public void exceptionTest() {
        SpeakerBrand brand = new SpeakerBrand("Teste", 2, 4.0);
        SmartSpeaker sp = new SmartSpeaker(true, "Ola", 42,"RUM", brand);

        Assert.assertThrows(IllegalVolumeException.class,
                () -> {
                    sp.setVolume(-1);
                });

        Assert.assertThrows(IllegalVolumeException.class,
                () -> {
                    SmartSpeaker sp1 = new SmartSpeaker(true, "Teste", 102, "RUM", brand);
                });

        Assert.assertThrows(IllegalVolumeException.class,
                () -> {
                    SmartSpeaker sp1 = new SmartSpeaker(true, "Teste", -1, "RUM", brand);
                });

        Assert.assertThrows(IllegalVolumeException.class,
                () -> {
                    sp.setVolume(120);
                });
    }
}
