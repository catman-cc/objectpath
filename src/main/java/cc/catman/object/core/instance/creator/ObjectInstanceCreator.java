package cc.catman.object.core.instance.creator;


import cc.catman.object.core.exception.ObjectCreatorRuntimeException;
import cc.catman.object.core.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 默认的实例化器,用于创建实例对象
 * @author jpanda
 * @since 0.0.3
 */
public class ObjectInstanceCreator implements InstanceCreator {

    private static final Logger log = LoggerFactory.getLogger(ObjectInstanceCreator.class);

    @Override
    public int score(Class<?> clazz, Object... args) {
        if (clazz.isPrimitive()){
            return 1;
        }
        int modifiers = clazz.getModifiers();
        return (Modifier.isAbstract(modifiers)||Modifier.isInterface(modifiers))
                ?-1
                :1;
    }

    @Override
    public <T> T create(Class<T> clazz, Object... args) {
        // 尝试查找默认构造方法来创建实例对象
        if (Objects.isNull(args) || args.length == 0){
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ObjectCreatorRuntimeException(e,clazz, args);
            }
        }
        return useConstructorInstanceObject(clazz, args);
    }

    private <T> T useConstructorInstanceObject(Class<T> clazz, Object[] args) {
        // 尝试查找带参数构造方法来创建实例对象
        Class<?>[] argTypes= Stream.of(args).map(Object::getClass).toArray(Class[]::new);
        try {
            Constructor<T> constructor = clazz.getConstructor(argTypes);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            // 无法找到带参数构造方法,只能尝试获取有可能可以使用的构造方法
            // 在进行参数类型匹配时,允许子类参数类型兼容父类参数类型
            return findConstructAndInstance(clazz, args, argTypes);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ObjectCreatorRuntimeException(e, clazz, args);
        }
    }

    private <T> T findConstructAndInstance(Class<T> clazz, Object[] args, Class<?>[] argTypes) {
        for (Constructor<?> clazzConstructor : clazz.getConstructors()) {
            T ins = tryInstance(clazz, args, argTypes, clazzConstructor);
            if (ins != null){ return ins;}
        }
        throw new ObjectCreatorRuntimeException("can not find constructor.", clazz, args);
    }

    @SuppressWarnings("unchecked")
    private <T> T tryInstance(Class<T> clazz, Object[] args, Class<?>[] argTypes, Constructor<?> clazzConstructor) {
        if (clazzConstructor.getParameterCount() == argTypes.length) {
            boolean match = true;
            for (int i = 0; i < argTypes.length; i++) {
                if (!ReflectionHelper.isAssignableFrom(clazzConstructor.getParameterTypes()[i], argTypes[i])) {
                    match=false;
                }
            }
            if (match) {
              // 找到匹配的构造方法,尝试创建实例对象
                try {
                    return (T) clazzConstructor.newInstance(args);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                    log.debug("Failed to create instance of {} with constructor {}.", clazz.getName(), clazzConstructor.getName(), ex);
                }
            }
        }
        return null;
    }

}
