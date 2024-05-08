package cc.catman.object;

import cc.catman.object.core.accessor.ObjectAccessor;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.json.JsonCoder;
import cc.catman.object.core.rewrite.ObjectRewrite;
import cc.catman.object.core.script.ScriptExecutorManager;
import lombok.Builder;
import lombok.Data;
/**
 * 配置对象
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
public class ObjectPathConfiguration {
    /**
     * 根据上下文,自动跳过null值,TODO:未实现
     */
    @Builder.Default
    private boolean smartSkipNull=false;

    /**
     * 当解析到null值时,是否停止解析
     */
    @Builder.Default
    private boolean stopParseWhenNull=false;

    /**
     * 当调用集合方法时,是否自动创建集合
     */
    @Builder.Default
    private boolean autoCreateCollectionWhenInvokeMethod=false;
//    /**
//     * 是否允许通过符号+,链接字符串
//     */
//    private boolean enableStringConcatUsePlus;
//
//    /**
//     * 当字符串可以转换为数字时,在进行+运算时,是否转换为数字,建议关闭,通过函数显示转换为数字
//     */
//    private boolean enableStringConvertToNumberWhenPlus;
//
//    /**
//     * 当针对对象错误的调用方法时,是否抛出异常,默认为true
//     */
//    private boolean throwExceptionWhenInvokeMethodError;


    /**
     * json解析器
     */
    private JsonCoder jsonCoder;

    private FunctionManager functionManager;

    private ObjectAccessor objectAccessor;

    private ObjectRewrite objectRewrite;

    private ScriptExecutorManager scriptExecutorManager;

}
