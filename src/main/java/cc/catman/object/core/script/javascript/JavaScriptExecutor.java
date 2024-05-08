package cc.catman.object.core.script.javascript;

import cc.catman.object.core.script.AbstractScriptExecutor;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * JavaScript执行器
 * @author jpanda
 * @since 2024-04-23
 */
public class JavaScriptExecutor extends AbstractScriptExecutor {

    @Override
    protected ScriptEngine createScriptEngine() {
        return new ScriptEngineManager().getEngineByName("JavaScript");
    }

    /**
     * 重写脚本,在脚本外包裹一个函数,用于解决脚本中的return问题
     * @param script 脚本
     * @return 重写后的脚本
     */
    @Override
    protected String rewrite(String script) {
        return String.format("function f(){%s};f();",script);
    }
}
