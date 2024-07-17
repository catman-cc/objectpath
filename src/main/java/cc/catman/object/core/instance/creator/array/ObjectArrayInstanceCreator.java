package cc.catman.object.core.instance.creator.array;
/**
 * 默认数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class ObjectArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public ObjectArrayInstanceCreator() {
        super(Object[].class,Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T) new Object[0];
    }
}
