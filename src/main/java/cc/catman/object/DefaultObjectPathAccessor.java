package cc.catman.object;

import cc.catman.object.core.DefaultObjectPathParserContext;
import cc.catman.object.core.ObjectPathVisitor;
import cc.catman.object.path.standard.ObjectPathParser;
/**
 * 默认ObjectPath实现
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultObjectPathAccessor implements ObjectPathAccessor{
    /**
     * 解析器
     */
    private final ObjectPathParser parser;
    /**
     * 配置
     */
    private final ObjectPathConfiguration config;

    public DefaultObjectPathAccessor(ObjectPathParser parser, ObjectPathConfiguration config) {
        this.parser = parser;
        this.config = config;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T eval(Object object, Class<T> clazz) {
        DefaultObjectPathParserContext ctx = createContext(object);
        ObjectPathVisitor visitor = new ObjectPathVisitor(ctx);
        Object result = parser.objectPath().accept(visitor);
        return (T) result;
    }

    public DefaultObjectPathParserContext createContext(Object root){
        return new DefaultObjectPathParserContext(
                root,
                root,
                null,
                config
        );
    }
}
