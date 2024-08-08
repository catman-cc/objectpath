package cc.catman.object.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实例对象创建异常
 * @author jpanda
 * @since 0.0.3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ObjectCreatorRuntimeException extends RuntimeException{

    /**
     * 实例对象类型
     */
    private final Class<?> clazz;

    /**
     * 实例对象构造参数
     */
    private final transient Object[] args;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     * @param clazz 期望创建的对象诶下
     * @param args 期望创建对象时使用的构造参数
     * @since 0.0.3
     */
    public ObjectCreatorRuntimeException(Class<?> clazz, Object[] args) {
        this.clazz = clazz;
        this.args = args;
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @param clazz 期望创建的对象诶下
     * @param args 期望创建对象时使用的构造参数
     * @since 1.4
     */
    public ObjectCreatorRuntimeException(Throwable cause, Class<?> clazz, Object[] args) {
        super(cause);
        this.clazz = clazz;
        this.args = args;
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param clazz 期望创建的对象诶下
     * @param args 期望创建对象时使用的构造参数
     */
    public ObjectCreatorRuntimeException(String message, Class<?> clazz, Object[] args) {
        super(message);
        this.clazz = clazz;
        this.args = args;
    }
}
