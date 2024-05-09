package cc.catman.object.core;


import cc.catman.object.core.accessor.ArrayObjectAccessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 数组对象访问器测试
 */
public class ArrayObjectAccessorTest {

    @Test
    public void supportIterator() {
        Iterator<Object> iterable = createList().iterator();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(iterable, 0));
    }

    @Test
    public void supportCollection() {
        List<Object> list = createList();
        Queue<Object> queue = new LinkedBlockingDeque<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(queue, 0));
    }

    @Test
    public void supportArray() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(array, 0));
    }

    @Test
    public void supportSet() {
        List<Object> list = createList();
        Set<Object> set = new HashSet<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(set, 0));
    }

    @Test
    public void unSupportMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(map, 0));
    }

    @Test
    public void unSupportNull() {
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(null, 0));
    }

    @Test
    public void supportLongKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(list, 0L));
    }

    @Test
    public void supportStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(list, "0"));
    }

    @Test
    public void unSupportStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(list, "a"));
    }

    @Test
    public void supportIntegerKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(list, 0));
    }

    @Test
    public void unSupportLongKeyWhenValueGreaterThanMaxInt() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(array, Integer.MAX_VALUE + 1L));
    }

    @Test
    public void unSupportLongKeyWhenValueLessThanMinInt() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(array, Integer.MIN_VALUE - 1L));
    }

    @Test
    public void readFromArray() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(array, 0));
        Assert.assertEquals(1, objectAccessor.get(array, 4));
        Assert.assertEquals(Object.class, objectAccessor.get(array, 10).getClass());
        Object strArray = objectAccessor.get(array, 8);
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromCollection() {
        List<Object> list = createList();
        Queue<Object> queue = new LinkedBlockingDeque<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(queue, 0));
        Assert.assertEquals(1, objectAccessor.get(queue, 4));
        Assert.assertEquals(Object.class, objectAccessor.get(queue, 10).getClass());
        Object strArray = objectAccessor.get(queue, 8);
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromIterator() {
        Iterator<Object> iterable = createList().iterator();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(iterable, 0));
        iterable = createList().iterator();
        Assert.assertEquals(1, objectAccessor.get(iterable, 4));
        iterable = createList().iterator();
        Assert.assertEquals(Object.class, objectAccessor.get(iterable, 10).getClass());
        iterable = createList().iterator();
        Object strArray = objectAccessor.get(iterable, 8);
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromSet() {
        Set<Object> set = new LinkedHashSet<>(createList());
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(set, 0));
        Assert.assertEquals(1, objectAccessor.get(set, 4));
        Assert.assertEquals(Object.class, objectAccessor.get(set, 10).getClass());

        Object strArray = objectAccessor.get(set, 8);
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readWithLongKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(list, 0L));
        Assert.assertEquals(1, objectAccessor.get(list, 4L));
        Assert.assertEquals(Object.class, objectAccessor.get(list, 10L).getClass());
        Object strArray = objectAccessor.get(list, 8L);
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readWithStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(list, "0"));
        Assert.assertEquals(1, objectAccessor.get(list, "4"));
        Assert.assertEquals(Object.class, objectAccessor.get(list, "10").getClass());
        Object strArray = objectAccessor.get(list, "8");
        Assert.assertTrue(strArray instanceof String[]);
        Assert.assertEquals("a", ((String[]) strArray)[0]);
    }

    @Test
    public void readFromUnsupportedType() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        // 匹配异常
        Assert.assertNull(objectAccessor.get(map, 0));
    }

    @Test
    public void readWithUnsupportedKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        // 匹配异常
        try {
            objectAccessor.get(list, new Object());
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(list, "a");
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(list, 10.1);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(list, 10.1f);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        // 匹配异常
        try {
            objectAccessor.get(list, 10.1d);
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