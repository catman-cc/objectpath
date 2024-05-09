package cc.catman.object;
/**
 * 对象路径访问器
 * @author jpanda
 * @since 0.0.1
 */
public interface ObjectPathAccessor {
    /**
     * 计算
     * @param object 对象
     * @return 结果
     */
    default Object eval(Object object){
        return eval(object, Object.class);
    }

    /**
     * 计算
     * @param object 对象
     * @param clazz 类型
     * @param <T> 类型
     * @return 结果
     */
    <T> T eval(Object object, Class<T> clazz);
}
