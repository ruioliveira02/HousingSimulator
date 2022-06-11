import com.housingsimulator.model.SpeakerBrand;
import org.junit.Assert;
import org.junit.Test;


public class SpeakerBrandTest {
    @Test
    public void constructorsTest() {
        SpeakerBrand b1 = new SpeakerBrand();
        SpeakerBrand b2 = new SpeakerBrand("Teste", 2, 2.0);
        SpeakerBrand b3 = new SpeakerBrand(b1);
        SpeakerBrand b4 = b1.clone();
        Assert.assertEquals(b1, b3);
        Assert.assertEquals(b1, b4);
        Assert.assertEquals(b2.getName(), "Teste");
        Assert.assertEquals(b2.getDailyConsumption(), 2);
    }

    @Test
    public void settersTest() {
        SpeakerBrand b1 = new SpeakerBrand();
        b1.setName("Ola");
        b1.setDailyConsumption(42);
        b1.setInstallationCost(2.0);
        Assert.assertEquals(b1.getName(), "Ola");
        Assert.assertEquals(b1.getDailyConsumption(), 42);
        Assert.assertEquals(b1.getInstallationPrice(), 2.0, 1e-6);
    }

    @Test
    public void toStringTest() {
        SpeakerBrand b2 = new SpeakerBrand("Teste", 2, 2.0 );
        Assert.assertEquals(b2.toString(), "name: 'Teste'; dailyConsumption: 2; installationPrice: 2.000000");
    }
}
