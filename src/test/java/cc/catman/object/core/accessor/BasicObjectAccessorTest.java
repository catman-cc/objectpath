package cc.catman.object.core.accessor;

import org.junit.Assert;
import org.junit.Test;


/**
 * 基础对象访问器测试,默认会认为java.lang包下的类是基础对象
 * 同时枚举,Date,Time,TimeStamp也是基础对象
 */
public class BasicObjectAccessorTest {

    @Test
    public void supportJavaBuiltInBasicType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(1, 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport("1", 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(1L, 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(1.0, 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(1.0f, 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(true, 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport('a', 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    @SuppressWarnings("all")
    public void supportJavaLangType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(new String(), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Byte((byte) 1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Short((short) 1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Integer(1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Long(1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Float(1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Double(1), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Boolean(true), 0,EAccessorKind.COVERT_TO_STRING));
        Assert.assertTrue(objectAccessor.isSupport(new Character('a'), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportEnum() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(TestEnum.A, 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportDate() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(new java.util.Date(), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportTime() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(new java.sql.Time(0), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void supportTimeStamp() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(new java.sql.Timestamp(0), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    @SuppressWarnings("all")
    public void unSupportIterator() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(new java.util.ArrayList<>().iterator(), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportCollection() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(new java.util.ArrayList<>(), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportMap() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(new java.util.HashMap<>(), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportNull() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertTrue(objectAccessor.isSupport(null, 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportCustomType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertFalse(objectAccessor.isSupport(new Object(), 0,EAccessorKind.COVERT_TO_STRING));
    }

    @Test
    public void unSupportGet() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.get(new Object(), 0);
            assert false;
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void unSupportEachKey() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachKey(new Object(), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void unSupportEachValue() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachValue(new Object(), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void unSupportEachEntry() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachEntry(new Object(), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
        Assert.assertTrue(true);}
    }

    @Test
    public void covertToString() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertEquals("1", objectAccessor.covertToString(1));
        Assert.assertEquals("1", objectAccessor.covertToString("1"));
        Assert.assertEquals("1", objectAccessor.covertToString(1L));
        Assert.assertEquals("1.0", objectAccessor.covertToString(1.0));
        Assert.assertEquals("1.0", objectAccessor.covertToString(1.0f));
        Assert.assertEquals("true", objectAccessor.covertToString(true));
        Assert.assertEquals("a", objectAccessor.covertToString('a'));
    }
    @Test
    public void covertToNumber(){
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        Assert.assertEquals(1, objectAccessor.covertToNumber(1));
        Assert.assertEquals(1d, objectAccessor.covertToNumber("1"));
        Assert.assertEquals(1L, objectAccessor.covertToNumber(1L));
        Assert.assertEquals(1.0, objectAccessor.covertToNumber(1.0));
        Assert.assertEquals(1.0f, objectAccessor.covertToNumber(1.0f));
        Assert.assertNull(objectAccessor.covertToNumber(true));
        Assert.assertNull(objectAccessor.covertToNumber('a'));
    }

    public enum TestEnum {
        A, B, C
    }
}