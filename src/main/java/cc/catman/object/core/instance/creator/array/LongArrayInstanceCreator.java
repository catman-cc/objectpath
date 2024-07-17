package cc.catman.object.core.instance.creator.array;
/**
 * Long类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class LongArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public LongArrayInstanceCreator() {
        super(long[].class,Long[] .class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T) (
                clazz.isPrimitive()
                ? new long[0]
                : new Long[0]
                );
    }
}
