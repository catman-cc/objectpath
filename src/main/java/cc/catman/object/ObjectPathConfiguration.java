package cc.catman.object;

import cc.catman.object.core.accessor.SpecifyObjectAccessor;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.json.JsonCoder;
import cc.catman.object.core.rewrite.ObjectRewrite;
import cc.catman.object.core.script.ScriptExecutorManager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectPathConfiguration {
    /**
     * 根据上下文,自动跳过null值
     */
    private boolean smartSkipNull;
    /**
     * 当调用集合方法时,是否自动创建集合
     */
    private boolean autoCreateCollectionWhenInvokeMethod;
    /**
     * 当调用字符串方法时,如果字符串为null,是否自动创建字符串
     */
    private boolean autoCreateStringWhenInvokeMethod;
    /**
     * 是否允许通过符号+,链接字符串
     */
    private boolean enableStringConcatUsePlus;

    /**
     * 当字符串可以转换为数字时,在进行+运算时,是否转换为数字,建议关闭,通过函数显示转换为数字
     */
    private boolean enableStringConvertToNumberWhenPlus;

    /**
     * 当针对对象错误的调用方法时,是否抛出异常,默认为true
     */
    private boolean throwExceptionWhenInvokeMethodError;


    /**
     * json解析器
     */
    private JsonCoder jsonCoder;

    private FunctionManager functionManager;

    private SpecifyObjectAccessor objectAccessor;

    private ObjectRewrite objectRewrite;

    private ScriptExecutorManager scriptExecutorManager;

}
