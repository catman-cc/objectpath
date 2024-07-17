package cc.catman.object.core.instance.creator.array;

/**
 * Byte类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class ByteArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public ByteArrayInstanceCreator() {
        super(byte[].class,Byte[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T)(clazz.isPrimitive()
                ? new byte[0]
                : new Byte[0]
        );
    }
}
