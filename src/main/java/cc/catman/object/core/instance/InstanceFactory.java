package cc.catman.object.core.instance;

/**
 * 实例对象工厂接口
 * @author jpanda
 * @since 0.0.3
 */
public interface InstanceFactory {

    /**
     * 创建实例对象
     * @param clazz 实例对象类型
     * @param args 实例对象构造参数
     * @return 实例对象
     * @param <T> 实例对象类型
     */
    <T> T create(Class<T> clazz, Object... args);
}
