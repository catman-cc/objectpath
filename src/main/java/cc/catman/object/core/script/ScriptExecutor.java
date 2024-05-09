package cc.catman.object.core.script;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本执行器,用于执行脚本
 *
 * @author jpanda
 * @since 2024-04-23
 */
public interface ScriptExecutor {
    /**
     * 执行脚本
     *
     * @param script 脚本
     * @return 执行结果
     */
    default Object eval(String script) {
        return eval(script, new HashMap<>());
    }

    /**
     * 执行脚本
     *
     * @param script 脚本
     * @param args   参数
     * @return 执行结果
     */
    @SuppressWarnings("java:S106")
    default Object eval(String script, Map<String, Object> args) {
        return eval(script, args, new PrintWriter(System.out), new PrintWriter(System.err));
    }

    /**
     * 执行脚本
     *
     * @param script 脚本
     * @param args   参数
     * @param out    输出流
     * @param err    错误流
     * @return 执行结果
     */
    Object eval(String script, Map<String, Object> args, PrintWriter out, PrintWriter err);
}
