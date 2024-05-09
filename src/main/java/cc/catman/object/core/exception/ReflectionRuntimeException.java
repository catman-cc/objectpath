package cc.catman.object.core.exception;
/**
 * 反射异常
 * @author jpanda
 * @since 0.0.1
 */
public class ReflectionRuntimeException extends RuntimeException{
    public ReflectionRuntimeException() {
    }

    public ReflectionRuntimeException(String message) {
        super(message);
    }

    public ReflectionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionRuntimeException(Throwable cause) {
        super(cause);
    }

    public ReflectionRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
