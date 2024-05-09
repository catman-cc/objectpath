package cc.catman.object.core;


import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.path.standard.ObjectPathBaseVisitor;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 对象路径访问器,用于访问对象路径表达式
 *
 * @author jpanda
 * @since 0.0.1
 */
public class ObjectPathVisitor extends ObjectPathBaseVisitor<Object> {
    /**
     * 解析器上下文,该上下文会存放当前正在解析的对象
     */
    @Getter
    protected ObjectPathParserContext context;

    @Getter
    protected ObjectPathConfiguration config;

    /**
     * 是否停止解析
     */
    protected boolean stop = false;

    public ObjectPathVisitor(ObjectPathParserContext context) {
        this.context = context;
        this.config = context.getConfiguration();
    }

    @Override
    public Object visitObjectPath(ObjectPathParser.ObjectPathContext ctx) {
        // 需要为每一个通道创建一个新的上下文
        //  获取表达式结果
        Object res = visit(ctx.expr());
        this.context.updateParent();
        this.context.updateCurrent(res);
        // 开始遍历通道
        List<ObjectPathParser.PipeContext> pipes = ctx.pipe();
        for (ObjectPathParser.PipeContext pipe : pipes) {
            res = this.visit(pipe);
        }
        return res;
    }

    @Override
    public Object visitPipe(ObjectPathParser.PipeContext ctx) {
        // 创建新的上下文,需要注意的是,在通道中,上下文是独立的,不会相互影响
        Object cv = this.context.currentValue();
        this.context = this.context.createContext(cv, null, cv);
        // 创建新的访问器
        // 访问通道,获取值
        cv = this.visit(ctx.expr());
        // 更新上下文
        // 更新当前上下文
        return cv;
    }

    /**
     * 访问表达式,每一个表达式都会有一个独立的上下文,用于存放当前解析的对象
     * 当表达式解析完毕后,会退出该表达式,并将上下文重置为之前的上下文对象
     *
     * @param ctx the parse tree
     * @return 表达式的执行结果
     */
    @Override
    public Object visitExpr(ObjectPathParser.ExprContext ctx) {
        // 切换上下文
        ObjectPathParserContext nc = switchContextWithExpr(ctx);
        // 持有新上下文的访问器
        ObjectPathVisitor childVisitor = new ObjectPathVisitor(nc);
        // 上下文切换完成后,访问选择器,选择器将会切换当前上下文对象
        List<ObjectPathParser.SelectorContext> selector = ctx.selector();
        // 遍历选择器,每次遍历都会切换上下文
        Object result = nc.currentValue();

        for (ObjectPathParser.SelectorContext s : selector) {
            // 遇到null值,是否停止解析
            if (config.isStopParseWhenNull() && (result == null)) {
                return null;

            }
            if (childVisitor.stop) {
                return result;
            }
            // 此处应该切换上下文
            result = childVisitor.visit(s);
            childVisitor.context.updateCurrent(childVisitor.context.currentValue());
            childVisitor.context.updateCurrent(result);
        }
        // 当表达式退出时,需要将上下文重置为之前的上下文,即退出当前上下文
        return result;
    }

    /**
     * 切换上下文,切换上下文有两种方式
     * 一种是指定表达式的位置,切换上下文,即location模式
     * 另一种是直接访问当前上下文的子对象,即ID模式
     *
     * @param ctx the parse tree
     */
    public ObjectPathParserContext switchContextWithExpr(ObjectPathParser.ExprContext ctx) {
        if (Optional.ofNullable(ctx.location()).isPresent()) {
            ELocation l = ELocation.fromLocation(ctx.location().getText());
            return this.context.createChildContext(l);
        }
        if (Optional.ofNullable(ctx.ID()).isPresent()) {
            String id = ctx.ID().getText();
            return this.context.createChild(this.context.eval(id));
        }
        // never reach here
        return this.context;
    }

    /**
     * 基于纯数字的索引,访问集合内的元素
     * 如果当前对象不是一个集合,则判断当前对象是否为map,如果是map则根据key获取value
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitINDEX(ObjectPathParser.INDEXContext ctx) {
        // 获取待访问的索引值
        TerminalNode index = ctx.NUMBER();
        // 访问索引,获取索引对应的对象
        this.context.updateCurrent(this.context.currentValue());
        return this.context.eval(index.getText());
    }

    /**
     * 访问子对象,此时目标对象可以是数组,集合,Map,对象,只要不是基本类型即可
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitINDEX_OR_NAME(ObjectPathParser.INDEX_OR_NAMEContext ctx) {
        // 获取待访问的索引值
        TerminalNode index = ctx.ID();
        // 访问索引,获取索引对应的对象
        Object obj = this.context.eval(index.getText());
        // 切换上下文
        this.context = this.context.createChild(obj);
        return obj;
    }

    @Override
    public Object visitCHILD(ObjectPathParser.CHILDContext ctx) {
        String id = ctx.ID().getText();
        Object obj = this.context.eval(id);
        this.context = this.context.createChild(obj);
        return obj;
    }

    /**
     * 通配符访问,表示访问所有子元素,并将子元素的筛选结果作为一个新的集合返回
     * 要求当前对象必须是一个集合
     * 需要此时注意的时,此时将会创建多个上下文,每个上下文都会有一个子对象
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitWILDCARD_ALL(ObjectPathParser.WILDCARD_ALLContext ctx) {
        this.stop = true;
        // 获取当前对象,此时需要判断当前对象是否为集合,如果不是集合,则抛出异常
        // 遍历操作由上下文对象完成,注意此时,上下文产生了分值,在通配符后的所有选择器都会在子上下文中执行
        ParserRuleContext parent = ctx.getParent();
        // 获取后续的操作,后续的操作将会在子上下文中执行
        int cc = parent.getChildCount();
        int index = IntStream.range(0, cc).filter(i -> parent.getChild(i).equals(ctx)).findFirst().orElse(0);

        if (index == cc - 1) {
            return this.context.currentValue();
        }
        List<ParseTree> children = parent.children;
        // 遍历当前对象,执行后续操作
        return this.context.map(obj -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createContext(this.context.rootValue(), this.context.currentValue(), obj);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            // 然后使用子访问器访问后续操作
            // 提取后续操作
            Object result = childVisitor.context.currentValue();
            for (int i = index + 1; i < cc; i++) {
                ParseTree parseTree = children.get(i);
                result = childVisitor.visit(parseTree);
                if (i < cc - 1) {
                    // 继续切换,上一次的结果作为当前对象,上次被访问的对象作为父对象
                    childVisitor = new ObjectPathVisitor(childVisitor.context.createContext(childVisitor.context.rootValue(), childVisitor.context.currentValue(), result));
                }
            }
            return result;
        });
    }

    /**
     * 递归下降,获取所有匹配的子元素,并将子元素的筛选结果作为一个新的集合返回
     * 递归下降的操作本质上是通过子表达式筛选出符合条件的元素,然后将这些元素作为一个新的集合返回.
     * 他的实现和通配符访问类似,都需要中断后续子表达式的自动执行,并将其作为筛选条件来处理
     * 比如,$..name,本质上是递归获取所有元素的name属性,然后将这些name属性作为一个新的集合返回
     * 再比如$..book[?(@.price<10)],本质上是递归获取所有价格小于10的书籍,然后将这些书籍作为一个新的集合返回
     * 其有一个隐藏的filter操作,即对父元素筛选持有某一个属性的元素
     */
    @Override
    public Object visitRECURSIVE_CHILD(ObjectPathParser.RECURSIVE_CHILDContext ctx) {
        // 获取当前对象,此时需要判断当前对象是否为集合,如果不是集合,则抛出异常
        // 遍历操作由上下文对象完成,注意此时,上下文产生了分值,在通配符后的所有选择器都会在子上下文中执行
        ParserRuleContext parent = ctx.getParent();
        // 获取后续的操作,后续的操作将会在子上下文中执行
        int cc = parent.getChildCount();
        int index = IntStream.range(0, cc).filter(i -> parent.getChild(i).equals(ctx)).findFirst().orElse(0);
        // 注意此处的id将会用来匹配子属性的名称,正常来说,其不应该匹配到集合的索引
        String id = ctx.ID().getText();
        // 获取后续需要执行的操作
        List<ParseTree> nextChildren = parent.children.subList(index + 1, cc);

        List<Object> objects = new ArrayList<>();
        // 递归获取所有子元素
        recursiveChild(this.context.currentValue(), id, nextChildren, objects);
        this.context.updateCurrent(objects);
        return objects;
    }

    /**
     * 访问指定对象的所有元素,并将所有元素作为一个新的集合返回,该操作只能在map和普通对象上执行
     * 如果在集合上执行,等同于通配符访问
     * 该操作只能在集合上执行
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitALL(ObjectPathParser.ALLContext ctx) {
        List<Object> list = new ArrayList<>();
        this.context.eachValue(list::add);
        this.context.updateCurrent(list);
        return list;
    }

    public void recursiveChild(final Object obj, String id, List<ParseTree> nextChildren, List<Object> objects) {
        this.context.each(entity -> {
            Object result = entity.value();
            ObjectPathParserContext child = this.context.createContext(this.context.rootValue(), obj, result);
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);

            if (Optional.ofNullable(entity.key()).filter(id::equals).isPresent()) {
                for (int i = 0; i < nextChildren.size(); i++) {
                    result = childVisitor.visit(nextChildren.get(i));
                    if (childVisitor.stop) {
                        break;
                    }
                    if (i < nextChildren.size() - 1) {
                        // 继续切换,上一次的结果作为当前对象,上次被访问的对象作为父对象
                        childVisitor = new ObjectPathVisitor(childVisitor.context.createContext(childVisitor.context.rootValue(), childVisitor.context.currentValue(), result));
                    }
                }
                objects.add(result);
            } else {
                childVisitor.recursiveChild(result, id, nextChildren, objects);
            }
        });
    }

    /**
     * 调用方法
     */
    @Override
    public Object visitMETHOD_CALL(ObjectPathParser.METHOD_CALLContext ctx) {
        String functionName = unquote(ctx.ID().getText());
        List<Object> params = new ArrayList<>();
        Optional.ofNullable(ctx.args())
                .ifPresent(args -> {
                    Object res = this.visit(args);
                    if (res instanceof List) {
                        params.addAll((List<?>) res);
                    } else {
                        params.add(res);
                    }
                });
        return this.context.invokeMethod(functionName, params);
    }

    /**
     * 访问切片,切片操作将会返回一个新的集合,该集合包含了切片范围内的所有元素
     * 切片操作只能在集合上执行
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitSLICE(ObjectPathParser.SLICEContext ctx) {
        // 获取切片的起始位置和结束位置
        List<TerminalNode> numbers = ctx.NUMBER();
        int start = Integer.parseInt(numbers.get(0).getText());
        int end = Integer.parseInt(numbers.get(1).getText());
        // 获取当前对象,此时需要判断当前对象是否为集合,如果不是集合,则抛出异常
        List<Object> old = this.context.covertToList();
        if (this.config.isAutoCreateCollectionWhenInvokeMethod()) {
            old = Optional.ofNullable(old).orElseGet(ArrayList::new);
        }
        // 然后根据切片的范围,获取切片的元素
        // 通常情况下,切片操作是左闭右开的,即[start,end)
        // 如果同时包含start和end,则需要将end+1?
        // 如果start未设置,则默认为0
        // 如果end未设置,则默认为size
        if (start < 0) {
            start = 0;
        }
        if (end > old.size()) {
            end = old.size();
        }
        return old.subList(start, end);
    }

    /**
     * 访问过滤器,过滤器将会过滤集合/对象中的元素,只有符合条件的元素才会被保留
     * 理论上过滤器只会缩小集合的范围,不会扩大集合的范围
     * 过滤器过滤对象的场景,需要根据对象的属性进行过滤,只有符合条件的对象才会被保留
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitFILTER(ObjectPathParser.FILTERContext ctx) {
        // 获取过滤条件
        ObjectPathParser.FilterExprContext f = ctx.filterExpr();
        Object res = this.visit(f);
        this.context.updateCurrent(res);
        return res;
    }

    @Override
    public Object visitFilterExpr(ObjectPathParser.FilterExprContext ctx) {
        if (this.config.isAutoCreateCollectionWhenInvokeMethod()
            && (!Optional.ofNullable(this.context.currentValue()).isPresent())) {
            this.context.updateCurrent(new ArrayList<>());

        }
        Object res = this.context.filter((o -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);

            Object match = false;
            if (Optional.ofNullable(ctx.comparExpr()).isPresent()) {
                match = childVisitor.visit(ctx.comparExpr());
            } else if (Optional.ofNullable(ctx.reverseComparExpr()).isPresent()) {
                match = childVisitor.visit(ctx.reverseComparExpr());
            } else if (Optional.ofNullable(ctx.scripts()).isPresent()) {
                match = childVisitor.visit(ctx.scripts());
            }
            return match instanceof Boolean && (Boolean) match;
        }));
        this.context.updateCurrent(res);
        return res;
    }

    /**
     * 访问比较操作,比较操作将会比较两个对象的大小,如果比较结果为true,则保留,否则丢弃
     * 比较操作只能在集合上执行
     * 但此处只会返回boolean值,不会返回集合
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitEQ(ObjectPathParser.EQContext ctx) {
        Object left = visit(ctx.getChild(0));
        Object right = visit(ctx.getChild(2));
        if (left == null) {
            return right == null;
        }
        if (right == null) {
            return false;
        }
        // 尝试将二者转换为数字进行比较
        Number ln = context.covertToNumber(left);
        Number rn = context.covertToNumber(right);
        if (ln != null && rn != null) {
            return ln.doubleValue() == rn.doubleValue();
        }
        // 转换为字符串进行比较
        String ls = left.toString();
        String rs = right.toString();
        return ls.equals(rs);
    }

    @Override
    public Object visitNEQ(ObjectPathParser.NEQContext ctx) {
        Object left = visit(ctx.getChild(0));
        Object right = visit(ctx.getChild(2));
        if (left == null) {
            return right != null;
        }
        if (right == null) {
            return false;
        }
        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() != ((Number) right).doubleValue();
        }
        if (left instanceof String && right instanceof String) {
            return !left.equals(right);
        }
        if (left instanceof Boolean && right instanceof Boolean) {
            return !left.equals(right);
        }
        return !left.equals(right);
    }

    @Override
    public Object visitLT(ObjectPathParser.LTContext ctx) {
        return compareNumber(ctx, (l, r) -> l.doubleValue() < r.doubleValue());
    }

    @Override
    public Object visitLE(ObjectPathParser.LEContext ctx) {
        return compareNumber(ctx, (l, r) -> l.doubleValue() <= r.doubleValue());
    }

    @Override
    public Object visitGT(ObjectPathParser.GTContext ctx) {
        return compareNumber(ctx, (l, r) -> l.doubleValue() > r.doubleValue());
    }

    @Override
    public Object visitGE(ObjectPathParser.GEContext ctx) {
        return compareNumber(ctx, (l, r) -> l.doubleValue() >= r.doubleValue());
    }

    protected boolean compareNumber(ObjectPathParser.ComparExprContext ctx, NumberCompare compare) {
        Object left = visit(ctx.getChild(0));
        Object right = visit(ctx.getChild(2));
        Number l = context.covertToNumber(left);
        Number r = context.covertToNumber(right);
        if (l == null || r == null) {
            return false;
        }
        return compare.compare(l, r);
    }

    /**
     * 这是一个比较特殊的操作,该操作将会尝试降低数组的维度,将数组中的元素提取出来
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public Object visitUP(ObjectPathParser.UPContext ctx) {
        Integer number = Optional.ofNullable(ctx.NUMBER()).map(ParseTree::getText).map(Integer::parseInt).orElse(-1);
        // 获取当前对象,要求当前对象必须是一个集合
        // 然后开始降低数组维度
        List<Object> objects = up(context.covertToList(), number);
        this.context.updateParent();
        this.context.updateCurrent(objects);
        return objects;
    }

    protected List<Object> up(List<?> objects, int number) {
        List<Object> newObjects = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof List) {
                List<?> list = (List<?>) object;
                if (number == 0) {
                    newObjects.addAll(list);
                } else {
                    newObjects.addAll(up(list, number - 1));
                }
            } else {
                newObjects.add(object);
            }
        }
        return newObjects;
    }


    @Override
    public Object visitISNULL(ObjectPathParser.ISNULLContext ctx) {
        return visit(ctx.getChild(0)) == null;
    }

    @Override
    public Object visitLREGEX(ObjectPathParser.LREGEXContext ctx) {
        String regex = ctx.getChild(0).getText();
        Object str = visit(ctx.getChild(2));
        if (str == null || regex == null) {
            return false;
        }
        return useRegexTest(regex, str);
    }

    @Override
    public Object visitRREGEX(ObjectPathParser.RREGEXContext ctx) {
        Object str = visit(ctx.getChild(0));
        String regex = ctx.getChild(2).getText();
        if (str == null || regex == null) {
            return false;
        }
        return useRegexTest(regex, str);
    }

    /**
     * 使用正则表达式测试字符串
     * js中正则表达式格式为/正则表达式/修饰符,这里需要将正则表达式提取出来 /pattern/flags;
     * 移除开头的/,然后以最后一个/为分隔符,分割正则表达式和修饰符
     * 尝试获取修饰符,这里只支持i,g,m三个修饰符,因为这三个修饰符是java支持的,其他的,比如s,x等修饰符,java不支持
     * i: 忽略大小写 g:全局匹配 m:多行匹配 s:单行匹配 x:忽略空白字符
     * modifier有可能为空,因为正则表达式不一定有修饰符
     *
     * @param regex 正则表达式
     * @param str   字符串
     * @return 是否匹配
     */
    private static boolean useRegexTest(String regex, Object str) {
        regex = regex.substring(1);
        int index = regex.lastIndexOf('/');
        String finalRegex = regex.substring(0, index);

        String modifier = regex.substring(index + 1);

        int flag = 0;
        if (modifier.contains("i")) {
            flag = Pattern.CASE_INSENSITIVE;
        }
        if (modifier.contains("g")) {
            flag |= Pattern.MULTILINE;
        }
        if (modifier.contains("m")) {
            flag |= Pattern.DOTALL;
        }
        return Pattern.compile(finalRegex, flag).matcher(str.toString()).find();
    }

    @Override
    public Object visitBETWEEN(ObjectPathParser.BETWEENContext ctx) {
        ObjectPathParser.ExprContext expr = ctx.expr();
        Object target = visit(expr);

        List<TerminalNode> number = ctx.NUMBER();
        Object left = visit(number.get(0));
        Object right = visit(number.get(1));
        if (target == null || left == null || right == null) {
            return false;
        }
        Number l = context.covertToNumber(left);
        Number r = context.covertToNumber(right);
        Number t = context.covertToNumber(target);
        if (l == null || r == null || t == null) {
            return false;
        }
        return l.doubleValue() <= t.doubleValue() && t.doubleValue() <= r.doubleValue();
    }

    @Override
    public Object visitLIKE(ObjectPathParser.LIKEContext ctx) {
        ObjectPathParser.ExprContext expr = ctx.expr();
        Object target = visit(expr);
        String likeStr = unquote(ctx.ID().getText());
        if (likeStr.startsWith("%") && likeStr.endsWith("%")) {
            return target.toString().contains(likeStr.substring(1, likeStr.length() - 1));
        }
        if (likeStr.startsWith("%")) {
            return target.toString().endsWith(likeStr.substring(1));
        }
        if (likeStr.endsWith("%")) {
            return target.toString().startsWith(likeStr.substring(0, likeStr.length() - 1));
        }
        return false;
    }

    protected String unquote(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        if (str.startsWith("'") && str.endsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    @Override
    public Object visitIN(ObjectPathParser.INContext ctx) {
        ObjectPathParser.ArgsContext args = ctx.args();
        Object list = visit(args);
        Object target = visit(ctx.expr());
        if (list == null || target == null) {
            return false;
        }
        if (list instanceof List) {
            return ((List<?>) list).contains(target);
        }
        return false;
    }

    @Override
    public Object visitNOTIN(ObjectPathParser.NOTINContext ctx) {
        ObjectPathParser.ArgsContext args = ctx.args();
        Object list = visit(args);
        Object target = visit(ctx.expr());
        if (list == null || target == null) {
            return false;
        }
        if (list instanceof List) {
            return !((List<?>) list).contains(target);
        }
        return true;
    }

    @Override
    public Object visitCONTAINS(ObjectPathParser.CONTAINSContext ctx) {
        ObjectPathParser.ArgsContext args = ctx.args();
        Object list = visit(args);
        Object target = visit(ctx.expr());
        if (list == null || target == null) {
            return false;
        }
        if (list instanceof List) {
            return ((List<?>) list).contains(target);
        }
        return false;
    }

    @Override
    public Object visitSTARTSWITH(ObjectPathParser.STARTSWITHContext ctx) {
        Object target = visit(ctx.expr());
        Object prefix = visit(ctx.getChild(2));
        if (target == null || prefix == null) {
            return false;
        }
        return target.toString().startsWith(prefix.toString());
    }

    @Override
    public Object visitENDSWITH(ObjectPathParser.ENDSWITHContext ctx) {
        Object target = visit(ctx.expr());
        Object suffix = visit(ctx.getChild(2));
        if (target == null || suffix == null) {
            return false;
        }
        return target.toString().endsWith(suffix.toString());
    }

    @Override
    public Object visitMATCHES(ObjectPathParser.MATCHESContext ctx) {
        Object target = visit(ctx.expr());
        Object regex = visit(ctx.getChild(2));
        if (target == null || regex == null) {
            return false;
        }
        return target.toString().matches(regex.toString());
    }

    @Override
    public Object visitAND(ObjectPathParser.ANDContext ctx) {
        Object left = visit(ctx.getChild(0));
        Object right = visit(ctx.getChild(2));
        if (left == null || right == null) {
            return false;
        }
        if (left instanceof Boolean && right instanceof Boolean) {
            return (Boolean) left && (Boolean) right;
        }
        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() != 0 && ((Number) right).doubleValue() != 0;
        }
        if (left instanceof String && right instanceof String) {
            return "TRUE".equalsIgnoreCase((String) left) && "TRUE".equalsIgnoreCase((String) right);
        }
        return false;
    }

    @Override
    public Object visitOR(ObjectPathParser.ORContext ctx) {
        Object left = visit(ctx.getChild(0));
        Object right = visit(ctx.getChild(2));
        if (left == null || right == null) {
            return false;
        }
        if (left instanceof Boolean && right instanceof Boolean) {
            return (Boolean) left || (Boolean) right;
        }
        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() != 0 || ((Number) right).doubleValue() != 0;
        }
        if (left instanceof String && right instanceof String) {
            return "TRUE".equalsIgnoreCase((String) left) || "TRUE".equalsIgnoreCase((String) right);
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitMIN(ObjectPathParser.MINContext ctx) {
        // 获取集合中的最小值,其要求当前元素必须是一个集合
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            Optional<Double> dOpt = Optional.ofNullable(ctx.DOUBLE()).map(ParseTree::getText).map(Double::parseDouble);
            if (dOpt.isPresent()) {
                this.context.updateCurrent(dOpt.get());
                return dOpt.get();
            }
            Integer v = Optional.ofNullable(ctx.NUMBER()).map(ParseTree::getText).map(Integer::parseInt).orElse(null);
            this.context.updateCurrent(v);
            return v;
        }
        // 此时需要对集合中的元素进行比较,找到最小值
        // 此时会有两个分支,一个是提供了筛选表达式,一个是没有提供筛选表达式
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Optional.ofNullable(expr).isPresent()) {
            // 提供了筛选表达式,则需要先筛选元素,然后再比较元素的大小
            // 这里的表达式访问的是集合中的每一个元素
            Object res = objects.stream().map(o -> {
                // 创建子上下文
                ObjectPathParserContext child = this.context.createChild(o);
                // 创建子访问器
                ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
                return childVisitor.visit(expr);
            }).min((o1, o2) -> {
                if (o1 instanceof Comparable && o2 instanceof Comparable) {
                    return ((Comparable<Object>) o1).compareTo(o2);
                }
                return 0;
            }).orElse(null);
            this.context.updateCurrent(res);
            return res;
        }
        // 没有提供筛选表达式,则直接比较元素的大小,找到最小值,这要求元素必须是可比较的,即实现了Comparable接口
        Object res = objects.stream().min((o1, o2) -> {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
            return 0;
        }).orElse(null);
        this.context.updateCurrent(res);
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitMAX(ObjectPathParser.MAXContext ctx) {
        // 获取集合中的最大值,其要求当前元素必须是一个集合
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            Optional<Double> dOpt = Optional.ofNullable(ctx.DOUBLE()).map(ParseTree::getText).map(Double::parseDouble);
            if (dOpt.isPresent()) {
                this.context.updateCurrent(dOpt.get());
                return dOpt.get();
            }
            Integer v = Optional.ofNullable(ctx.NUMBER()).map(ParseTree::getText).map(Integer::parseInt).orElse(null);
            this.context.updateCurrent(v);
            return v;
        }
        // 此时需要对集合中的元素进行比较,找到最大值
        // 此时会有两个分支,一个是提供了筛选表达式,一个是没有提供筛选表达式
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Optional.ofNullable(expr).isPresent()) {
            // 提供了筛选表达式,则需要先筛选元素,然后再比较元素的大小
            // 这里的表达式访问的是集合中的每一个元素
            Object res = objects.stream().map(o -> {
                // 创建子上下文
                ObjectPathParserContext child = this.context.createChild(o);
                // 创建子访问器
                ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
                return childVisitor.visit(expr);
            }).max((o1, o2) -> {
                if (o1 instanceof Comparable && o2 instanceof Comparable) {
                    return ((Comparable<Object>) o1).compareTo(o2);
                }
                return 0;
            }).orElse(null);
            this.context.updateCurrent(res);
            return res;
        }
        // 没有提供筛选表达式,则直接比较元素的大小,找到最大值,这要求元素必须是可比较的,即实现了Comparable接口
        Object res = objects.stream().max((o1, o2) -> {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
            return 0;
        }).orElse(null);
        this.context.updateCurrent(res);
        return res;
    }

    @Override
    public Object visitAVG(ObjectPathParser.AVGContext ctx) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            Optional<Double> dOpt = Optional.ofNullable(ctx.DOUBLE()).map(ParseTree::getText).map(Double::parseDouble);
            if (dOpt.isPresent()) {
                this.context.updateCurrent(dOpt.get());
                return dOpt.get();
            }
            Integer v = Optional.ofNullable(ctx.NUMBER()).map(ParseTree::getText).map(Integer::parseInt).orElse(null);
            this.context.updateCurrent(v);
            return v;
        }
        // 计算平均值,要求元素必须是数字
        double avg = objects.stream().map(o -> Optional.ofNullable(ctx.expr()).map(e -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            return childVisitor.visit(e);
        }).orElse(o)).mapToDouble(o -> this.context.covertToNumber(o).doubleValue()).average().orElse(0);
        this.context.updateCurrent(avg);
        return avg;
    }

    @Override
    public Object visitSUM(ObjectPathParser.SUMContext ctx) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            Optional<Double> dOpt = Optional.ofNullable(ctx.DOUBLE()).map(ParseTree::getText).map(Double::parseDouble);
            if (dOpt.isPresent()) {
                this.context.updateCurrent(dOpt.get());
                return dOpt.get();
            }
            Integer v = Optional.ofNullable(ctx.NUMBER()).map(ParseTree::getText).map(Integer::parseInt).orElse(null);
            this.context.updateCurrent(v);
            return v;
        }
        // 计算和,要求元素必须是数字
        double sum = objects.stream().map(o -> Optional.ofNullable(ctx.expr()).map(e -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            return childVisitor.visit(e);
        }).orElse(o)).mapToDouble(o -> this.context.covertToNumber(o).doubleValue()).sum();
        this.context.updateCurrent(sum);
        return sum;
    }

    @Override
    public Object visitSIZE(ObjectPathParser.SIZEContext ctx) {
        List<?> objects = this.context.covertToList();
        int size = objects.size();
        this.context.updateCurrent(size);
        return size;
    }

    @Override
    public Object visitISEMPTY(ObjectPathParser.ISEMPTYContext ctx) {
        List<?> objects = this.context.covertToList();
        boolean empty = objects.isEmpty();
        this.context.updateCurrent(empty);
        return empty;
    }

    @Override
    public Object visitLENGTH(ObjectPathParser.LENGTHContext ctx) {
        // 字符串,数组,集合,Map的长度
        Object obj = this.context.currentValue();
        if (obj instanceof String) {
            int length = ((String) obj).length();
            this.context.updateCurrent(length);
            return length;
        }
        if (obj instanceof Map) {
            int length = ((Map<?, ?>) obj).size();
            this.context.updateCurrent(length);
            return length;
        }
        List<Object> objects = this.context.covertToList();
        int length = objects.size();
        this.context.updateCurrent(length);
        return length;
    }

    /**
     * 获取map的keys集合或者对象的字段集合
     *
     * @param ctx the parse tree
     * @return 集合
     */
    @Override
    public Object visitKEYS(ObjectPathParser.KEYSContext ctx) {
        List<Object> keys = new ArrayList<>();
        this.context.eachKey(keys::add);
        this.context.updateCurrent(keys);
        return keys;
    }

    @Override
    public Object visitVALUES(ObjectPathParser.VALUESContext ctx) {
        List<Object> values = new ArrayList<>();
        this.context.eachValue(values::add);
        this.context.updateCurrent(values);
        return values;
    }

    @Override
    public Object visitREVERSE(ObjectPathParser.REVERSEContext ctx) {
        List<Object> objects = this.context.covertToList();
        Collections.reverse(objects);
        this.context.updateCurrent(objects);
        return objects;
    }

    @Override
    public Object visitSORT(ObjectPathParser.SORTContext ctx) {
        List<ObjectPathParser.ExprContext> exprs = ctx.expr();
        List<?> sorted = sortByExprs(exprs);
        this.context.updateCurrent(sorted);

        return sorted;
    }

    @SuppressWarnings("all")
    public List<?> sortByExprs(List<ObjectPathParser.ExprContext> exprs) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return objects;
        }
        ObjectPathParser.ExprContext expr = exprs.get(0);
        return objects.stream().sorted((o1, o2) -> {
            if (Optional.ofNullable(expr).isPresent()) {
                // 创建子访问器
                ObjectPathVisitor o1v = new ObjectPathVisitor(this.context.createChild(o1));
                ObjectPathVisitor o2v = new ObjectPathVisitor(this.context.createChild(o2));
                int i = ((Comparable<Object>) o1v.visit(expr)).compareTo(o2v.visit(expr));
                if (i == 0) {
                    // 如果两个元素的排序值相同,则继续使用后续的排序表达式进行计算
                    for (int j = 1; j < exprs.size(); j++) {
                        ObjectPathParser.ExprContext e = exprs.get(j);
                        i = ((Comparable<Object>) o1v.visit(e)).compareTo(o2v.visit(e));
                        if (i != 0) {
                            return i;
                        }
                    }
                }
                return i;
            } else if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return ((Comparable<Object>) o1).compareTo(o2) * -1;
            }
            return 0;
        }).collect(Collectors.toList());
    }


    @Override
    public Object visitRSORT(ObjectPathParser.RSORTContext ctx) {
        List<ObjectPathParser.ExprContext> exprs = ctx.expr();
        List<?> sorted = sortByExprs(exprs);
        Collections.reverse(sorted);
        this.context.updateCurrent(sorted);
        return sorted;
    }

    /**
     * 对集合进行分组,分组操作将会将集合中的元素按照指定的表达式进行分组
     */
    @Override
    public Object visitGROUPBY(ObjectPathParser.GROUPBYContext ctx) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return objects;
        }
        ObjectPathParser.ExprContext expr = ctx.expr();
        Map<Object, List<Object>> groups = objects.stream().collect(Collectors.groupingBy(o -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            return childVisitor.visit(expr);
        }));
        this.context.updateCurrent(groups);
        return groups;
    }

    @Override
    public Object visitDISTINCT(ObjectPathParser.DISTINCTContext ctx) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return objects;
        }
        if (!Optional.ofNullable(ctx.expr()).isPresent()) {
            List<Object> distinct = objects.stream().distinct().collect(Collectors.toList());
            this.context.updateCurrent(distinct);
            return distinct;
        }
        // 如果提供了表达式,则需要根据表达式进行去重
        ObjectPathParser.ExprContext expr = ctx.expr();
        List<Object> distinctCache = new ArrayList<>();
        List<Object> distinct = objects.stream().filter(o -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            Object res = childVisitor.visit(expr);
            if (distinctCache.contains(res)) {
                return false;
            }
            distinctCache.add(res);
            return true;
        }).distinct().collect(Collectors.toList());
        this.context.updateCurrent(distinct);
        return distinct;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object visitJOIN(ObjectPathParser.JOINContext ctx) {
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            objects = new ArrayList<>();
        }
        ObjectPathParser.ArgsContext args = ctx.args();
        for (ObjectPathParser.ArgContext arg : args.arg()) {
            Object obj = visit(arg);
            if (obj instanceof List) {
                objects.addAll((List<Object>) obj);
            } else {
                objects.add(obj);
            }
        }
        this.context.updateCurrent(objects);
        return objects;
    }

    @Override
    public Object visitFIRST(ObjectPathParser.FIRSTContext ctx) {
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return null;
        }
        Object first = objects.get(0);
        this.context.updateCurrent(first);
        return first;
    }

    @Override
    public Object visitLAST(ObjectPathParser.LASTContext ctx) {
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return null;
        }
        Object last = objects.get(objects.size() - 1);
        this.context.updateCurrent(last);
        return last;
    }

    @Override
    public Object visitINDEXOF(ObjectPathParser.INDEXOFContext ctx) {
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return null;
        }
        Number index = this.context.covertToNumber(ctx.NUMBER().getText());
        if (index == null) {
            return null;
        }
        int i = index.intValue();
        // 如果索引小于0,则从后往前数
        if (i < 0) {
            i = objects.size() + i;
        }
        if (i < 0 || i >= objects.size()) {
            return null;
        }
        Object obj = objects.get(i);
        this.context.updateCurrent(obj);
        return obj;
    }

    /**
     * 对字符串进行分割操作
     */
    @Override
    public Object visitSPLIT(ObjectPathParser.SPLITContext ctx) {
        String s = this.context.covertToString();
        String split = unquote(ctx.ID().getText());
        // 移除开头和结尾的引号
        String[] splitStr = s.split(split);
        this.context.updateCurrent(Arrays.asList(splitStr));
        return Arrays.asList(splitStr);
    }

    @Override
    public Object visitREPLACE(ObjectPathParser.REPLACEContext ctx) {
        String old = unquote(ctx.ID(0).getText());
        String want = unquote(ctx.ID(1).getText());
        String s = this.context.covertToString();
        String replace = s.replace(old, want);
        this.context.updateCurrent(replace);
        return replace;
    }

    @Override
    public Object visitTOUPPER(ObjectPathParser.TOUPPERContext ctx) {
        String upperCase = this.context.covertToString().toUpperCase();
        this.context.updateCurrent(upperCase);
        return upperCase;
    }

    @Override
    public Object visitTOLOWER(ObjectPathParser.TOLOWERContext ctx) {
        String lowerCase = this.context.covertToString().toLowerCase();
        this.context.updateCurrent(lowerCase);
        return lowerCase;
    }

    @Override
    public Object visitSUBSTRING(ObjectPathParser.SUBSTRINGContext ctx) {
        String s = this.context.covertToString();
        int start = Integer.parseInt(Optional.ofNullable(ctx.NUMBER(0)).map(ParseTree::getText).orElse("0"));
        int end = Integer.parseInt(Optional.ofNullable(ctx.NUMBER(1)).map(ParseTree::getText).orElse(String.valueOf(s.length())));
        String substring = s.substring(start, end);
        this.context.updateCurrent(substring);
        return substring;
    }

    @Override
    public Object visitLTRIM(ObjectPathParser.LTRIMContext ctx) {
        String s = this.context.covertToString();
        String trim = s.replaceAll("^\\s+", "");
        this.context.updateCurrent(trim);
        return trim;
    }

    @Override
    public Object visitRTRIM(ObjectPathParser.RTRIMContext ctx) {
        String s = this.context.covertToString();
        String trim = s.replaceAll("\\s+$", "");
        this.context.updateCurrent(trim);
        return trim;
    }

    @Override
    public Object visitTRIM(ObjectPathParser.TRIMContext ctx) {
        String s = this.context.covertToString();
        String trim = s.trim();
        this.context.updateCurrent(trim);
        return trim;
    }

    @Override
    public Object visitCONCAT(ObjectPathParser.CONCATContext ctx) {
        String s = this.context.covertToString();
        String cs = unquote(ctx.ID().getText());
        String concat = s.concat(cs);
        this.context.updateCurrent(concat);
        return concat;
    }

    @Override
    public Object visitScripts(ObjectPathParser.ScriptsContext ctx) {
        ObjectPathParser.Scripts_startContext ssc = ctx.scripts_start();
        String scriptName = ssc.ID().getText();
        ObjectPathParser.NamedArgsContext namedArgsContext = ssc.namedArgs();
        Object nas = visit(namedArgsContext);
        String text = ctx.SCRIPT_CONTENT().getText();
        // 移除脚本中前置的>以及后置</
        if (text.startsWith(">")) {
            text = text.substring(1);
        }
        if (text.endsWith("</")) {
            text = text.substring(0, text.length() - 2);
        }
        // 此处时就获取了完整的脚本数据,脚本类型,脚本参数,脚本内容
        Object res = this.context.executeScript(scriptName, nas, text);
        this.context.updateCurrent(res);
        return res;
    }

    @Override
    public Object visitNamedArgs(ObjectPathParser.NamedArgsContext ctx) {
        Map<Object, Object> namedArgs = new HashMap<>();
        List<ObjectPathParser.NamedArgContext> namedArg = ctx.namedArg();
        for (ObjectPathParser.NamedArgContext nac : namedArg) {
            String name = nac.ID().getText();
            Object obj = visit(nac.arg());
            namedArgs.put(name, obj);
        }
        return namedArgs;
    }

    @Override
    public Object visitReverseComparExpr(ObjectPathParser.ReverseComparExprContext ctx) {
        ObjectPathParser.ComparExprContext comparExprContext = ctx.comparExpr();
        Object result = visit(comparExprContext);
        return !(Boolean) result;
    }

    @Override
    public Object visitArgs(ObjectPathParser.ArgsContext ctx) {
        List<Object> args = new ArrayList<>();
        List<ObjectPathParser.ArgContext> arg = ctx.arg();
        for (ObjectPathParser.ArgContext a : arg) {
            args.add(visit(a));
        }
        return args;
    }

    @Override
    public Object visitArg(ObjectPathParser.ArgContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Object visitComplexValue(ObjectPathParser.ComplexValueContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Object visitJson(ObjectPathParser.JsonContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public Object visitObject(ObjectPathParser.ObjectContext ctx) {
        return this.context.parseJson(ctx.getText());
    }

    @Override
    public Object visitArray(ObjectPathParser.ArrayContext ctx) {
        return this.context.parseJson(ctx.getText());
    }

    @Override
    public Object visitValue(ObjectPathParser.ValueContext ctx) {
        if (Optional.ofNullable(ctx.ID()).isPresent()) {
            String id = ctx.ID().getText();
            id = unquote(id);
            this.context = this.context.createChild(id);
            return id;
        }
        if (Optional.ofNullable(ctx.NUMBER()).isPresent()) {
            String number = ctx.NUMBER().getText();
            if (number.endsWith("l") || number.endsWith("L")) {
                number = number.substring(0, number.length() - 1);
                long l = Long.parseLong(number);
                this.context = this.context.createChild(l);
                return l;
            }
            if (number.contains(".")) {
                double d = Double.parseDouble(number);
                this.context = this.context.createChild(d);
                return d;
            }
            if (number.endsWith("d") || number.endsWith("D")) {
                number = number.substring(0, number.length() - 1);
                double d = Double.parseDouble(number);
                this.context = this.context.createChild(d);
                return d;
            }
            Integer i = Integer.parseInt(number);
            this.context = this.context.createChild(i);
            return i;
        }
        if (Optional.ofNullable(ctx.STRING()).isPresent()) {
            String string = ctx.STRING().getText();
            this.context = this.context.createChild(this.context.eval(string));
            return string;
        }
        if (Optional.ofNullable(ctx.DOUBLE()).isPresent()) {
            String doubleStr = ctx.DOUBLE().getText();
            double d = Double.parseDouble(doubleStr);
            this.context = this.context.createChild(d);
            return d;
        }
        if (Optional.ofNullable(ctx.BOOL()).isPresent()) {
            String boolStr = ctx.BOOL().getText();
            boolean b = Boolean.parseBoolean(boolStr);
            this.context = this.context.createChild(b);
            return b;
        }
        return null;
    }
}
