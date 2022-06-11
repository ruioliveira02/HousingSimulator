import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.model.*;
import org.junit.Assert;
import org.junit.Test;

public class AbstractEntityTest {
    private class TestEntity extends AbstractEntity {
        public TestEntity() {
            super();
        }
        public TestEntity(String name) {
            super(name);
        }
        public TestEntity(TestEntity testEntity) {
            super(testEntity);
        }

        public TestEntity clone() { return new TestEntity(this);} //Example clone
    }



    @Test
    public void nameTest() {
        AbstractEntity e1 = new TestEntity();
        AbstractEntity e2 = new TestEntity("New name");

        e1.setName("New name");

        Assert.assertEquals("New name", e1.getName());
        Assert.assertEquals("New name",e2.getName());
    }

    @Test
    public void idIncrement(){
        AbstractEntity e1 = new TestEntity();
        AbstractEntity e2 = new TestEntity();

        AbstractEntity e3 = new TestEntity();
        AbstractEntity e4 = new TestEntity("test");

        Assert.assertTrue(e1.getId()< e2.getId());
        Assert.assertTrue(e3.getId()< e4.getId());
    }




    @Test
    public void cloneTest() {
        AbstractEntity e1 = new TestEntity();

        Assert.assertEquals(e1.clone(), e1);
        Assert.assertEquals(new TestEntity((TestEntity) e1), e1);
        Assert.assertNotSame(e1, e1.clone());
    }


    @Test
    public void hashTest() {
        AbstractEntity e1 = new TestEntity();
        Assert.assertEquals(e1.hashCode(), e1.getId());
    }

    @Test
    public void copy() {
        AbstractEntity e1 = new TestEntity("Test1");
        AbstractEntity e2 = new TestEntity("Test2");

        SmartBulb b1 = new SmartBulb();

        e1.copy(e2);

        Assert.assertEquals(e1.getName(), e2.getName());
        Assert.assertNotEquals(e1.getId(), e2.getId());

        Assert.assertThrows(WrongEntityTypeException.class, () -> {
            e1.copy(b1);
        });
    }

    @Test
    public void toStringTest() {
        AbstractEntity e1 = new TestEntity();
        e1.setName("Test");

        Assert.assertEquals(e1.toString(),String.format("id: %d; name: 'Test'", e1.getId()));
    }

    @Test
    public void emptyConstructorTest() {
        AbstractEntity e1 = new TestEntity("ola");
        AbstractEntity e2 = new TestEntity();
        Assert.assertEquals(e2.getId(), e1.getId() + 1);
    }

    @Test
    public void idIncreasement(){
        AbstractEntity e = new TestEntity();
        AbstractEntity.increaseCurrentId(e.getId()+100);
        AbstractEntity e1 = new TestEntity();
        AbstractEntity.increaseCurrentId(e.getId()-100);
        AbstractEntity e2 = new TestEntity();
        Assert.assertEquals(e1.getId(),e.getId()+100);
        Assert.assertTrue(e2.getId()>e.getId());
    }


}
