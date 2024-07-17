package cc.catman.object.core;


import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.ArrayObjectAccessor;
import cc.catman.object.core.accessor.property.wrapper.PropertyWrapperFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 数组对象访问器测试
 */
public class ArrayObjectAccessorTest {
    ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
    PropertyWrapperFactory wf = cfg.getWrapperFactory();
    @Test
    public void supportIterator() {
        Iterator<Object> iterable = createList().iterator();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(iterable), 0));
    }

    @Test
    public void supportCollection() {
        List<Object> list = createList();
        Queue<Object> queue = new LinkedBlockingDeque<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(queue), 0));
    }

    @Test
    public void supportArray() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(array), 0));
    }

    @Test
    public void supportSet() {
        List<Object> list = createList();
        Set<Object> set = new HashSet<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(set), 0));
    }

    @Test
    public void unSupportMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(map), 0));
    }

    @Test
    public void unSupportNull() {
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(null), 0));
    }

    @Test
    public void supportLongKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(list), 0L));
    }

    @Test
    public void supportStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(list), "0"));
    }

    @Test
    public void unSupportStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(list), "a"));
    }

    @Test
    public void supportIntegerKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(wf.create(list), 0));
    }

    @Test
    public void unSupportLongKeyWhenValueGreaterThanMaxInt() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(array), Integer.MAX_VALUE + 1L));
    }

    @Test
    public void unSupportLongKeyWhenValueLessThanMinInt() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(array), Integer.MIN_VALUE - 1L));
    }

    @Test
    public void readFromArray() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(wf.create(array), 0).read());
        Assert.assertEquals(1, objectAccessor.get(wf.create(array), 4).read());
        Assert.assertEquals(Object.class, objectAccessor.get(wf.create(array), 10).readType());
        Object strArray = objectAccessor.get(wf.create(array), 8).read();
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromCollection() {
        List<Object> list = createList();
        Queue<Object> queue = new LinkedBlockingDeque<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(wf.create(queue), 0).read());
        Assert.assertEquals(1, objectAccessor.get(wf.create(queue), 4).read());
        Assert.assertEquals(Object.class, objectAccessor.get(wf.create(queue), 10).readType());
        Object strArray = objectAccessor.get(wf.create(queue), 8).read();
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromIterator() {
        Iterator<Object> iterable = createList().iterator();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(wf.create(iterable), 0).read());
        iterable = createList().iterator();
        Assert.assertEquals(1, objectAccessor.get(wf.create(iterable), 4).read());
        iterable = createList().iterator();
        Assert.assertEquals(Object.class, objectAccessor.get(wf.create(iterable), 10).readType());
        iterable = createList().iterator();
        Object strArray = objectAccessor.get(wf.create(iterable), 8).read();
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromSet() {
        Set<Object> set = new LinkedHashSet<>(createList());
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(wf.create(set), 0).read());
        Assert.assertEquals(1, objectAccessor.get(wf.create(set), 4).read());
        Assert.assertEquals(Object.class, objectAccessor.get(wf.create(set), 10).readType());

        Object strArray = objectAccessor.get(wf.create(set), 8).read();
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readWithLongKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(wf.create(list), 0L).read());
        Assert.assertEquals(1, objectAccessor.get(wf.create(list), 4L).read());
        Assert.assertEquals(Object.class, objectAccessor.get(wf.create(list), 10L).readType());
        Object strArray = objectAccessor.get(wf.create(list), 8L).read();
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readWithStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(wf.create(list), "0").read());
        Assert.assertEquals(1, objectAccessor.get(wf.create(list), "4").read());
        Assert.assertEquals(Object.class, objectAccessor.get(wf.create(list), "10").readType());
        Object strArray = objectAccessor.get(wf.create(list), "8").read();
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromUnsupportedType() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        // 匹配异常
        Assert.assertNull(objectAccessor.get(wf.create(map), 0).read());
    }

    @Test
    public void readWithUnsupportedKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        // 匹配异常
        try {
            objectAccessor.get(wf.create(list), new Object());
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(wf.create(list), "a");
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(wf.create(list), 10.1);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(wf.create(list), 10.1f);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(wf.create(list), 10.1d);
            assert
                    false;
        } catch (Exception e) {
            assert true;
        }
        Assert.assertTrue(true);
    }

    public static List<Object> createList() {
        return Arrays.asList(
                "a", "b", "c", "d"
                , 1, 2, 3, 4
                , new String[]{"a", "b", "c", "d"}
                , new Integer[]{1, 2, 3, 4}
                , new Object()
        );
    }
}