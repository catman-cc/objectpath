package cc.catman.object.core.instance.creator;

import lombok.SneakyThrows;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 抽象实例化工厂
 * @author jpanda
 * @since 0.0.3
 */
public abstract class AbstractInstanceCreator implements InstanceCreator{

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz, Object... args) {
        if (!List.class.isAssignableFrom(clazz)){
            throw new IllegalArgumentException("Class is not a List implementation type.");
        }
        Class<?> candidateClass=getCandidateClass(clazz);
        // 目前的list基本都是可以直接通过无参构造器创建的
        Object t = candidateClass.newInstance();
        return (T)t;
    }

    /**
     * 获取候选的实现类
     * @param clazz 实例对象类型
     * @return 候选的实现类
     */
    protected Class<?> getCandidateClass(Class<?> clazz){
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
            return findCandidateClassForAbstractOrInterface(clazz);
        }
        return findCandidateClass(clazz);
    }

    /**
     * 查找候选的实现类,针对抽象类和接口
     * @param clazz 实例对象类型
     * @return 可用的实现类
     */
    protected abstract Class<?> findCandidateClassForAbstractOrInterface(Class<?> clazz);

    /**
     * 查找候选的实现类
     * @param clazz 待实例化的对象
     * @return 候选的实现类
     */
    protected Class<?> findCandidateClass(Class<?> clazz){
        return clazz;
    }
}
