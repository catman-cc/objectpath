package cc.catman.object.core;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.ObjectAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.function.FunctionProvider;
import cc.catman.object.core.json.JsonCoder;
import cc.catman.object.core.rewrite.ObjectRewrite;
import cc.catman.object.core.script.ScriptExecutor;
import cc.catman.object.core.script.ScriptExecutorManager;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 默认对象路径解析上下文
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultObjectPathParserContext implements ObjectPathParserContext{
    /**
     * 根对象
     */
    private final PropertyWrapper root;
    /**
     * 父对象,需要注意,此处的父对象并不是指当前对象的父对象,而是指当前上下文的创建者所持有的cv
     */
    private PropertyWrapper pv;
    /**
     * 当前对象
     */
    private PropertyWrapper cv;

    /**
     * 父上下文
     */
    private final ObjectPathParserContext parent;

    /**
     * 对象访问器,用于访问对象的属性
     */
    private final ObjectAccessor objectAccessor;

    /**
     * 对象重写器,负责将对象重写为指定的对象
     */
    private final ObjectRewrite objectRewrite;

    /**
     * 函数管理器,用于管理自定义函数
     */
    private final FunctionManager functionManager;
    /**
     * json解析器
     */
    private final JsonCoder jsonCoder;

    /**
     * 脚本执行器管理器
     */
    private final ScriptExecutorManager scriptExecutorManager;

    @Getter
    private final ObjectPathConfiguration configuration;

    @Getter
    @Setter
    private EContextMod mod;

    @Override
    public boolean readOnly() {
        return EContextMod.READ_ONLY.equals(mod);
    }

    public DefaultObjectPathParserContext(PropertyWrapper root, ObjectPathConfiguration configuration) {
       this(root,root,configuration);
    }

    public DefaultObjectPathParserContext(PropertyWrapper root, PropertyWrapper cv, ObjectPathConfiguration configuration) {
      this(root,cv,null,configuration);
    }

    public DefaultObjectPathParserContext(PropertyWrapper root, PropertyWrapper cv, ObjectPathParserContext parent, ObjectPathConfiguration configuration) {
        this.root = root;
        this.cv = cv;
        this.parent = parent;
        this.configuration = configuration;
        this.objectAccessor=configuration.getObjectAccessor();
        this.objectRewrite=configuration.getObjectRewrite();
        this.jsonCoder=configuration.getJsonCoder();
        this.scriptExecutorManager=configuration.getScriptExecutorManager();
        this.functionManager=configuration.getFunctionManager();
        this.mod=EContextMod.READ_ONLY;
    }

    @Override
    public ObjectPathParserContext parent() {
        return parent;
    }

    @Override
    public PropertyWrapper rootValue() {
        return root;
    }

    @Override
    public PropertyWrapper parentValue() {
        return pv;
    }

    @Override
    public PropertyWrapper currentValue() {
        return cv;
    }

    @Override
    public PropertyWrapper updateCurrent(Object current) {
        this.cv=configuration.getWrapperFactory().create(current);
        return this.cv;
    }

    @Override
    public void updateParent(Object parent) {
        this.pv=configuration.getWrapperFactory().create(parent);
    }

    @Override
    public ObjectPathParserContext createChildContext(ELocation location) {
        switch (location){
            case ROOT:
                return createContext(root,null,root);
            case PARENT:
                return createContext(root,null,pv);
            case CURRENT:
                return createContext(root,pv,cv);
        }
        return null;
    }

    @Override
    public ObjectPathParserContext createContext(Object root, Object parent, Object current) {
        if (this.mod.isReadOnly()){
            DefaultObjectPathParserContext context = new DefaultObjectPathParserContext(
                    configuration.getWrapperFactory().createReadOnly(root),
                    configuration.getWrapperFactory().createReadOnly(current),
                    this,
                    this.configuration
            );
            context.setMod(this.getMod());
            context.pv = configuration.getWrapperFactory().createReadOnly(parent);
            return context;
        }
        DefaultObjectPathParserContext context = new DefaultObjectPathParserContext(
                configuration.getWrapperFactory().create(root),
                configuration.getWrapperFactory().create(current),
                this,
                this.configuration
        );
        context.setMod(this.getMod());
        context.pv = configuration.getWrapperFactory().create(parent);
        return context;
    }

    @Override
    public ObjectPathParserContext createChild(Object current) {
        return createContext(root,null,current);
    }

    @Override
    public PropertyWrapper eval(String path) {
        return cv.read(path);
    }

    @Override
    public void eachKey(ObjectPathParserContext context, Consumer<Object> consumer) {
        if (Objects.isNull(cv)){
            return;
        }
        objectAccessor.eachKey(cv,consumer);
    }

    @Override
    public void each(ObjectPathParserContext context, Consumer<Entity> consumer) {
        if (Objects.isNull(cv)){
            return;
        }
        objectAccessor.eachEntry(cv,consumer);
    }

    @Override
    public void eachValue(ObjectPathParserContext context, Consumer<Object> consumer) {
        if (Objects.isNull(cv)){
            return;
        }
        objectAccessor.eachValue(cv,consumer);
    }

    @Override
    public Object map(ObjectPathParserContext context, Function<Object, Object> mapper) {
        // 此处不可直接返回null
        return objectAccessor.map(cv,mapper);
    }

    @Override
    public Object filter(ObjectPathParserContext context, Predicate<Object> filter) {
        // 此处不可直接返回null
        return objectAccessor.filter(cv,filter);
    }

    @Override
    public int size() {
        if (Objects.isNull(cv)){
            return 0;
        }
        return objectAccessor.size(cv);
    }

    @Override
    public List<String> keys() {
        return Collections.emptyList();
    }

    @Override
    public List<String> values() {
        return Collections.emptyList();
    }

    @Override
    public List<String> reverse() {
        return Collections.emptyList();
    }

    @Override
    public List<Object> covertToList() {
        if (cv.isNull()){
            return configuration.isUseZeroForNull()?Collections.emptyList():null;
        }
        return objectAccessor.covertToList(cv);
    }

    @Override
    public String covertToString(PropertyWrapper object) {
        if (object.isNull()){
            return configuration.isUseZeroForNull()?"":null;
        }
        return objectAccessor.covertToString(object);
    }

    @Override
    public Number covertToNumber(PropertyWrapper object) {
        if (object.isNull()){
            return configuration.isUseZeroForNull()?0:null;
        }
        if (object.isInstanceOf(Number.class)){
            return object.read(Number.class);
        }
        if (object.isInstanceOf(String.class)){
            return Double.parseDouble(object.read(String.class));
        }
        return objectAccessor.covertToNumber(object);
    }

    @Override
    public PropertyWrapper parseJson(String json) {
        return this.configuration.getWrapperFactory().create(jsonCoder.decode(json,Object.class));
    }

    @Override
    public PropertyWrapper rewrite(Object object) {
        return this.configuration.getWrapperFactory().create(this.objectRewrite.rewrite(object).getRewrite());
    }

    @Override
    public PropertyWrapper invokeMethod(String functionName, List<Object> params) {
        FunctionProvider fp = this.functionManager.getProvider(functionName, params);
        if (fp!=null){
            return this.configuration.getWrapperFactory().create(fp.apply(params));
        }
        throw new UnsupportedOperationException("not support function "+functionName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyWrapper executeScript(String scriptName, Object nas, String text) {
        ScriptExecutor executor = this.scriptExecutorManager.getExecutor(scriptName);
        if (executor!=null){
            return this.configuration.getWrapperFactory().create(executor.eval(text, (Map<String, Object>) nas));
        }
        return null;
    }

}
