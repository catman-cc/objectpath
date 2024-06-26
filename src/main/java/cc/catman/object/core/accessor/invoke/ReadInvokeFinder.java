package cc.catman.object.core.accessor.invoke;

import java.util.function.Supplier;

/**
 * 调用器查找器
 * @author jpanda
 * @since 0.0.1
 */
public interface ReadInvokeFinder {
    /**
     * 查找调用器
     * @param clazz 类
     * @param name 名称
     * @return 调用器
     */
    Invoke find(Class<?> clazz, String name);

    /**
     * 查找调用器
     * @param clazz 类
     * @param name 名称
     * @param provider 提供者
     * @return 调用器
     */
    default Invoke find(Class<?> clazz, String name, Supplier<Invoke> provider) {
        if (provider == null){
            return find(clazz, name);
        }
        return provider.get();
    }
}
