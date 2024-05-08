package cc.catman.object.core;


import cc.catman.object.core.accessor.ArrayObjectAccessor;
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
        assert objectAccessor.isSupport(iterable, 0);
    }

    @Test
    public void supportCollection() {
        List<Object> list = createList();
        Queue<Object> queue=new LinkedBlockingDeque<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.isSupport(queue, 0);
    }

    @Test
    public void supportArray() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.isSupport(array, 0);
    }

    @Test
    public void supportSet() {
        List<Object> list = createList();
        Set<Object> set = new HashSet<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.isSupport(set, 0);
    }

    @Test
    public void unSupportMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert !objectAccessor.isSupport(map, 0);
    }

    @Test
    public void unSupportNull() {
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert !objectAccessor.isSupport(null, 0);
    }

    @Test
    public void supportLongKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.isSupport(list, 0L);
    }

    @Test
    public void supportStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.isSupport(list, "0");
    }
    @Test
    public void unSupportStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert !objectAccessor.isSupport(list, "a");
    }

    @Test
    public void supportIntegerKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.isSupport(list, 0);
    }

    @Test
    public void unSupportLongKeyWhenValueGreaterThanMaxInt() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert !objectAccessor.isSupport(array, Integer.MAX_VALUE + 1L);
    }

    @Test
    public void unSupportLongKeyWhenValueLessThanMinInt() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert !objectAccessor.isSupport(array, Integer.MIN_VALUE - 1L);
    }

    @Test
    public void readFromArray() {
        Object[] array = createList().toArray(new Object[0]);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.get(array, 0).equals("a");
        assert objectAccessor.get(array, 4).equals(1);
        assert objectAccessor.get(array, 10).getClass().equals(Object.class);
        Object strArray = objectAccessor.get(array, 8);
        assert strArray instanceof String[];
        assert ((String[]) strArray)[0].equals("a");
    }

    @Test
    public void readFromCollection() {
        List<Object> list = createList();
        Queue<Object> queue=new LinkedBlockingDeque<>(list);
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.get(queue, 0).equals("a");
        assert objectAccessor.get(queue, 4).equals(1);
        assert objectAccessor.get(queue, 10).getClass().equals(Object.
                class);
        Object strArray = objectAccessor.get(queue, 8);
        assert strArray instanceof String[];
        assert ((String[]) strArray)[0].equals("a");
    }

    @Test
    public void readFromIterator() {
        Iterator<Object> iterable = createList().iterator();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.get(iterable, 0).equals("a");
        iterable = createList().iterator();
        assert objectAccessor.get(iterable, 4).equals(1);
        iterable = createList().iterator();
        assert objectAccessor.get(iterable, 10).getClass().equals(Object.class);
        iterable = createList().iterator();
        Object strArray = objectAccessor.get(iterable, 8);
        assert strArray instanceof String[];
        assert ((String[]) strArray)[0].equals("a");
    }

    @Test
    public void readFromSet() {
        Set<Object> set = new LinkedHashSet<>(createList());
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.get(set, 0).equals("a");
        assert objectAccessor.get(set, 4).equals(1);
        assert objectAccessor.get(set, 10).getClass().equals(Object.class);
        Object strArray = objectAccessor.get(set, 8);
        assert strArray instanceof String[];
        assert ((String[]) strArray)[0].equals("a");
    }

    @Test
    public void readWithLongKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.get(list, 0L).equals("a");
        assert objectAccessor.get(list, 4L).equals(1);
        assert objectAccessor.get(list, 10L).getClass().equals(Object.class);
        Object strArray = objectAccessor.get(list, 8L);
        assert strArray instanceof String[];
        assert ((String[]) strArray)[0].equals("a");
    }

    @Test
    public void readWithStringKey() {
        List<Object> list = createList();
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        assert objectAccessor.get(list, "0").equals("a");
        assert objectAccessor.get(list, "4").equals(1);
        assert objectAccessor.get(list, "10").getClass().equals(Object.class);
        Object strArray = objectAccessor.get(list, "8");
        assert strArray instanceof String[];
        assert ((String[]) strArray)[0].equals("a");
    }

    @Test
    public void readFromUnsupportedType() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        ArrayObjectAccessor objectAccessor = new ArrayObjectAccessor();
        // 匹配异常
            assert objectAccessor.get(map, 0)==null;
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