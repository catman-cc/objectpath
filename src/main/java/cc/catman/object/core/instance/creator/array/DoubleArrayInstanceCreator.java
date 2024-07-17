package cc.catman.object.core.instance.creator.array;
/**
 * Double类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class DoubleArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public DoubleArrayInstanceCreator() {
        super(double[].class,Double[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return ((T) (clazz.isPrimitive()
        ? new double[0]
        : new Double[0]));
    }
}
