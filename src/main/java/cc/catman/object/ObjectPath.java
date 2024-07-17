package cc.catman.object;

import cc.catman.object.core.error.StandardErrorListener;
import cc.catman.object.path.standard.ObjectPathLexer;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;

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
        lexer.addErrorListener(new StandardErrorListener(this.config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ObjectPathParser opp = new ObjectPathParser(tokens);
        opp.getInterpreter().setPredictionMode(PredictionMode.SLL);
        opp.removeErrorListeners();

        opp.addErrorListener(new StandardErrorListener(this.config));
        if (this.config.isSyntaxErrorStop()){
            opp.setErrorHandler(new BailErrorStrategy());
        }

        ObjectPathParser.ObjectPathContext objectPathContext;
        try {
            objectPathContext = opp.objectPath();
        }catch (Exception e){
            tokens.seek(0);
            opp.reset();
            opp.getInterpreter().setPredictionMode(PredictionMode.LL);
            objectPathContext=opp.objectPath();
        }

        return new DefaultObjectPathAccessor(opp,config,objectPathContext);
    }
}
