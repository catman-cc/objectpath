package cc.catman.object.core.accessor.matcher;

/**
 * 类名匹配器
 * @author jpanda
 * @since 0.0.1
 */
public interface IClassNameMatcher extends IClassMatcher {

    @Override
    default boolean match(Class<?> name){
        return match(name.getName());
    }
    boolean match(String name);
}
