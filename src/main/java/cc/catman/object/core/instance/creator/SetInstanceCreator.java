package cc.catman.object.core.instance.creator;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Set集合实例化创建器
 */
public class SetInstanceCreator extends AbstractInstanceCreator{
    /**
     * 查找候选的实现类,针对抽象类和接口
     *
     * @param clazz 实例对象类型
     * @return 可用的实现类
     */
    @Override
    protected Class<?> findCandidateClassForAbstractOrInterface(Class<?> clazz) {
        return null;
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
        if (HashSet.class.equals(clazz)){
            return 100;
        }
        if (LinkedHashSet.class.equals(clazz)){
            return 100;
        }
        if (Set.class.isAssignableFrom(clazz)){
            return 50;
        }
        return -1;
    }
}
