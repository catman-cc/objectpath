package cc.catman.object.core.instance.creator.array;

import cc.catman.object.core.instance.creator.InstanceCreator;

/**
 * 一个抽象数组实例化工厂
 * @author jpanda
 * @since 0.0.3
 */
public abstract class AbstractArrayInstanceCreator implements InstanceCreator {

    protected Class<?> elementClazz;
    protected Class<?> elementUnboxedClazz;

    protected AbstractArrayInstanceCreator(Class<?> elementClazz, Class<?> elementUnboxedClazz) {
        this.elementClazz = elementClazz;
        this.elementUnboxedClazz = elementUnboxedClazz;
    }


    /**
     * 评分,用于判断实例对象是否适合创建,返回值越大,越适合创建,如果返回一个小于0的值,则表示不适合创建
     *
     * @param clazz 实例对象类型
     * @param args  实例对象构造参数
     * @return 评分
     */
    @Override
    public int score(Class<?> clazz, Object... args) {
        if (!clazz.isArray()){
            return -1;
        }
        return elementClazz.equals(clazz)||elementUnboxedClazz.equals(clazz)? 100 : -1;
    }

    /**
     * 创建实例对象
     *
     * @param clazz 实例对象类型
     * @param args  实例对象构造参数
     * @return 实例对象
     */
    @Override
    public <T> T create(Class<T> clazz, Object... args) {
        return doCreate(clazz, args);
    }

    protected abstract <T> T doCreate(Class<T> clazz, Object... args);
}
