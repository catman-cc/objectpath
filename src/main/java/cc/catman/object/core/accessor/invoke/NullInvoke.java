package cc.catman.object.core.accessor.invoke;

/**
 * 空调用器
 * @author jpanda
 * @since 0.0.3
 */
public class NullInvoke implements Invoke {

    @Override
    public Object invoke(Object object, Object... args) {
        return null;
    }
}
