package cc.catman.object;

import cc.catman.object.path.standard.ObjectPathLexer;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

@Data
@Builder
public class DefaultObjectPath implements ObjectPath{

    private ObjectPathConfiguration config;


    @Override
    public ObjectPathAccessor parse(String path) {
        CodePointCharStream stream = CharStreams.fromString(path);
        ObjectPathLexer lexer = new ObjectPathLexer(stream);
        ObjectPathParser opp = new ObjectPathParser(new CommonTokenStream(lexer));
        return new DefaultObjectPathAccessor(opp,config);
    }
}
