package cc.catman.object.core.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

/**
 * 抽象脚本执行器,提供了脚本执行的基本实现
 * @author jpanda
 * @since 2024-04-23
 */
public abstract class AbstractScriptExecutor implements ScriptExecutor{
    /**
     * ScriptObjectMirror解析器
     */
    private final ScriptObjectMirrorParser scriptObjectMirrorParser;

    public AbstractScriptExecutor() {
        this(new ScriptObjectMirrorParser());
    }

    public AbstractScriptExecutor(ScriptObjectMirrorParser scriptObjectMirrorParser) {
        this.scriptObjectMirrorParser = scriptObjectMirrorParser;
    }

    @Override
    public Object eval(String script, Map<String, Object> args, PrintWriter out, PrintWriter err) {
        ScriptEngine scriptEngine = createScriptEngine();
        ScriptContext sc = scriptEngine.getContext();

        Bindings bindings = scriptEngine.createBindings();
        bindings.putAll(args);

        Optional.ofNullable(out).ifPresent(sc::setWriter);
        Optional.ofNullable(err).ifPresent(sc::setErrorWriter);
        String scriptStr = rewrite(script);
        try {
            Object res = scriptEngine.eval(scriptStr, bindings);
            return scriptObjectMirrorParser.parse(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract ScriptEngine createScriptEngine();

    /**
     * 脚本重写,用于重写脚本内容
     * @param script 脚本
     * @return 重写后的脚本
     */
    protected String rewrite(String script) {
        return script;
    }
}
