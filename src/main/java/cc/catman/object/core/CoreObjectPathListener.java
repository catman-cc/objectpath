package cc.catman.object.core;


import cc.catman.object.path.standard.ObjectPathBaseListener;
import cc.catman.object.path.standard.ObjectPathParser;

public class CoreObjectPathListener extends ObjectPathBaseListener {
    /**
     * 解析器上下文,该上下文会存放当前正在解析的对象
     */
    protected ObjectPathParserContext context;

    /**
     * 访问表达式,每一个表达式都会有一个独立的上下文,用于存放当前解析的对象
     * @param ctx the parse tree
     */
    @Override
    public void enterExpr(ObjectPathParser.ExprContext ctx) {
        super.enterExpr(ctx);
    }

    /**
     * 访问表达式,每一个表达式都会有一个独立的上下文,用于存放当前解析的对象,因此需要在表达式退出时,退出当前上下文
     * @param ctx the parse tree
     */
    @Override
    public void exitExpr(ObjectPathParser.ExprContext ctx) {
        super.exitExpr(ctx);
    }
}
