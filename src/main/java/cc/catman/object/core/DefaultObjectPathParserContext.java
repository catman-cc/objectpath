package cc.catman.object.core;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.ObjectAccessor;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.function.FunctionProvider;
import cc.catman.object.core.json.JsonCoder;
import cc.catman.object.core.rewrite.ObjectRewrite;
import cc.catman.object.core.script.ScriptExecutor;
import cc.catman.object.core.script.ScriptExecutorManager;
import lombok.Getter;

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
    private final Object root;
    /**
     * 父对象,需要注意,此处的父对象并不是指当前对象的父对象,而是指当前上下文的创建者所持有的cv
     */
    private Object pv;
    /**
     * 当前对象
     */
    private Object cv;

    /**
     * 父上下文
     */
    private ObjectPathParserContext parent;

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
    private FunctionManager functionManager;
    /**
     * json解析器
     */
    private final JsonCoder jsonCoder;

    /**
     * 脚本执行器管理器
     */
    private final ScriptExecutorManager scriptExecutorManager;

    @Getter
    private ObjectPathConfiguration configuration;

    public DefaultObjectPathParserContext(Object root, ObjectPathConfiguration configuration) {
       this(root,root,configuration);
    }

    public DefaultObjectPathParserContext(Object root, Object cv, ObjectPathConfiguration configuration) {
      this(root,cv,null,configuration);
    }

    public DefaultObjectPathParserContext(Object root, Object cv, ObjectPathParserContext parent, ObjectPathConfiguration configuration) {
        this.root = root;
        this.cv = cv;
        this.parent = parent;
        this.configuration = configuration;
        this.objectAccessor=configuration.getObjectAccessor();
        this.objectRewrite=configuration.getObjectRewrite();
        this.jsonCoder=configuration.getJsonCoder();
        this.scriptExecutorManager=configuration.getScriptExecutorManager();
        this.functionManager=configuration.getFunctionManager();
    }

    public DefaultObjectPathParserContext(Object root, Object cv, ObjectAccessor objectAccessor, ObjectRewrite objectRewrite, FunctionManager functionManager, JsonCoder jsonCoder, ScriptExecutorManager scriptExecutorManager) {
        this.root = root;
        this.cv = cv;
        this.objectAccessor = objectAccessor;
        this.objectRewrite = objectRewrite;
        this.functionManager = functionManager;
        this.jsonCoder = jsonCoder;
        this.scriptExecutorManager = scriptExecutorManager;
    }


    @Override
    public ObjectPathParserContext parent() {
        return parent;
    }

    @Override
    public Object rootValue() {
        return root;
    }

    @Override
    public Object parentValue() {
        return pv;
    }

    @Override
    public Object currentValue() {
        return cv;
    }

    @Override
    public void updateCurrent(Object current) {
        this.cv = current;
    }

    @Override
    public void updateParent(Object parent) {
        this.pv = parent;
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
        DefaultObjectPathParserContext context = new DefaultObjectPathParserContext(
                root,
                current,
                this,
                this.configuration
        );
        context.pv = parent;
        return context;
    }

    @Override
    public ObjectPathParserContext createChild(Object current) {
        return createContext(root,null,current);
    }

    @Override
    public ObjectPathParserContext exit() {
        return Optional.ofNullable(this.parent).orElse(this);
    }

    @Override
    public Object eval(String path) {
        return objectAccessor.get(cv, path);
    }

    @Override
    public void eachKey(ObjectPathParserContext context, Consumer<Object> consumer) {
        objectAccessor.eachKey(cv,consumer);
    }

    @Override
    public void each(ObjectPathParserContext context, Consumer<Entity> consumer) {
        objectAccessor.eachEntry(cv,consumer);
    }

    @Override
    public void eachValue(ObjectPathParserContext context, Consumer<Object> consumer) {
        objectAccessor.eachValue(cv,consumer);
    }

    @Override
    public Object map(ObjectPathParserContext context, Function<Object, Object> mapper) {
        return objectAccessor.map(cv,mapper);
    }

    @Override
    public Object filter(ObjectPathParserContext context, Predicate<Object> filter) {
        return objectAccessor.filter(cv,filter);
    }

    @Override
    public int size() {
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
        if (cv==null){
            if (configuration.isAutoCreateCollectionWhenInvokeMethod()){
               return objectAccessor.covertToList(new ArrayList<>());
            }
        }
        return objectAccessor.covertToList(cv);
    }

    @Override
    public String covertToString(Object object) {
        return objectAccessor.covertToString(object);
    }

    @Override
    public Number covertToNumber(Object object) {
        if (object==null){
            return null;
        }
        if (object instanceof Number){
            return (Number) object;
        }
        if (object instanceof String){
            return Double.parseDouble((String) object);
        }
        return objectAccessor.covertToNumber(object);
    }

    @Override
    public Object parseJson(String json) {
        return jsonCoder.decode(json,Object.class);
    }

    @Override
    public Object rewrite(Object object) {
        return this.objectRewrite.rewrite(object).getRewrite();
    }

    @Override
    public Object invokeMethod(String functionName, List<Object> params) {
        FunctionProvider fp = this.functionManager.getProvider(functionName, params);
        if (fp!=null){
            return fp.apply(params);
        }
        throw new UnsupportedOperationException("not support function "+functionName);
    }

    @Override
    public Object executeScript(String scriptName, Object nas, String text) {
        ScriptExecutor executor = this.scriptExecutorManager.getExecutor(scriptName);
        if (executor!=null){
            return executor.eval(text, (Map<String, Object>) nas);
        }
        return null;
    }
}
