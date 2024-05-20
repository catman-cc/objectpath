package cc.catman.object;

import cc.catman.object.core.Features;
import cc.catman.object.core.accessor.ObjectAccessor;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.json.JsonCoder;
import cc.catman.object.core.rewrite.ObjectRewrite;
import cc.catman.object.core.script.ScriptExecutorManager;
import lombok.Builder;
import lombok.Data;

import java.util.EnumMap;

/**
 * 配置对象
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
public class ObjectPathConfiguration {

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

    /**
     * 当发生语法错误时,抑制异常,并将其记录到错误日志
     * 该值为false时,将显式抛出异常信息
     */
    @Builder.Default
    private boolean preventSyntaxErrorAndSendToErrorLog=false;

    /**
     * 当发生语法错误时,是否停止解析
     */
    @Builder.Default
    private boolean syntaxErrorStop=true;

    /**
     * 一些特性方法
     */
    @Builder.Default
    private EnumMap<Features,Boolean> features=new EnumMap<>(Features.class);
    /**
     * json解析器
     */
    private JsonCoder jsonCoder;

    private FunctionManager functionManager;

    private ObjectAccessor objectAccessor;

    private ObjectRewrite objectRewrite;

    private ScriptExecutorManager scriptExecutorManager;

    public boolean isEnableFeature(Features feature){
        return features.getOrDefault(feature,false);
    }

}
