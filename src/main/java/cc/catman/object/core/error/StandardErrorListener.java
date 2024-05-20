package cc.catman.object.core.error;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.exception.ObjectPathSyntaxRuntimeException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * 标准错误监听器
 *
 * @author jpanda
 * @since 0.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class StandardErrorListener extends BaseErrorListener {

    /**
     * 配置信息
     */
    protected ObjectPathConfiguration configuration;

    public StandardErrorListener(ObjectPathConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String oldMsg = msg;
        if (offendingSymbol instanceof CommonToken) {
            CommonToken ct = (CommonToken) offendingSymbol;
            String string = ct.getTokenSource().getInputStream().toString();
            String start = string.substring(0, ct.getStartIndex()) + "👉🏻";
            String errorText = string.substring(ct.getStartIndex(), ct.getStopIndex() + 1);
            String end = "👈🏻" + string.substring(ct.getStopIndex() + 1);
            String tip = "user input content:              \t" + string;
            String hint = "error content between 👉🏻 and 👈🏻 :\t" + start + errorText + end;

            msg = msg
                  + "\r\n========================================================================================"
                  + "\r\ndetails:\r\n"
                  + "\t" + tip + "\r\n"
                  + "\t" + hint
                  + "\r\n========================================================================================"
            ;
        }
        ObjectPathSyntaxRuntimeException exception = new ObjectPathSyntaxRuntimeException(msg, recognizer, offendingSymbol, line, charPositionInLine, oldMsg, e);
        if (configuration.isPreventSyntaxErrorAndSendToErrorLog()) {
            log.error("syntax error:line:{}:{},msg:{}", line, charPositionInLine, msg, exception);
        } else {
            throw exception;
        }
    }
}
