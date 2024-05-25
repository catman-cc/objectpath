package cc.catman.object.core.accessor.invoke;

/**
 * 调用器
 *
 * @author jpanda
 * @since 0.0.1
 */
public interface Invoke {
    Object invoke(Object object, Object... args);
}
