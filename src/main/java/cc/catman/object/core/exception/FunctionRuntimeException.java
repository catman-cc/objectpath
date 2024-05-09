package cc.catman.object.core.exception;

import lombok.Getter;

/**
 * 函数运行时异常
 * 当函数执行出现异常时,抛出此异常
 * @author jpanda
 * @since 0.0.1
 */
@Getter
public class FunctionRuntimeException extends RuntimeException{
    /**
     * 函数名称
     */
    private final String functionName;
    /**
     * 参数
     */
    private final transient Object params;

    public FunctionRuntimeException(String functionName, Object params) {
        this.functionName = functionName;
        this.params = params;
    }

    public FunctionRuntimeException(String message, String functionName, Object params) {
        super(message);
        this.functionName = functionName;
        this.params = params;
    }

    public FunctionRuntimeException(String message, Throwable cause, String functionName, Object params) {
        super(message, cause);
        this.functionName = functionName;
        this.params = params;
    }

    public FunctionRuntimeException(Throwable cause, String functionName, Object params) {
        super(cause);
        this.functionName = functionName;
        this.params = params;
    }

    public FunctionRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String functionName, Object params) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.functionName = functionName;
        this.params = params;
    }
}
