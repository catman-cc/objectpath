package cc.catman.object.core.script;

import java.util.HashMap;
import java.util.Map;

public class DefaultScriptExecutorManager implements ScriptExecutorManager {
    protected Map<String,ScriptExecutor> executors;

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
        this.executors.put(name, executor);
        return this;
    }

    @Override
    public ScriptExecutor getExecutor(String name) {
        return this.executors.get(name);
    }
}
