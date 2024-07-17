package cc.catman.object.core.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.wrapper.PropertyWrapperFactory;
import org.junit.Assert;
import org.junit.Test;


/**
 * 基础对象访问器测试,默认会认为java.lang包下的类是基础对象
 * 同时枚举,Date,Time,TimeStamp也是基础对象
 */
public class BasicObjectAccessorTest {
    ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
    PropertyWrapperFactory wf = cfg.getWrapperFactory();
    @Test
    public void supportJavaBuiltInBasicType() {

        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create("1"), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(1L), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(1.0), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(1.0f), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(true), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create('a'), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    @SuppressWarnings("all")
    public void supportJavaLangType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new String()), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Byte((byte) 1)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Short((short) 1)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Integer(1)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Long(1)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Float(1)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Double(1)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Boolean(true)), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new Character('a')), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportEnum() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(TestEnum.A), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportDate() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new java.util.Date()), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportTime() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new java.sql.Time(0)), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportTimeStamp() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(new java.sql.Timestamp(0)), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    @SuppressWarnings("all")
    public void unSupportIterator() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(new java.util.ArrayList<>().iterator()), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportCollection() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create( new java.util.ArrayList<>()), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportMap() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(new java.util.HashMap<>()), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportNull() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(null), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportCustomType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(new Object()), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportGet() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.get(wf.create(new Object()), 0);
            assert false;
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void unSupportEachKey() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachKey(wf.create(new Object()), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void unSupportEachValue() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachValue(wf.create(new Object()), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void unSupportEachEntry() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachEntry(wf.create(new Object()), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
        Assert.assertTrue(true);}
    }

    @Test
    public void covertToString() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertEquals("1", objectAccessor.covertToString(wf.create(1)));
        Assert.assertEquals("1", objectAccessor.covertToString(wf.create("1")));
        Assert.assertEquals("1", objectAccessor.covertToString(wf.create(1L)));
        Assert.assertEquals("1.0", objectAccessor.covertToString(wf.create(1.0)));
        Assert.assertEquals("1.0", objectAccessor.covertToString(wf.create(1.0f)));
        Assert.assertEquals("true", objectAccessor.covertToString(wf.create(true)));
        Assert.assertEquals("a", objectAccessor.covertToString(wf.create('a')));
    }
    @Test
    public void covertToNumber(){
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertEquals(1, objectAccessor.covertToNumber(wf.create(1)));
        Assert.assertEquals(1d, objectAccessor.covertToNumber(wf.create("1")));
        Assert.assertEquals(1L, objectAccessor.covertToNumber(wf.create(1L)));
        Assert.assertEquals(1.0, objectAccessor.covertToNumber(wf.create(1.0)));
        Assert.assertEquals(1.0f, objectAccessor.covertToNumber(wf.create(1.0f)));
        Assert.assertNull(objectAccessor.covertToNumber(wf.create(true)));
        Assert.assertNull(objectAccessor.covertToNumber(wf.create('a')));
    }

    public enum TestEnum {
        A, B, C
    }
}