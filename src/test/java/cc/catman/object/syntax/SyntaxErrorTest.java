package cc.catman.object.syntax;

import cc.catman.object.ObjectPath;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.cases.Order.OrderMock;
import cc.catman.object.core.exception.ObjectPathSyntaxRuntimeException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import org.junit.Test;

public class SyntaxErrorTest extends BaseErrorListener {
    /**
     * 有语法错误,并且会被抛出
     */
    @Test
    public void hasSyntaxError() {
        try {
            ObjectPathAccessor parse = createParse(false,true);
            parse.eval(OrderMock.mockOrder());
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ParseCancellationException);
            Assert.assertTrue(e.getCause() instanceof ObjectPathSyntaxRuntimeException);
            Assert.assertTrue(true);
        }
    }

    /**
     * 无语法错误,但有可能又因为语法错误,导致获取到错误的语法树,从而导致无法正确解析
     */
    @Test
    @SuppressWarnings("all")
    public void noSyntaxError() {
        try {
            ObjectPathAccessor parse = createParse(true,false);
            parse.eval(OrderMock.mockOrder());
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertFalse(e instanceof ObjectPathSyntaxRuntimeException);
            Assert.assertFalse(e instanceof RecognitionException);
        }
    }

    @Test
    public void hasObjectPathSyntaxRuntimeException() {
        try {
            ObjectPathAccessor parse = createParse(false,false);
            parse.eval(OrderMock.mockOrder());
        } catch (Exception e) {
            Assert.assertFalse(e instanceof ObjectPathSyntaxRuntimeException);
            Assert.assertFalse(e instanceof RecognitionException);
        }
    }

    private static ObjectPathAccessor createParse(
            boolean preventSyntaxErrorAndSendToErrorLog
            ,boolean syntaxErrorStop
    ) {
        ObjectPathConfiguration configuration =ObjectPathConfiguration.create();


        configuration
                .setPreventSyntaxErrorAndSendToErrorLog(preventSyntaxErrorAndSendToErrorLog);
        configuration.setSyntaxErrorStop(syntaxErrorStop);

        ObjectPath op = ObjectPath.builder()
                .config(configuration)
                .build();
        return op.parse("$.items[?((@price--@costPrice) * @quantity >2 )].count()");
    }
}
