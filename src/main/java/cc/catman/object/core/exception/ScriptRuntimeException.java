package cc.catman.object.core.exception;

import java.io.PrintStream;
import java.util.Map;

/**
 * 脚本运行时异常
 * @author jpanda
 * @since 0.0.1
 */
public class ScriptRuntimeException extends RuntimeException{
    /**
     * 脚本内容
     */
    private final String script;
    /**
     * 参数
     */
    private final transient Map<String,Object> args;
    /**
     * 脚本类型
     */
    private final String scriptKind;

    public ScriptRuntimeException(String script, Map<String,Object> args, String scriptKind) {
        this.script = script;
        this.args = args;
        this.scriptKind = scriptKind;
    }

    public ScriptRuntimeException(String message, String script, Map<String,Object> args, String scriptKind) {
        super(message);
        this.script = script;
        this.args = args;
        this.scriptKind = scriptKind;
    }

    public ScriptRuntimeException(String message, Throwable cause, String script, Map<String,Object> args, String scriptKind) {
        super(message, cause);
        this.script = script;
        this.args = args;
        this.scriptKind = scriptKind;
    }

    public ScriptRuntimeException(Throwable cause, String script, Map<String,Object> args, String scriptKind) {
        super(cause);
        this.script = script;
        this.args = args;
        this.scriptKind = scriptKind;
    }

    public ScriptRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String script, Map<String,Object> args, String scriptKind) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.script = script;
        this.args = args;
        this.scriptKind = scriptKind;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        String sb = "script:" + script + "\n" +
                    "args:" + args + "\n" +
                    "scriptKind:" + scriptKind + "\n";
        s.println(sb);
        super.printStackTrace(s);
    }
}
