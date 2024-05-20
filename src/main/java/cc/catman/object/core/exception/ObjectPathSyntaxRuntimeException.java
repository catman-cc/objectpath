package cc.catman.object.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * ObjectPath语法解析运行时异常
 * @author jpanda
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ObjectPathSyntaxRuntimeException extends RuntimeException {
   private final transient Recognizer<?, ?> recognizer;
   private final transient Object offendingSymbol;
   private final int line;
   private final int charPositionInLine;
   private final String msg;
   private final RecognitionException e;

    public ObjectPathSyntaxRuntimeException(String message, Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super(message);
        this.recognizer = recognizer;
        this.offendingSymbol = offendingSymbol;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.msg = msg;
        this.e = e;
    }

}
