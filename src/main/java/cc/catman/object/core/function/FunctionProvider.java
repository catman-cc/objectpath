package cc.catman.object.core.function;

import cc.catman.object.core.ObjectPathParserContext;

import java.util.List;

/**
 * 自定义函数提供者
 * @author jpanda
 * @since 0.0.1
 */
public interface FunctionProvider {

    /**
     * 获取函数名称
     * @return 函数名称
     */
    String name();

    /**
     * 获取函数描述
     * @return 函数描述
     */
    String desc();

    /**
     * 获取函数参数描述
     * @return 函数描述
     */
    List<List<FunctionArgDesc>> args();


    /**
     * 获取函数返回值描述
     * @return 函数返回值描述
     */
    FunctionResultDesc result();

    /**
     * 判断是否匹配指定函数
     * @param name 函数名称
     * @param params 参数列表
     * @return 是否匹配
     */
    boolean isMatch(String name, List<Object> params);

    /**
     * 执行函数
     * @param params 参数列表
     * @return 执行结果
     */
    Object apply(ObjectPathParserContext context,List<Object> params);
}
