package cc.catman.object.core.instance.creator.array;

/**
 * Float类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class FloatArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public FloatArrayInstanceCreator() {
        super(float[].class,Float[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return ((T)(clazz.isPrimitive()
        ? new float[0]
        : new Float[0]
        ));
    }
}
