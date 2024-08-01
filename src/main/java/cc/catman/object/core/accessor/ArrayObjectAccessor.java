package cc.catman.object.core.accessor;


import cc.catman.object.core.Entity;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.util.ArrayHelper;
import cc.catman.object.core.util.PropertyWrapperHelper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 数组对象访问器
 * 支持数组,集合,迭代器的访问
 *
 * @author jpanda
 * @since 2024-04-23
 */
@Slf4j
public class ArrayObjectAccessor extends AbstractObjectAccessor {
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
    public boolean isSupport(PropertyWrapper object, Object key, EAccessorKind kind) {
        Object obj = object.read();
        if (kind == EAccessorKind.GET) {
            return validaKey(obj, key) && validaObject(obj);
        }
        return validaObject(obj);
    }

    @Override
    @SuppressWarnings({"unchecked","java:S2293"})
    public List<Object> covertToList(PropertyWrapper object) {
        if (object.isNull()) {
            return  configuration.isUseZeroForNull()
                    ?new ArrayList<>()
                    :null;

        }
        if (object.isInstanceOf(List.class)) {
            return object.read(List.class);
        }
        Class<?> ot = object.readType();
        if (Objects.nonNull(ot) && ot.isArray()) {
                // 如果是基本类型数组,需要特殊处理,可以通过Arrays.asList方法转换
                Object[] objects = this.convertObjectArray(object.read());
                return Arrays.asList(objects);
            }

        if (object.isInstanceOf(Object[].class)) {
            return Arrays.asList(object.read(Object[].class));
        }
        if (object.isInstanceOf(Collection.class)) {
            return new ArrayList<Object>(object.read(Collection.class));
        }
        if (object.isInstanceOf(Iterator.class)) {
            Iterator<?> iterator = object.read(Iterator.class);
            List<Object> list = new ArrayList<>();
            iterator.forEachRemaining(list::add);
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public PropertyWrapper filter(PropertyWrapper object, Predicate<Object> filter) {
        List<Object> objects = this.covertToList(object);
        if (object.isNull()){
            return null;
        }
        List<Object> res=new ArrayList<>();
        objects.forEach(o->{
            if (filter.test(o)){
                res.add(o);
            }
        });
        return object.wrapper(res);
    }

    @Override
    public PropertyWrapper get(PropertyWrapper pw, Object key) {
        if (pw.isNull()) {
            return pw.wrapper(null);
        }
        Object object=pw.read();
        // 获取key值,key值一定是数字
        int index = getIndex(key);
        // 尝试获取元素集合,进行迭代
        Optional<Object> fromArray = readFromArray(object, index);
        if (fromArray.isPresent()) {
            return pw.wrapper(fromArray.get());
        }
        Optional<Object> fromCollection = readFromCollection(object, index);
        if (fromCollection.isPresent()) {
            return pw.wrapper(fromCollection.get());
        }
        Optional<Object> fromIterator = readFromIterator(object, index);
        return pw.wrapper(fromIterator.orElse(null));
    }

    @Override
    public void eachKey(PropertyWrapper pw, Consumer<Object> consumer) {
        if (pw.isNull()) {
            return;
        }
        Object object=pw.read();
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
     * @param pw   对象
     * @param consumer 消费者
     */
    @Override
    public void eachValue(PropertyWrapper pw, Consumer<Object> consumer) {
        if (pw.isNull()) {
            return;
        }
        Object object=pw.read();
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
    public void eachEntry(PropertyWrapper pw, Consumer<Entity> consumer) {
        if ((pw.isNull())) {
            return;
        }
        Object object=pw.read();
        if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                consumer.accept(Entity.builder().index(i).value(array[i]).build());
            }
        } else if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            int i = 0;
            for (Object o : collection) {
                consumer.accept(Entity.builder().index(i).value(o).build());
                i++;
            }
        } else if (object instanceof Iterator) {
            Iterator<?> iterator = (Iterator<?>) object;
            int i = 0;
            while (iterator.hasNext()) {
                consumer.accept(Entity.builder().index(i).value(iterator.next()).build());
                i++;
            }
        }
    }

    /**
     * 支持通过mapper函数进行映射
     *
     * @param pw 对象
     * @param mapper 映射函数
     * @return 映射后的对象
     */
    @Override
    public PropertyWrapper map(PropertyWrapper pw, Function<Object, Object> mapper) {
        if (pw.isNull()) {
            return null;
        }
        Object object = pw.read();
        // 将对象转换为流
        int size = size(object);
        List<Object> list = IntStream.range(0, size)
                .mapToObj(pw::read)
                .map(mapper)
                .map(PropertyWrapperHelper::unWrapper)
                .collect(Collectors.toList());
        return pw.wrapper(list);
    }

    private void addCollectionVerifications() {
        objectVerifications.add((object, key) -> {
            if (object.getClass().isArray()) {
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
            double v = ((Number) key).doubleValue();
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

    private int size(Object o){
        if (Objects.isNull(o)){
            return 0;
        }
        if (o instanceof Object[]){
            return ((Object[]) o).length;
        }
        if (o instanceof Collection){
            return ((Collection<?>) o).size();
        }
        if (o instanceof Iterator){
            Iterator<?> iter = (Iterator<?>) o;
            int size=0;
            while (iter.hasNext()){
                iter.next();
                size++;
            }
            return size;
        }
        return 0;
    }
    @SuppressWarnings("unchecked")
    protected Stream<Object> toStream(Object object) {
        if (object == null) {
            return Stream.empty();
        }
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
        if (object == null) {
            return Optional.empty();
        }
        if (object.getClass().isArray()) {
            // 必须特殊处理,因为像int[]这种数组直接转换为Object[]会失败,但是可以通过Array.get方法获取
            if (object instanceof int[]) {
                return Optional.ofNullable(Array.get(object, index));
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
        if ((object == null)) {
            return Optional.empty();
        }
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
        if (object == null) {
            return Optional.empty();
        }
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

    @SuppressWarnings({"java:S3776", "java:S6541", "java:S3012"})
    protected Object[] convertObjectArray(Object object) {
        if ((object == null)) {
            return configuration.isUseZeroForNull()
                    ?new Object[]{}
                    :null;
        }

        if (object instanceof Object[]) {
            return (Object[]) object;
        }
        if (object.getClass().isArray()) {
            // 如果是基本类型数组,需要特殊处理,直接转换为Object[]会导致失败
            if (object instanceof int[]) {
                int[] array = (int[]) object;
                Object[] objects = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    objects[i] = array[i];
                }
                return objects;
            }
            Object[] objects = ArrayHelper.convertToArray(object);
            if (objects != null){ return objects;}
        }
        throw new IllegalArgumentException("object is not a array, object:" + object);
    }


}
