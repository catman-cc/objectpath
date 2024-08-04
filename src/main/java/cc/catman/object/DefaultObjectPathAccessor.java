package cc.catman.object;

import cc.catman.object.core.DefaultObjectPathParserContext;
import cc.catman.object.core.EContextMod;
import cc.catman.object.core.ObjectPathParserContext;
import cc.catman.object.core.ObjectPathVisitor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.util.ReflectionHelper;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Getter;
import org.antlr.v4.runtime.atn.PredictionMode;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认ObjectPath实现
 *
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultObjectPathAccessor implements ObjectPathAccessor {
    /**
     * 解析器
     */
    @Getter
    private final ObjectPathParser parser;

    private ObjectPathParser.ObjectPathContext objectPathContext;

    private final AtomicBoolean needReset = new AtomicBoolean(false);
    /**
     * 配置
     */
    private final ObjectPathConfiguration config;

    public DefaultObjectPathAccessor(ObjectPathParser parser, ObjectPathConfiguration config) {
        this.parser = parser;
        this.config = config;
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        try {
            this.objectPathContext = parser.objectPath();
        } catch (Exception e) {
            parser.reset();
        }
    }

    public DefaultObjectPathAccessor(ObjectPathParser parser, ObjectPathConfiguration config, ObjectPathParser.ObjectPathContext objectPathContext) {
        this.parser = parser;
        this.config = config;
        this.objectPathContext = objectPathContext;
    }

    @Override
    public <T> T eval(Object object, Class<T> clazz) {
        reset();
        ObjectPathParserContext ctx;
        if (object instanceof ObjectPathParserContext) {
            ctx = (ObjectPathParserContext) object;
        } else {
            ctx = createContext(object, EContextMod.READ_ONLY);
        }

        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);


        PropertyWrapper result = opc.accept(visitor);
        return result.read(clazz);
    }

    /**
     * 设值,并会将过滤后的数据传递给赋值表达式,在完成计算后,重新赋值
     *
     * @param object 对象
     * @param expr   表达式
     */
    @Override
    public Object setExpr(Object object, String expr) {
        reset();

        DefaultObjectPathParserContext ctx = createContext(object, EContextMod.WRITE_MOD);
        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);


        ctx.setMod(EContextMod.WRITE_MOD);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);


        PropertyWrapper result = opc.accept(visitor);
        ctx.checkWrite();



        ObjectPathAccessor valueParse = ObjectPath.parse(expr, config);
        if (result.isInstanceOf(Collection.class)) {
            if (result.isNotNull()) {
                Collection<?> c = (Collection<?>) result.read();
                Optional<?> any = c.stream()
                        .filter(Objects::nonNull)
                        .findAny();

                boolean wrapped = any
                        .map(v -> ReflectionHelper.isAssignableFrom(PropertyWrapper.class, v.getClass()))
                        .orElse(false);
                if (wrapped) {
                    c.stream().map(v -> (PropertyWrapper) v).forEach(v -> {
                        // 创建新的上下文,该上下文用来执行赋值表达式
                        DefaultObjectPathParserContext newCtx = createContext(object,v);
                        newCtx.setMod(EContextMod.READ_ONLY);
                        Object sv = valueParse.eval(newCtx);
                        v.set(sv);
                    });
                    return c;
                }
            }

        }
        // 创建新的上下文,该上下文用来执行赋值表达式
        DefaultObjectPathParserContext newCtx = createContext(result);
        newCtx.setMod(EContextMod.READ_ONLY);
        Object sv = valueParse.eval(newCtx);
        result.set(sv);
        return result.read();
    }

    private void reset() {
        if (needReset.get()) {
            this.parser.reset();
        }
        this.needReset.set(true);
    }

    @Override
    public Object setExpr(Object object, ObjectPathParser.ObjectPathContext objectPath) {
        reset();
        DefaultObjectPathParserContext ctx = createContext(object, EContextMod.WRITE_MOD);
        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);

        ctx.setMod(EContextMod.WRITE_MOD);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);
        PropertyWrapper result = opc.accept(visitor);
        // 创建新的上下文,该上下文用来执行赋值表达式
        DefaultObjectPathParserContext newCtx = createContext(result);
        newCtx.setMod(EContextMod.READ_ONLY);
        ObjectPathVisitor newVisitor = new ObjectPathVisitor(newCtx);
        PropertyWrapper sv = objectPath.accept(newVisitor);
        result.set(sv.read());
        return result.read();
    }

    /**
     * 设值
     *
     * @param object 对象
     * @param value  新值
     */
    @Override
    public Object setValue(Object object, Object value) {
        reset();
        DefaultObjectPathParserContext ctx = createContext(object, EContextMod.WRITE_MOD);

        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);
        ObjectPathParser.ObjectPathContext opc = Optional.ofNullable(this.objectPathContext)
                .orElseGet(parser::objectPath);
        PropertyWrapper result = opc.accept(visitor);
        ctx.checkWrite();
        result.set(value);
        return result.read();
    }

    public DefaultObjectPathParserContext createContext(Object root, EContextMod mod) {
        DefaultObjectPathParserContext context;
        if (mod.isReadOnly()) {
            context = new DefaultObjectPathParserContext(
                    this.config.getWrapperFactory().createReadOnly(root),
                    this.config.getWrapperFactory().createReadOnly(root),
                    null,
                    config
            );
        } else {
            context = new DefaultObjectPathParserContext(
                    this.config.getWrapperFactory().create(root),
                    this.config.getWrapperFactory().create(root),
                    null,
                    config
            );
        }
        context.setMod(mod);
        return context;
    }

    public DefaultObjectPathParserContext createContext(PropertyWrapper pw) {
        PropertyWrapper root = pw;
        Optional<PropertyWrapper> belong = pw.getBelong();
        while (belong.isPresent()) {
            root = belong.get();
            belong = root.getBelong();
        }
        return new DefaultObjectPathParserContext(
                root,
                pw,
                null,
                config
        );
    }

    public DefaultObjectPathParserContext createContext(Object root,PropertyWrapper pw) {
        return new DefaultObjectPathParserContext(
                this.config.getWrapperFactory().createReadOnly(root),
                pw,
                null,
                config
        );
    }
}
