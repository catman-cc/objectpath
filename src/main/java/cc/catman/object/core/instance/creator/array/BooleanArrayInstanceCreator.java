package cc.catman.object.core.instance.creator.array;

/**
 * Boolean类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class BooleanArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public BooleanArrayInstanceCreator() {
        super(boolean[].class,Boolean[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T)(clazz.isPrimitive()
                ? new boolean[0]
                : new Boolean[0]
        );
    }
}
