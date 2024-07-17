package cc.catman.object.core.instance.creator.array;

/**
 * 字符串数组实例创建器
 * @author jpanda
 * @since 0.0.3
 */
public class StringArrayInstanceCreator extends AbstractArrayInstanceCreator{

    public StringArrayInstanceCreator() {
        super(String[].class,String[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doCreate(Class<T> clazz, Object... args) {
        return (T) new String[0];
    }
}
