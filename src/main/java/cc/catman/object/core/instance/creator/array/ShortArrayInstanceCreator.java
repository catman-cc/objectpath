package cc.catman.object.core.instance.creator.array;
/**
 * Short类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class ShortArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public ShortArrayInstanceCreator() {
        super(short[].class,Short[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T) (clazz.isPrimitive()
                ? new short[0]
                : new Short[0]
                );
    }
}
