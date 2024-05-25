package cc.catman.object.core.script;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.script.javascript.JavaScriptExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认的脚本执行器管理器
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultScriptExecutorManager implements ScriptExecutorManager {

    protected Map<String,ScriptExecutor> executors;

    private ObjectPathConfiguration configuration;

    public static ScriptExecutorManager defaultScriptExecutorManager(){
        JavaScriptExecutor jse = new JavaScriptExecutor();
        return new DefaultScriptExecutorManager()
                .addExecutor("js", jse)
                .addExecutor("javascript", jse)
                .addExecutor("JavaScript", jse);
    }

    public DefaultScriptExecutorManager() {
        this(new HashMap<>());
    }

    public DefaultScriptExecutorManager(Map<String, ScriptExecutor> executors) {
        this.executors = executors;
    }

    @Override
    public Map<String, ScriptExecutor> executors() {
        return this.executors;
    }

    @Override
    public ScriptExecutorManager addExecutor(String name, ScriptExecutor executor) {
        Optional.ofNullable(this.configuration).ifPresent(c-> c.inject(executor));
        this.executors.put(name, executor);
        return this;
    }

    @Override
    public ScriptExecutor getExecutor(String name) {
        return this.executors.get(name);
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
        this.executors.values().forEach(configuration::inject);
    }
}
