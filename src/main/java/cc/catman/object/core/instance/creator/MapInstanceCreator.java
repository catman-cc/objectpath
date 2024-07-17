package cc.catman.object.core.instance.creator;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map集合的默认实例化工厂
 * @author jpanda
 * @since 0.0.3
 */
public class MapInstanceCreator extends AbstractInstanceCreator{
    /**
     * 评分,用于判断实例对象是否适合创建,返回值越大,越适合创建,如果返回一个小于0的值,则表示不适合创建
     *
     * @param clazz 实例对象类型
     * @param args  实例对象构造参数
     * @return 评分
     */
    @Override
    public int score(Class<?> clazz, Object... args) {
        if (HashMap.class.equals(clazz)){
            return 100;
        }
        if (LinkedHashMap.class.equals(clazz)){
            return 100;
        }
        if (Map.class.isAssignableFrom(clazz)){
            return 50;
        }
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
        if (AbstractMap.class.isAssignableFrom(clazz)){
            return HashMap.class;
        }
        return HashMap.class;
    }
}
