package cc.catman.object;

import cc.catman.object.core.ConfigurationAccessor;
import cc.catman.object.core.Features;
import cc.catman.object.core.accessor.ClassifierObjectAccessor;
import cc.catman.object.core.accessor.ObjectAccessor;
import cc.catman.object.core.accessor.property.DefaultPropertyAccessorCache;
import cc.catman.object.core.accessor.property.DiscardPropertyAccessorCache;
import cc.catman.object.core.accessor.property.PropertyAccessorCache;
import cc.catman.object.core.accessor.property.accessor.DefaultPropertyAccessorFactory;
import cc.catman.object.core.accessor.property.accessor.PropertyAccessorFactory;
import cc.catman.object.core.accessor.property.wrapper.DefaultPropertyWrapperFactory;
import cc.catman.object.core.accessor.property.wrapper.PropertyWrapperFactory;
import cc.catman.object.core.function.DefaultFunctionManager;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.instance.DefaultInstanceFactory;
import cc.catman.object.core.instance.InstanceFactory;
import cc.catman.object.core.json.NoneJsonCoder;
import cc.catman.object.core.json.JsonCoder;
import cc.catman.object.core.rewrite.AggregationObjectRewrite;
import cc.catman.object.core.rewrite.ObjectRewrite;
import cc.catman.object.core.script.DefaultScriptExecutorManager;
import cc.catman.object.core.script.ScriptExecutorManager;
import lombok.Builder;
import lombok.Data;

import java.util.EnumMap;
import java.util.Objects;

/**
 * 配置对象
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
public class ObjectPathConfiguration {

    /**
     * 创建一个默认的配置对象
     * @return 配置对象
     */
    public static ObjectPathConfiguration create(){
        return ObjectPathConfiguration.create(new NoneJsonCoder());
    }

    /**
     * 创建一个默认的配置对象
     * @param jsonCoder JSON编码器
     * @return 配置对象
     */
    public static ObjectPathConfiguration create(JsonCoder jsonCoder){
        ObjectPathConfiguration opc = ObjectPathConfiguration.builder()
                .jsonCoder(jsonCoder)
                .disableJsonParse(jsonCoder instanceof NoneJsonCoder)
                .functionManager(new DefaultFunctionManager())
                .objectAccessor(ClassifierObjectAccessor.defaultAccessor())
                .objectRewrite(new AggregationObjectRewrite())
                .scriptExecutorManager(DefaultScriptExecutorManager.defaultScriptExecutorManager())
                .instanceFactory(DefaultInstanceFactory.defaultInstance())
                .propertyAccessorCache(new DefaultPropertyAccessorCache())
                .propertyAccessorFactory(DefaultPropertyAccessorFactory.create())
                .wrapperFactory(new DefaultPropertyWrapperFactory())
                .syntaxErrorStop(true)
                .preventSyntaxErrorAndSendToErrorLog(true)
                .build();
        opc.getFeatures().put(Features.CONNECT_STRING_USE_PLUS_SIGN,true);
        return opc.inject();
    }

    /**
     * 当解析到null值时,是否停止解析
     */
    @Builder.Default
    private boolean stopParseWhenNull=false;

    /**
     * 在进行解析时,如果访问了一个null数据,则尝试使用其零值作为参数进行解析
     * - boolean: false
     * - number: 0
     * - string: ""
     * - List: ArrayList
     * - Map:  LinkedHashMap
     */
    @Builder.Default
    private boolean useZeroForNull=true;

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
     * 是否禁用json解析
     */
    @Builder.Default
    private boolean disableJsonParse=false;

    /**
     * 是否禁用脚本解析
     */
    @Builder.Default
    private boolean disableScriptParse=false;

    /**
     * 是否禁用自定义函数
     */
    @Builder.Default
    private boolean disableCustomFunction=false;

    /**
     * 是否使用缓存来提高反射性能
     */
    @Builder.Default
    private boolean useCacheForReflect=true;

    /**
     * 是否跳过超出范围的索引,理论上,超出索引的元素应该抛出异常,但是有些情况下,跳过索引会更加合理
     */
    @Builder.Default
    private boolean skipOutOfRangeIndexForList=true;

    /**
     * 是否允许对set进行写操作,set集合在修改时,将会动态修改其访问器的index
     */
    @Builder.Default
    private boolean allowWriteForSet=false;

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

    private InstanceFactory instanceFactory;

    private PropertyWrapperFactory wrapperFactory;

    private PropertyAccessorCache propertyAccessorCache;

    private PropertyAccessorFactory propertyAccessorFactory;

    public boolean isEnableFeature(Features feature){
        return features.getOrDefault(feature,false);
    }

    public void inject(Object object){
        if (Objects.isNull(object)){
            return;
        }
        if (ConfigurationAccessor.class.isAssignableFrom(object.getClass())){
            ConfigurationAccessor accessor = (ConfigurationAccessor) object;
            accessor.injectConfiguration(this);
        }
    }

    public ObjectPathConfiguration inject(){
        this.inject(this.getJsonCoder());
        this.inject(this.getObjectRewrite());
        this.inject(this.getObjectAccessor());
        this.inject(this.getFunctionManager());
        this.inject(this.getScriptExecutorManager());
        this.inject(this.getInstanceFactory());
        this.inject(this.getWrapperFactory());
        this.inject(this.getPropertyAccessorFactory());
        this.inject(this.getPropertyAccessorCache());
        return this;
    }

}
