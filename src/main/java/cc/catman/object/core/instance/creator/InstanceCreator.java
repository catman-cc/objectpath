package cc.catman.object.core.instance.creator;

/**
 * 实例工厂接口,负责创建实例对象
 * @author jpanda
 * @since 0.0.3
 */
public interface InstanceCreator {

    /**
     * 评分,用于判断实例对象是否适合创建,返回值越大,越适合创建,如果返回一个小于0的值,则表示不适合创建
     * @param clazz 实例对象类型
     * @param args 实例对象构造参数
     * @return 评分
     */
    int  score(Class<?> clazz,Object ...args);

    /**
     * 创建实例对象
     * @param clazz 实例对象类型
     * @param args 实例对象构造参数
     * @return 实例对象
     * @param <T> 实例对象类型
     */
     <T> T create(Class<T> clazz, Object... args);
}
