package cc.catman.object.core.accessor.invoke;

import cc.catman.object.core.exception.ReflectionRuntimeException;

import java.util.*;
import java.util.function.Supplier;

/**
 * 一个可缓存的执行器查找工具
 *
 * @author jpanda
 * @since 0.0.2
 */
public class CachedReadInvokeFinder implements ReadInvokeFinder {
    /**
     * 一个简单的缓存
     */
    private final Map<Class<?>, Map<String, Invoke>> cache = new WeakHashMap<>();
    /**
     * 委托查找器
     */
    private final ReadInvokeFinder delegate;

    public CachedReadInvokeFinder() {
        this(new StandardReadInvokeFinder());
    }

    public CachedReadInvokeFinder(ReadInvokeFinder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Invoke find(Class<?> clazz, String name) {
        Map<String, Invoke> invokeCache = cache.computeIfAbsent(clazz, k -> new WeakHashMap<>());
        Invoke invoke = invokeCache.computeIfAbsent(name, k -> JavaBeanInvokeFinder.findInvoke(clazz, name));
        return Optional.ofNullable(invoke)
                .orElseThrow(() -> new ReflectionRuntimeException("Cannot find method " + name + " in class " + clazz.getName()));
    }

    /**
     * 从缓存中获取方法调用器
     * @param clazz 类
     * @param name 方法名
     * @param provider 提供者
     * @return 方法调用器
     */
    @Override
    public Invoke find(Class<?> clazz, String name, Supplier<Invoke> provider) {
        Invoke cached = findFromCache(clazz, name);
        return Optional.ofNullable(cached).orElseGet(() -> {
            Invoke invoke = getInvokeWithProvider(clazz, name, provider);
            if (invoke != null) {
                return invoke;
            }
            return Optional.ofNullable(delegate.find(clazz, name))
                    .map(i -> cache.computeIfAbsent(clazz, k -> new WeakHashMap<>()).put(name, i))
                    .orElseThrow(() -> new ReflectionRuntimeException("Cannot find method " + name + " in class " + clazz.getName()));
        });
    }

    /**
     * 通过访问者提供的provider获取方法调用器
     * @param clazz 类
     * @param name 方法名/属性名
     * @param provider 访问者提供的provider
     * @return 方法调用器
     */
    protected Invoke getInvokeWithProvider(Class<?> clazz, String name, Supplier<Invoke> provider) {
        if (provider != null) {
            Invoke invoke = provider.get();
            if (invoke != null) {
                cache.computeIfAbsent(clazz, k -> new WeakHashMap<>()).put(name, invoke);
                return invoke;
            }
        }
        return null;
    }

    /**
     * 从缓存中查找方法调用器
     *
     * @param clazz 类
     * @param name  方法名/属性名
     * @return 方法调用器
     */
    protected Invoke findFromCache(Class<?> clazz, String name) {
        Map<String, Invoke> invokeCache = cache.computeIfAbsent(clazz, k -> new WeakHashMap<>());
        return invokeCache.get(name);
    }
}
