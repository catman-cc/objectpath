package cc.catman.object.core.accessor.invoke;

/**
 * 写入执行器查找器
 * @author jpanda
 * @since 0.0.3
 */
public interface SetInvokeFinder {
    /**
     * 查找调用器
     * @param clazz 类
     * @param name 名称
     * @param valueType 值得类型
     * @return 调用器
     */
    Invoke findWrite(Class<?> clazz, String name,Class<?> valueType);

}
