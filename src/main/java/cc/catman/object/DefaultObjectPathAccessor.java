package cc.catman.object;

import cc.catman.object.core.DefaultObjectPathParserContext;
import cc.catman.object.core.EContextMod;
import cc.catman.object.core.ObjectPathParserContext;
import cc.catman.object.core.ObjectPathVisitor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Getter;
import org.antlr.v4.runtime.atn.PredictionMode;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认ObjectPath实现
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultObjectPathAccessor implements ObjectPathAccessor{
    /**
     * 解析器
     */
    @Getter
    private final ObjectPathParser parser;

    private ObjectPathParser.ObjectPathContext objectPathContext;

    private final AtomicBoolean needReset=new AtomicBoolean(false);
    /**
     * 配置
     */
    private final ObjectPathConfiguration config;

    public DefaultObjectPathAccessor(ObjectPathParser parser, ObjectPathConfiguration config) {
        this.parser = parser;
        this.config = config;
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        try {
            this.objectPathContext=parser.objectPath();
        }catch (Exception e){
            parser.reset();
        }
    }

    public DefaultObjectPathAccessor(ObjectPathParser parser, ObjectPathConfiguration config, ObjectPathParser.ObjectPathContext objectPathContext) {
        this.parser = parser;
        this.config = config;
        this.objectPathContext = objectPathContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T eval(Object object, Class<T> clazz) {
        reset();
        ObjectPathParserContext ctx;
        if (object instanceof ObjectPathParserContext){
            ctx= (ObjectPathParserContext) object;
        }else {
            ctx = createContext(object);
        }

        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);

        ctx.setMod(EContextMod.READ_ONLY);

        PropertyWrapper result =opc.accept(visitor);
        return result.read(clazz);
    }

    /**
     * 设值,并会将过滤后的数据传递给赋值表达式,在完成计算后,重新赋值
     *
     * @param object 对象
     * @param expr   表达式
     */
    @Override
    public void setExpr(Object object, String expr) {
        reset();

        DefaultObjectPathParserContext ctx = createContext(object);
        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);


        ctx.setMod(EContextMod.READ_ONLY);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);


        PropertyWrapper result =opc.accept(visitor);

        // 创建新的上下文,该上下文用来执行赋值表达式
        DefaultObjectPathParserContext newCtx = createContext(result);
        newCtx.setMod(EContextMod.READ_ONLY);

        ObjectPathAccessor valueParse = ObjectPath.parse(expr, config);
        Object sv = valueParse.eval(newCtx);
        result.set(sv);
    }

    private void reset() {
        if (needReset.get()){
           this.parser.reset();
       }
        this.needReset.set(true);
    }

    @Override
    public void setExpr(Object object,ObjectPathParser.ObjectPathContext objectPath){
        reset();
        DefaultObjectPathParserContext ctx = createContext(object);
        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);

        ctx.setMod(EContextMod.READ_ONLY);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);
        PropertyWrapper result =opc.accept(visitor);
        // 创建新的上下文,该上下文用来执行赋值表达式
        DefaultObjectPathParserContext newCtx = createContext(result);
        newCtx.setMod(EContextMod.READ_ONLY);
        ObjectPathVisitor newVisitor = new ObjectPathVisitor(newCtx);
        PropertyWrapper sv = objectPath.accept(newVisitor);
        result.set(sv.read());

    }

    /**
     * 设值
     *
     * @param object 对象
     * @param value  新值
     */
    @Override
    public void setValue(Object object, Object value) {
        reset();
        DefaultObjectPathParserContext ctx = createContext(object);
        ctx.setMod(EContextMod.READ_ONLY);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);
        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);
        PropertyWrapper result =opc.accept(visitor);
        result.set(value);
    }

    public DefaultObjectPathParserContext createContext(Object root){
        return new DefaultObjectPathParserContext(
                this.config.getWrapperFactory().create(root),
                this.config.getWrapperFactory().create(root),
                null,
                config
        );
    }

    public DefaultObjectPathParserContext createContext(PropertyWrapper pw){
        PropertyWrapper root=pw;
        Optional<PropertyWrapper> belong = pw.getBelong();
        while(belong.isPresent()){
            root= belong.get();
            belong = root.getBelong();
        }
        return new DefaultObjectPathParserContext(
                root,
                pw,
                null,
                config
        );
    }
}
