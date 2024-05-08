package cc.catman.object.core.function;

import java.util.List;

/**
 * 函数管理器
 * @author jpanda
 * @since 0.0.1
 */
public interface FunctionManager {
    /**
     * 注册函数提供者
     * @param provider 函数提供者
     */
    void register(FunctionProvider provider);
    /**
     * 获取函数提供者
     * @param name 函数名称
     * @return 函数提供者
     */
    List<FunctionProvider> getProvider(String name);

    /**
     * 获取函数提供者
     * @param name 函数名称
     * @param params 参数列表
     * @return 函数提供者
     */
    FunctionProvider getProvider(String name, List<Object> params);

    /**
     * 判断是否存在指定函数
     * @param name 函数名称
     * @return 是否存在
     */
    boolean contains(String name);

    /**
     * 获取所有函数提供者
     * @return 函数提供者列表
     */
    List<FunctionProvider> providers();

    /**
     * 获取所有函数名称
     * @return 函数名称列表
     */
    List<String> names();
}
