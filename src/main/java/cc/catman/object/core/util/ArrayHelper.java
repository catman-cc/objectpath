package cc.catman.object.core.util;

import java.util.Objects;

/**
 * 数组工具类
 * @author jpanda
 * @since 0.0.3
 */
public class ArrayHelper {
    private ArrayHelper() {
    }

    /**
     * 获取数组指定位置的值
     * @param object 数组对象
     * @param index 索引
     * @return 值
     * @param <T> 泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object object, int index) {
        if (index<0){
            return null;
        }
        Object[] array = toArray(object);
        if (Objects.isNull(array)) {
            return null;
        }
        if (index >= array.length) {
            return null;
        }
        return (T) array[index];
    }

    public static boolean isArray(Class<?> clazz){
        return clazz.isArray();
    }

    public static boolean isArray(Object object){
        return object.getClass().isArray();
    }

    public static boolean set(Object object, int index, Object value) {
        if (Objects.isNull(object)){
            return false;
        }
        try {
            if (object instanceof Object[]) {
                ((Object[]) object)[index] = value;
            } else if (object instanceof int[]) {
                ((int[]) object)[index] = (int) value;
            } else if (object instanceof long[]) {
                ((long[]) object)[index] = (long) value;
            } else if (object instanceof double[]) {
                ((double[]) object)[index] = (double) value;
            } else if (object instanceof float[]) {
                ((float[]) object)[index] = (float) value;
            }else if (object instanceof short[]) {
                ((short[]) object)[index] = (short) value;
            } else if (object instanceof byte[]) {
                ((byte[]) object)[index] = (byte) value;
            } else if (object instanceof char[]) {
                ((char[]) object)[index] = (char) value;
            } else if (object instanceof boolean[]) {
                ((boolean[]) object)[index] = (boolean) value;
            }
            return true;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }
    public static String toString(Object object,String split) {
        Object[] array = toArray(object);
        if (Objects.isNull(array)){
            return "null";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                sb.append(split);
            }
            sb.append(array[i]);
        }
        return sb.append("]").toString();
    }
    @SuppressWarnings({"java:S3776","java:S3012"})
    public static Object[] toArray(Object object) {
        if (object == null) {
            return new Object[0];
        }
        if (object instanceof Object[]) {
            return (Object[]) object;
        }
        if (object.getClass().isArray() && object instanceof int[]) {
                int[] array = (int[]) object;
                Object[] arr = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    arr[i] = ((int[]) object)[i];
                }
                return arr;
            }

        return convertToArray(object);
    }

    @SuppressWarnings({"java:S3776","java:S3012","java:S1168"})
    public static Object[] convertToArray(Object object) {
        if (object instanceof long[]) {
            long[] array = (long[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        if (object instanceof double[]) {
            double[] array = (double[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        if (object instanceof float[]) {
            float[] array = (float[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        if (object instanceof short[]) {
            short[] array = (short[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        if (object instanceof byte[]) {
            byte[] array = (byte[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        if (object instanceof char[]) {
            char[] array = (char[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        if (object instanceof boolean[]) {
            boolean[] array = (boolean[]) object;
            Object[] objects = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                objects[i] = array[i];
            }
            return objects;
        }
        return null;
    }

}
