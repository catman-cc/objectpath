package cc.catman.object;

import cc.catman.object.path.standard.ObjectPathLexer;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * 默认ObjectPath实现
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
public class ObjectPath implements IObjectPath {

    /**
     * 配置信息
     */
    private ObjectPathConfiguration config;

    /**
     * 解析
     * @param path   路径
     * @return 对象
     */
    public static ObjectPathAccessor parse(String path, ObjectPathConfiguration config) {
        return new ObjectPath(config).parse(path);
    }

    @Override
    public ObjectPathAccessor parse(String path) {
        CodePointCharStream stream = CharStreams.fromString(path);
        ObjectPathLexer lexer = new ObjectPathLexer(stream);
        ObjectPathParser opp = new ObjectPathParser(new CommonTokenStream(lexer));
        return new DefaultObjectPathAccessor(opp,config);
    }
}
