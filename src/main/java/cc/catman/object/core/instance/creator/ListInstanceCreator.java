package cc.catman.object.core.instance.creator;

import java.util.*;

/**
 * List集合的默认实例化工厂,不算是一个好的实现,因为比较适合的实现,应该是针对不同的List实现类型创建不同的工厂.
 * @author jpanda
 * @since 0.0.2
 */
public class ListInstanceCreator extends AbstractInstanceCreator {

    @Override
    public int score(Class<?> clazz, Object... args) {
        if (List.class.equals(clazz)){
            // 只是单纯的声明了List接口,并没有指定具体的实现类型,那么可以考虑给一个较高的评分
            return 100;
        }
        if (List.class.isAssignableFrom(clazz)){
            // 声明了List接口的实现类型,那么可以考虑给一个相对较高的评分,但不会特别高
            return 50;
        }
        // 如果不是List接口的实现类型,那么就不适合创建,因此返回0
        return -1;
    }

    /**
     * 查找候选的实现类,针对抽象类和接口
     *
     * @param clazz 实例对象类型
     * @return 可用的实现类
     */
    @Override
    protected Class<?> findCandidateClassForAbstractOrInterface(Class<?> clazz) {
        if (AbstractSequentialList.class.isAssignableFrom(clazz)){
            return LinkedList.class;
        }
        if (AbstractList.class.isAssignableFrom(clazz)) {
            return ArrayList.class;
        }
        return ArrayList.class;
    }
}
