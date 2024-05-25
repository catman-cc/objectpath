package cc.catman.object.core.script;

import cc.catman.object.core.ConfigurationAccessor;

import java.util.Map;

/**
 * 脚本执行器管理器
 * @author jpanda
 * @since 0.0.1
 */
public interface ScriptExecutorManager extends ConfigurationAccessor {
     /**
      * 获取所有的执行器
      * @return 执行器
      */
     Map<String,ScriptExecutor> executors();
        /**
        * 添加执行器
        * @param name 名称
        * @param executor 执行器
        * @return 执行器管理器
        */
     ScriptExecutorManager addExecutor(String name,ScriptExecutor executor);
        /**
        * 获取执行器
        * @param name 名称
        * @return 执行器
        */
     ScriptExecutor getExecutor(String name);
}
