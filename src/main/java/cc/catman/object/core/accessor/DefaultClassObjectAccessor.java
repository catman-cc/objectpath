package cc.catman.object.core.accessor;

import cc.catman.object.core.Entity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * 默认的对象访问器,其会基于反射的方式去访问对象的属性,这种方式是最通用的,但是也是最慢的
 * 为了提高性能,我们应该尽量避免使用该访问器,我们应该尽量使用更加高效的访问器,比如,针对特定的对象,提供特定的访问器
 * 但是,对于一些特殊的对象,比如Date,我们不应该去访问其属性,而是应该将其视为一个时间戳或者字符串
 * @author jpanda
 * @since 2021-04-23
 */
@Slf4j
public class DefaultClassObjectAccessor implements ObjectAccessor {

    /**
     * 读取方法查找器
     */
    private final ReadInvokeFinder finder;

    public DefaultClassObjectAccessor() {
        this(new StandardReadInvokeFinder());
    }

    public DefaultClassObjectAccessor(ReadInvokeFinder finder) {
        this.finder = finder;
    }

    @Override
    public boolean isSupport(Object object, Object key, EAccessorKind kind) {
        if (object == null) {
            return false;
        }
        if (kind != EAccessorKind.GET) {
            return true;
        }
        return null!=key;
    }

    @Override
    public Object get(Object object, Object key) {
        if (object == null) {
            return null;
        }
        if (key == null) {
            return null;
        }
        // 尝试寻找key对应的属性值,根据javabean的规范,我们将按照一下规则去寻找属性:
        // 1. get+key,如果存在该方法,则调用该方法
        // 2. is+key,如果存在该方法,则调用该方法
        // 3. key,如果存在该属性,则直接返回该属性
        // 4. 直接寻找对应key值的方法,如果存在,则调用该方法,但是不推荐这种方式
        // 5. 如果都不存在,则返回null
        Class<?> clazz = object.getClass();
        String keyString = key.toString();
        Invoke invoke =finder.find(clazz,keyString);
        if (invoke != null) {
            return invoke.invoke(object);
        }
        return null;
    }

    @Override
    public void eachValue(Object object, Consumer<Object> consumer) {
        // 变量当前对象中的所有属性,并且调用consumer
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            FieldInvoke i = FieldInvoke.of(f);
            Object value = i.invoke(object);
            consumer.accept(value);
        }
    }

    @Override
    public void eachEntry(Object object, Consumer<Entity> consumer) {
        // 变量当前对象中的所有属性,并且调用consumer
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            FieldInvoke i = FieldInvoke.of(f);
            Object value = i.invoke(object);
            consumer.accept(Entity.builder()
                    .key(f.getName())
                    .value(value)
                    .build());
        }
    }
}
