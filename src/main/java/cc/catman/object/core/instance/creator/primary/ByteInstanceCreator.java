package cc.catman.object.core.instance.creator.primary;

import cc.catman.object.core.instance.creator.InstanceCreator;

/**
 * Byte类型实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class ByteInstanceCreator implements InstanceCreator {
    /**
     * 评分,用于判断实例对象是否适合创建,返回值越大,越适合创建,如果返回一个小于0的值,则表示不适合创建
     *
     * @param clazz 实例对象类型
     * @param args  实例对象构造参数
     * @return 评分
     */
    @Override
    public int score(Class<?> clazz, Object... args) {
        return byte.class.equals(clazz)||Byte.class.equals(clazz)
                ? 10
                : -1;
    }

    /**
     * 创建实例对象
     *
     * @param clazz 实例对象类型
     * @param args  实例对象构造参数
     * @return 实例对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz, Object... args) {
        return  (T)new Byte("0");
    }
}
