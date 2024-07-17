package cc.catman.object.core.instance.creator.array;
/**
 * Char类型数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class CharArrayInstanceCreator extends AbstractArrayInstanceCreator{
    public CharArrayInstanceCreator() {
        super(char[].class,Character[].class);
    }

    @Override
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return ((T)(clazz.isPrimitive()
                ? new char[0]
                : new Character[0]));
    }
}
