package cc.catman.object.core.accessor.invoke;
/**
 * 标准读取调用器查找器
 * @author jpanda
 * @since 0.0.1
 */
public class StandardInvokeFinder implements InvokeFinder{
    @Override
    public Invoke find(Class<?> clazz, String name) {
        return JavaBeanInvokeFinder.findInvoke(clazz,name);
    }

    /**
     * 查找调用器
     *
     * @param clazz     类
     * @param name      名称
     * @param valueType 值得类型
     * @return 调用器
     */
    @Override
    public Invoke findWrite(Class<?> clazz, String name, Class<?> valueType) {
        return JavaBeanInvokeFinder.findWriteInvoke(clazz, name, valueType);
    }
}
