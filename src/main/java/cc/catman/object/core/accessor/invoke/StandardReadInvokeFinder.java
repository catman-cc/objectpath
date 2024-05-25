package cc.catman.object.core.accessor.invoke;
/**
 * 标准读取调用器查找器
 * @author jpanda
 * @since 0.0.1
 */
public class StandardReadInvokeFinder implements ReadInvokeFinder{
    @Override
    public Invoke find(Class<?> clazz, String name) {
        return JavaBeanInvokeFinder.findInvoke(clazz,name);
    }
}
