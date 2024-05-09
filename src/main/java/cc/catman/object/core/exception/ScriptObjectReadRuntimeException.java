package cc.catman.object.core.exception;

import lombok.Getter;

/**
 * 脚本对象读取异常
 * @author jpanda
 * @since 0.0.1
 */
@Getter
public class ScriptObjectReadRuntimeException extends RuntimeException{
    /**
     * 脚本对象
     */
    private final  transient Object scriptObject;

    public ScriptObjectReadRuntimeException(Object scriptObject) {
        this.scriptObject = scriptObject;
    }

    public ScriptObjectReadRuntimeException(String message, Object scriptObject) {
        super(message);
        this.scriptObject = scriptObject;
    }

    public ScriptObjectReadRuntimeException(String message, Throwable cause, Object scriptObject) {
        super(message, cause);
        this.scriptObject = scriptObject;
    }

    public ScriptObjectReadRuntimeException(Throwable cause, Object scriptObject) {
        super(cause);
        this.scriptObject = scriptObject;
    }

    public ScriptObjectReadRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object scriptObject) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.scriptObject = scriptObject;
    }
}
