package cc.catman.object.core.accessor;

import org.junit.Test;


/**
 * 基础对象访问器测试,默认会认为java.lang包下的类是基础对象
 * 同时枚举,Date,Time,TimeStamp也是基础对象
 */
public class BasicObjectAccessorTest {

    @Test
    public void supportJavaBuiltInBasicType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(1, 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport("1", 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(1L, 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(1.0, 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(1.0f, 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(true, 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport('a', 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void supportJavaLangType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(new String(), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Byte((byte) 1), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Short((short) 1), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Integer(1), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Long(1), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Float(1), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Double(1), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Boolean(true), 0,EAccessorKind.COVERT_TO_STRING);
        assert objectAccessor.isSupport(new Character('a'), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void supportEnum() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(TestEnum.A, 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void supportDate() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(new java.util.Date(), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void supportTime() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(new java.sql.Time(0), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void supportTimeStamp() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(new java.sql.Timestamp(0), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void unSupportIterator() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert !objectAccessor.isSupport(new java.util.ArrayList<>().iterator(), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void unSupportCollection() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert !objectAccessor.isSupport(new java.util.ArrayList<>(), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void unSupportMap() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert !objectAccessor.isSupport(new java.util.HashMap<>(), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void unSupportNull() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.isSupport(null, 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void unSupportCustomType() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert !objectAccessor.isSupport(new Object(), 0,EAccessorKind.COVERT_TO_STRING);
    }

    @Test
    public void unSupportGet() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.get(new Object(), 0);
            assert false;
        } catch (UnsupportedOperationException e) {
            assert true;
        }
    }

    @Test
    public void unSupportEachKey() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachKey(new Object(), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            assert true;
        }
    }

    @Test
    public void unSupportEachValue() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachValue(new Object(), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            assert true;
        }
    }

    @Test
    public void unSupportEachEntry() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        try {
            objectAccessor.eachEntry(new Object(), System.out::println);
            assert false;
        } catch (UnsupportedOperationException e) {
            assert true;
        }
    }

    @Test
    public void covertToString() {
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.covertToString(1).equals("1");
        assert objectAccessor.covertToString("1").equals("1");
        assert objectAccessor.covertToString(1L).equals("1");
        assert objectAccessor.covertToString(1.0).equals("1.0");
        assert objectAccessor.covertToString(1.0f).equals("1.0");
        assert objectAccessor.covertToString(true).equals("true");
        assert objectAccessor.covertToString('a').equals("a");
    }
    @Test
    public void covertToNumber(){
        BasicObjectAccessor objectAccessor = BasicObjectAccessor.defaultAccessor();
        assert objectAccessor.covertToNumber(1).equals(1);
        assert objectAccessor.covertToNumber("1").equals(1d);
        assert objectAccessor.covertToNumber(1L).equals(1L);
        assert objectAccessor.covertToNumber(1.0).equals(1.0);
        assert objectAccessor.covertToNumber(1.0f).equals(1.0f);
        assert objectAccessor.covertToNumber(true) == null;
        assert objectAccessor.covertToNumber('a')==null;
    }

    public enum TestEnum {
        A, B, C
    }
}