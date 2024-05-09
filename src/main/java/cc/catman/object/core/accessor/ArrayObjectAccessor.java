package cc.catman.object.core.accessor;


import cc.catman.object.core.Entity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数组对象访问器
 * 支持数组,集合,迭代器的访问
 *
 * @author jpanda
 * @since 2024-04-23
 */
@Slf4j
public class ArrayObjectAccessor implements ObjectAccessor {
    /**
     * 对象验证器
     */
    private final List<ObjectAccessorVerification> objectVerifications = new ArrayList<>();
    /**
     * key值验证器
     */
    private final List<ObjectAccessorVerification> keyVerifications = new ArrayList<>();

    public ArrayObjectAccessor() {
        addKeyVerifications();
        addObjectVerifications();
    }



    @Override
    public boolean isSupport(Object object, Object key,EAccessorKind kind) {
        if (kind==EAccessorKind.GET){
            return validaKey(object, key)&& validaObject(object);
        }
        return validaObject(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> covertToList(Object object) {
        if (object instanceof List){
            return (List<Object>) object;
        }
        if (object.getClass().isArray()){
            // 如果是基本类型数组,需要特殊处理,可以通过Arrays.asList方法转换
            Object[] objects = this.convertObjectArray(object);
            return Arrays.asList(objects);
        }
        if (object instanceof Object[]) {
            return Arrays.asList((Object[]) object);
        }
        if (object instanceof Collection) {
            return new ArrayList<>((Collection<?>) object);
        }
        if (object instanceof Iterator) {
            Iterator<?> iterator = (Iterator<?>) object;
            List<Object> list = new ArrayList<>();
            iterator.forEachRemaining(list::add);
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public Object filter(Object object,  Predicate<Object> filter) {
        List<Object> objects = this.covertToList(object);
        return objects.stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public Object get(Object object, Object key) {
        // 获取key值,key值一定是数字
        int index = getIndex(key);
        // 尝试获取元素集合,进行迭代
        Optional<Object> fromArray = readFromArray(object, index);
        if (fromArray.isPresent()) {
            return fromArray.get();
        }
        Optional<Object> fromCollection = readFromCollection(object, index);
        if (fromCollection.isPresent()) {
            return fromCollection.get();
        }
        Optional<Object> fromIterator = readFromIterator(object, index);
        return fromIterator.orElse(null);
    }

    @Override
    public void eachKey(Object object, Consumer<Object> consumer) {
        if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                consumer.accept(i);
            }
        } else if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            for (int i = 0; i < collection.size(); i++) {
                consumer.accept(i);
            }
        } else if (object instanceof Iterator) {
            Iterator<?> iterator = (Iterator<?>) object;
            int i = 0;
            while (iterator.hasNext()) {
                consumer.accept(i);
                i++;
            }
        }
    }

    /**
     * 遍历对象
     *
     * @param object   对象
     * @param consumer 消费者
     */
    @Override
    public void eachValue(Object object, Consumer<Object> consumer) {
        if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            for (Object o : array) {
                consumer.accept(o);
            }
        } else if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            collection.forEach(consumer);
        } else if (object instanceof Iterator) {
            Iterator<?> iterator = (Iterator<?>) object;
            iterator.forEachRemaining(consumer);
        }
    }

    @Override
    public void eachEntry(Object object, Consumer<Entity> consumer) {
        if (object instanceof Object[]){
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                consumer.accept(Entity.builder().index(i).value(array[i]).build());
            }
        }else if (object instanceof Collection){
            Collection<?> collection = (Collection<?>) object;
            int i = 0;
            for (Object o : collection) {
                consumer.accept(Entity.builder().index(i).value(o).build());
                i++;
            }
        }else if (object instanceof Iterator){
            Iterator<?> iterator = (Iterator<?>) object;
            int i = 0;
            while (iterator.hasNext()){
                consumer.accept(Entity.builder().index(i).value(iterator.next()).build());
                i++;
            }
        }
    }

    /**
     * 支持通过mapper函数进行映射
     * @param object 对象
     * @param mapper 映射函数
     * @return 映射后的对象
     */
    @Override
    public Object map(Object object, Function<Object, Object> mapper) {
        // 将对象转换为流
        Stream<?> stream = toStream(object);
        // 映射
        return stream.map(mapper).collect(Collectors.toList());
    }

    private void addCollectionVerifications() {
        objectVerifications.add((object, key) -> {
            if (object.getClass().isArray()){
                return ObjectAccessorVerificationResult.PASS;
            }
            if (Stream.of(Object[].class, Collection.class, Iterator.class).anyMatch(clazz -> clazz.isAssignableFrom(object.getClass()))) {
                return ObjectAccessorVerificationResult.PASS;
            }
            return ObjectAccessorVerificationResult.UNKNOWN;
        });
    }

    private void addObjectVerifications() {
        // 验证待访问对象是否支持访问
        addNullVerification();
        // 不能是map,应该落到其他访问器中
        addMapVerification();
        // 是否是数组,集合,迭代器
        addCollectionVerifications();
    }

    private void addMapVerification() {
        objectVerifications.add((object, key) -> {
            if (object instanceof Map) {
                return ObjectAccessorVerificationResult.FAIL;
            }
            return ObjectAccessorVerificationResult.UNKNOWN;
        });
    }

    private void addNullVerification() {
        objectVerifications.add((object, key) -> {
            if (object == null) {
                return ObjectAccessorVerificationResult.FAIL;
            }
            return ObjectAccessorVerificationResult.UNKNOWN;
        });
    }

    private void addKeyVerifications() {
        // 不支持null对象或key
        addNullKeyVerification();
        // 如果是数字key,直接通过验证
        addIntegerKeyVerification();
        // 如果是字符串key,尝试转换为数字
        addStringKeyVerification();
        // 添加数字key验证
        addNumberKeyVerification();
    }

    private void addStringKeyVerification() {
        keyVerifications.add((object, key) -> {
            if (!(key instanceof String)) {
                return ObjectAccessorVerificationResult.UNKNOWN;
            }
            // 尝试将其转换为数字
            try {
                tryConvertToInteger((String) key);
                return ObjectAccessorVerificationResult.PASS;
            } catch (IllegalArgumentException e) {
                return ObjectAccessorVerificationResult.FAIL;
            }
        });
    }

    private void addIntegerKeyVerification() {
        keyVerifications.add((object, key) -> {
            if (key instanceof Integer) {
                return ObjectAccessorVerificationResult.PASS;
            }
            return ObjectAccessorVerificationResult.UNKNOWN;
        });
    }

    private void addNullKeyVerification() {
        keyVerifications.add((object, key) -> {
            if (key == null) {
                return ObjectAccessorVerificationResult.FAIL;
            }
            return ObjectAccessorVerificationResult.UNKNOWN;
        });
    }

    private void addNumberKeyVerification() {
        // 如果是Number类型key,尝试转换为数字
        keyVerifications.add((object, key) -> {
            if (!(key instanceof Number)) {
                return ObjectAccessorVerificationResult.UNKNOWN;
            }
            double v =((Number) key).doubleValue();
            // 如果超出int范围,直接返回失败
            if (v < Integer.MIN_VALUE || v > Integer.MAX_VALUE) {
                return ObjectAccessorVerificationResult.FAIL;
            }
            // 如果包含小数部分,直接返回失败
            if (v % 1 != 0) {
                return ObjectAccessorVerificationResult.FAIL;
            }
            return ObjectAccessorVerificationResult.PASS;
        });
    }





    private boolean validaKey(Object object, Object key) {
        // 验证是否有一个验证器通过
        for (ObjectAccessorVerification verification : keyVerifications) {
            ObjectAccessorVerificationResult verify = verification.verify(object, key);
            if (verify == ObjectAccessorVerificationResult.PASS) {
                return true;
            }
            if (verify == ObjectAccessorVerificationResult.FAIL) {
                return false;
            }
        }
        // 此处是兜底,如果没有一个验证器通过,则返回false
        return false;
    }

    private boolean validaObject(Object object) {
        // 验证是否有一个验证器通过
        for (ObjectAccessorVerification verification : objectVerifications) {
            ObjectAccessorVerificationResult verify = verification.verify(object, null);
            if (verify == ObjectAccessorVerificationResult.PASS) {
                return true;
            }
            if (verify == ObjectAccessorVerificationResult.FAIL) {
                return false;
            }
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    protected Stream<Object> toStream(Object object) {
        if (object instanceof Object[]) {
            return Arrays.stream((Object[]) object);
        }
        if (object instanceof Collection) {
            return ((Collection<Object>) object).stream();
        }
        if (object instanceof Iterator) {
            Iterator<?> iterator = (Iterator<?>) object;
            return Stream.generate(() -> {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
                return null;
            });
        }
        return Stream.empty();
    }

    private Optional<Object> readFromArray(Object object, int index) {
        if (object.getClass().isArray()) {
           // 必须特殊处理,因为像int[]这种数组直接转换为Object[]会失败,但是可以通过Array.get方法获取
            if (object instanceof int[]){
                return Optional.ofNullable(Array.get(object,index));
            }
            return Optional.ofNullable(((Object[]) object)[index]);

        }
        if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            if (index >= 0 && index < array.length) {
                return Optional.ofNullable(array[index]);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private Optional<Object> readFromCollection(Object object, int index) {
        if (object instanceof Collection) {
            Collection<Object> collection = (Collection<Object>) object;
            if (index >= 0 && index < collection.size()) {
                return Optional.ofNullable(collection.toArray()[index]);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private Optional<Object> readFromIterator(Object object, int index) {
        if (object instanceof Iterator) {
            Iterator<Object> iterator = (Iterator<Object>) object;
            int i = 0;
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (i == index) {
                    return Optional.ofNullable(next);
                }
                i++;
            }
        }
        return Optional.empty();
    }


    /**
     * 获取索引
     *
     * @param key 可能是数字,也可能是字符串
     * @return 索引
     */
    private int getIndex(Object key) {
        if (key instanceof Integer) {
            return (Integer) key;
        }
        if (key instanceof String) {
            return tryConvertToInteger((String) key);
        }
        if (key instanceof Number) {
            double v = ((Number) key).doubleValue();
            // 如果超出int范围,直接返回失败
            if (v < Integer.MIN_VALUE || v > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("key is out of range, key:" + key);
            }
            // 如果包含小数部分,直接返回失败
            if (v % 1 != 0) {
                throw new IllegalArgumentException("key is not a integer, key:" + key);
            }
            return (int) v;
        }
        throw new IllegalArgumentException("key is not a number, key:" + key);
    }

    private Integer tryConvertToInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.trace("key is not a number:{}", value);
            throw new IllegalArgumentException("key is not a number, key:" + value);
        }
    }
    @SuppressWarnings({"java:S3776","java:S6541","java:S3012"})
    protected Object[] convertObjectArray(Object object) {
        if (object instanceof Object[]) {
            return (Object[]) object;
        }
        if(object.getClass().isArray()){
            // 如果是基本类型数组,需要特殊处理,直接转换为Object[]会导致失败
            if (object instanceof int[]){
                int[] array = (int[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof long[]){
                long[] array = (long[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof double[]){
                double[] array = (double[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof float[]){
                float[] array = (float[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof short[]){
                short[] array = (short[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof byte[]){
                byte[] array = (byte[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof char[]){
                char[] array = (char[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            if (object instanceof boolean[]){
                boolean[] array = (boolean[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
        }
        throw new IllegalArgumentException("object is not a array, object:" + object);
    }
}
