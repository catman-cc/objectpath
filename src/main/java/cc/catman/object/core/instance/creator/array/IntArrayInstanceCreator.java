package cc.catman.object.core.instance.creator.array;
/**
 * Integer类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class IntArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public IntArrayInstanceCreator() {
        super(int[].class, Integer[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T)(clazz.isPrimitive()
        ? new int[0]
        : new Integer[0]
        );
    }
}
