package cc.catman.object.core.accessor.matcher;
/**
 * 类名匹配器
 * @author jpanda
 * @since 0.0.1
 */
@FunctionalInterface
public interface IClassMatcher {

    boolean match(Class<?> clazz);
}
