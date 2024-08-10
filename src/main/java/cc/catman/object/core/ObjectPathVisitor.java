package cc.catman.object.core;


import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.convert.IConvert;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.ReflectionHelper;
import cc.catman.object.core.util.StringHelper;
import cc.catman.object.path.standard.ObjectPathBaseVisitor;
import cc.catman.object.path.standard.ObjectPathParser;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 对象路径访问器,用于访问对象路径表达式
 *
 * @author jpanda
 * @since 0.0.1
 */
public class ObjectPathVisitor extends ObjectPathBaseVisitor<PropertyWrapper> {
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
    public PropertyWrapper visitObjectPath(ObjectPathParser.ObjectPathContext ctx) {
        // 需要为每一个通道创建一个新的上下文
        //  获取表达式结果
        PropertyWrapper res = visit(ctx.expr());
        this.context.updateParent();
        this.context.updateCurrent(res);
        // 开始遍历通道
        List<ObjectPathParser.PipeContext> pipes = ctx.pipe();
        for (ObjectPathParser.PipeContext pipe : pipes) {
            res = this.visit(pipe);
        }
        ObjectPathParser.TypeCastContext cast = ctx.typeCast();
        if (Objects.nonNull(cast)){
            Class<?> type = this.context.analyzerType(cast.getText());
            if (!res.isInstanceOf(type)){
                return wrapper(res.read(type));
            }
        }
        return res;
    }

    @Override
    public PropertyWrapper visitPipe(ObjectPathParser.PipeContext ctx) {
        // 创建新的上下文,需要注意的是,在通道中,上下文是独立的,不会相互影响
        PropertyWrapper cv = this.context.currentValue();
        this.context = this.context.createContext(cv, null, cv);
        // 创建新的访问器
        // 访问通道,获取值
        PropertyWrapper pipeValue = this.visit(ctx.expr());
        this.context.updateCurrent(pipeValue);
        return pipeValue;
    }

    @Override
    public PropertyWrapper visitTYPE_CAST_EXPR(ObjectPathParser.TYPE_CAST_EXPRContext ctx) {
        PropertyWrapper res = visit(ctx.expr());
        ObjectPathParser.TypeCastContext cast = ctx.typeCast();
        if (Objects.nonNull(cast)){
            Class<?> type = this.context.analyzerType(cast.getText());
            return wrapper(res.read(type));
        }
        return res;
    }

    /**
     * 访问表达式,每一个表达式都会有一个独立的上下文,用于存放当前解析的对象
     * 当表达式解析完毕后,会退出该表达式,并将上下文重置为之前的上下文对象
     *
     * @param ctx the parse tree
     * @return 表达式的执行结果
     */
    @Override
    public PropertyWrapper visitPATH_EXPR(ObjectPathParser.PATH_EXPRContext ctx) {
        // 切换上下文
        ObjectPathParserContext nc = switchContextWithExpr(ctx);
        // 持有新上下文的访问器
        ObjectPathVisitor childVisitor = new ObjectPathVisitor(nc);
        // 上下文切换完成后,访问选择器,选择器将会切换当前上下文对象
        List<ObjectPathParser.SelectorContext> selector = ctx.selector();
        // 遍历选择器,每次遍历都会切换上下文
        PropertyWrapper result = nc.currentValue();

        for (ObjectPathParser.SelectorContext s : selector) {
            // 遇到null值,是否停止解析
            if (config.isStopParseWhenNull() && (result == null)) {
                return wrapper(null);
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

    @SuppressWarnings("java:S3776")
    @Override
    public PropertyWrapper visitCALCULATE_EXPR(ObjectPathParser.CALCULATE_EXPRContext ctx) {
        this.context.disableWrite();
        String text = ctx.getChild(1).getText();
        // 计算时,需要未左右计算添加独立上下文
        ObjectPathParserContext lc = this.context.createChild(this.context.currentValue());
        ObjectPathVisitor lv = new ObjectPathVisitor(lc);
        ObjectPathParserContext rc = this.context.createChild(this.context.currentValue());
        ObjectPathVisitor rv = new ObjectPathVisitor(rc);

        PropertyWrapper left = lv.visit(ctx.expr(0));
        PropertyWrapper right = rv.visit(ctx.expr(1));

        boolean isNumber = (left.isInstanceOf(Number.class) && right.isInstanceOf(Number.class));
        if (isNumber) {
            Number l = Optional.ofNullable(left.read(Number.class)).orElse(0);
            Number r = Optional.ofNullable(right.read(Number.class)).orElse(0);
            if ("+".equals(text)) {
                return wrapper(add(l,r));
            }
            if ("-".equals(text)) {
                return wrapper(sub(l,r));
            }
            if ("*".equals(text)) {
                return wrapper(mul(l,r));
            }
            if ("/".equals(text)) {
                return wrapper(div(l,r));
            }
            if ("%".equals(text)) {
                return wrapper(mod(l,r));
            }
        }

        if (this.config.isEnableFeature(Features.CONNECT_STRING_USE_PLUS_SIGN)
            && ("+".equals(text))) {
            String leftText = left.read(String.class);
            String rightText = right.read(String.class);
            if (Objects.nonNull(leftText) && (leftText.startsWith("\"") || leftText.startsWith("'"))) {
                left = wrapper(unquote(leftText));
            }

            if (Objects.nonNull(rightText) && (rightText.startsWith("\"") || rightText.startsWith("'"))) {
                right = wrapper(unquote(rightText));
            }


            if (config.isUseZeroForNull()) {
                return wrapper(Optional.ofNullable(left.read()).map(Object::toString).orElse("")
                               + Optional.ofNullable(right.read()).map(Object::toString).orElse(""));
            }
            return wrapper(Optional.ofNullable(left.read()).map(Object::toString).orElse("null")
                           + Optional.ofNullable(right.read()).map(Object::toString).orElse("null"));

        }
        return wrapper(null);
    }

    protected Number add(Number a,Number b){
        if(ReflectionHelper.isAssignableFrom(Double.class,a.getClass())
        || ReflectionHelper.isAssignableFrom(Double.class,b.getClass())
        ){
            return a.doubleValue()+b.doubleValue();
        }
        if(ReflectionHelper.isAssignableFrom(Float.class,a.getClass())
            || ReflectionHelper.isAssignableFrom(Float.class,b.getClass())){
            return a.floatValue()+b.floatValue();
        }

        if(ReflectionHelper.isAssignableFrom(Long.class,a.getClass())
            || ReflectionHelper.isAssignableFrom(Long.class,b.getClass())){
            return a.longValue()+b.longValue();
        }
        if(ReflectionHelper.isAssignableFrom(Integer.class,a.getClass())
            || ReflectionHelper.isAssignableFrom(Integer.class,b.getClass())){
            return a.intValue()+b.intValue();
        }
        if (ReflectionHelper.isAssignableFrom(Short.class,a.getClass())
        ||ReflectionHelper.isAssignableFrom(Short.class,b.getClass())){
            return a.shortValue()+b.shortValue();
        }
        return a.doubleValue()+b.doubleValue();
    }

    protected Number sub(Number a,Number b){
        if(ReflectionHelper.isAssignableFrom(Double.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Double.class,b.getClass())
        ){
            return a.doubleValue()-b.doubleValue();
        }
        if(ReflectionHelper.isAssignableFrom(Float.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Float.class,b.getClass())){
            return a.floatValue()-b.floatValue();
        }

        if(ReflectionHelper.isAssignableFrom(Long.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Long.class,b.getClass())){
            return a.longValue()-b.longValue();
        }
        if(ReflectionHelper.isAssignableFrom(Integer.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Integer.class,b.getClass())){
            return a.intValue()-b.intValue();
        }
        if (ReflectionHelper.isAssignableFrom(Short.class,a.getClass())
            ||ReflectionHelper.isAssignableFrom(Short.class,b.getClass())){
            return a.shortValue()-b.shortValue();
        }
        return a.doubleValue()-b.doubleValue();
    }
    protected Number mul(Number a,Number b){
        if(ReflectionHelper.isAssignableFrom(Double.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Double.class,b.getClass())
        ){
            return a.doubleValue()*b.doubleValue();
        }
        if(ReflectionHelper.isAssignableFrom(Float.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Float.class,b.getClass())){
            return a.floatValue()*b.floatValue();
        }

        if(ReflectionHelper.isAssignableFrom(Long.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Long.class,b.getClass())){
            return a.longValue()*b.longValue();
        }
        if(ReflectionHelper.isAssignableFrom(Integer.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Integer.class,b.getClass())){
            return a.intValue()*b.intValue();
        }
        if (ReflectionHelper.isAssignableFrom(Short.class,a.getClass())
            ||ReflectionHelper.isAssignableFrom(Short.class,b.getClass())){
            return a.shortValue()*b.shortValue();
        }
        return a.doubleValue()*b.doubleValue();
    }

    protected Number div(Number a,Number b){
        if(ReflectionHelper.isAssignableFrom(Double.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Double.class,b.getClass())
        ){
            return a.doubleValue()/b.doubleValue();
        }
        if(ReflectionHelper.isAssignableFrom(Float.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Float.class,b.getClass())){
            return a.floatValue()/b.floatValue();
        }

        if(ReflectionHelper.isAssignableFrom(Long.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Long.class,b.getClass())){
            return a.longValue()/b.longValue();
        }
        if(ReflectionHelper.isAssignableFrom(Integer.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Integer.class,b.getClass())){
            return a.intValue()/b.intValue();
        }
        if (ReflectionHelper.isAssignableFrom(Short.class,a.getClass())
            ||ReflectionHelper.isAssignableFrom(Short.class,b.getClass())){
            return a.shortValue()/b.shortValue();
        }
        return a.doubleValue()/b.doubleValue();
    }

    protected Number mod(Number a,Number b){
        if(ReflectionHelper.isAssignableFrom(Double.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Double.class,b.getClass())
        ){
            return a.doubleValue()%b.doubleValue();
        }
        if(ReflectionHelper.isAssignableFrom(Float.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Float.class,b.getClass())){
            return a.floatValue()%b.floatValue();
        }

        if(ReflectionHelper.isAssignableFrom(Long.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Long.class,b.getClass())){
            return a.longValue()%b.longValue();
        }
        if(ReflectionHelper.isAssignableFrom(Integer.class,a.getClass())
           || ReflectionHelper.isAssignableFrom(Integer.class,b.getClass())){
            return a.intValue()%b.intValue();
        }
        if (ReflectionHelper.isAssignableFrom(Short.class,a.getClass())
            ||ReflectionHelper.isAssignableFrom(Short.class,b.getClass())){
            return a.shortValue()%b.shortValue();
        }
        return a.doubleValue()%b.doubleValue();
    }
    /**
     * 访问分组
     *
     * @param ctx the parse tree
     */
    @Override
    public PropertyWrapper visitGROUP_EXPR(ObjectPathParser.GROUP_EXPRContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public PropertyWrapper visitREG_EXPR(ObjectPathParser.REG_EXPRContext ctx) {
        return wrapper(ctx.REG().getText());
    }

    /**
     * 一个允许拥有默认值的表达式
     *
     * @param ctx the parse tree
     */
    @Override
    public PropertyWrapper visitDEFAULT_EXPR(ObjectPathParser.DEFAULT_EXPRContext ctx) {
        boolean hasConstrained = ctx.expr().size() == 3;

        ObjectPathParser.ExprContext value = ctx.expr(0);
        ObjectPathParser.ExprContext defaultValue = hasConstrained
                ? ctx.expr(2)
                : ctx.expr(1);
        PropertyWrapper v = visit(value);

        if (!hasConstrained){
            // 直接进行null判断
            boolean useDefaultValue= (v == null)
                                     || (
                                             v.isInstanceOf(Boolean.class)
                                             && v.read(Boolean.class)
                                     );
            return useDefaultValue ? visit(defaultValue) : v;
        }

        ParseTree filter = ctx.getChild(1);
        ObjectPathParserContext nc = this.context.createChild(v);
        // 持有新上下文的访问器
        ObjectPathVisitor childVisitor = new ObjectPathVisitor(nc);
        PropertyWrapper filterResult = childVisitor.visit(filter);
        return Boolean.TRUE.equals(filterResult.read(boolean.class))?
                v :
                visit(defaultValue);
    }

    @Override
    public PropertyWrapper visitEXPR_FUNC_EXPR(ObjectPathParser.EXPR_FUNC_EXPRContext ctx) {
        ObjectPathParser.ExprContext expr = ctx.expr();
        this.context.updateCurrent(visit(expr));
        ObjectPathParser.FuncContext func = ctx.func();
        return this.context.updateCurrent(visit(func));
    }

    /**
     * 切换上下文,切换上下文有两种方式
     * 一种是指定表达式的位置,切换上下文,即location模式
     * 另一种是直接访问当前上下文的子对象,即ID模式
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    public ObjectPathParserContext switchContextWithExpr(ObjectPathParser.PATH_EXPRContext ctx) {
        if (Optional.ofNullable(ctx.location()).isPresent()) {
            ELocation l = ELocation.fromLocation(ctx.location().getText());
            return this.context.createChildContext(l);
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
    public PropertyWrapper visitINDEX(ObjectPathParser.INDEXContext ctx) {
        // 获取待访问的索引值
        String index = this.createChildVisitor().visit(ctx.expr()).read(String.class);
        // 访问索引,获取索引对应的对象
        this.context.updateCurrent(this.context.currentValue());
        return this.context.eval(index);
    }


    /**
     * 访问子对象,此时目标对象可以是数组,集合,Map,对象,只要不是基本类型即可
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public PropertyWrapper visitINDEX_OR_NAME(ObjectPathParser.INDEX_OR_NAMEContext ctx) {
        // 获取待访问的索引值
        String id = Optional.ofNullable(ctx.ID()).orElseGet(ctx::STRING)
                .getText();
        // 访问索引,获取索引对应的对象
        PropertyWrapper obj = this.context.eval(id);
        // 切换上下文
        this.context = this.context.createChild(obj);
        return obj;
    }


    /**
     * 访问子对象,此时目标对象可以是数组,集合,Map,对象,只要不是基本类型即可
     * 此处会根据表达式中的name,提取出对应的对象,然后返回一个map
     *
     * @param ctx the parse tree
     * @return 属性包装器
     */
    @Override
    public PropertyWrapper visitINDEX_OR_NAME_LIST(ObjectPathParser.INDEX_OR_NAME_LISTContext ctx) {
        List<String> childs = new ArrayList<>();
        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            childs.add(StringHelper.unquote(ctx.getChild(i).getText()));
        }
        Map<Object, Object> map = new HashMap<>(childs.size());
        for (String id : childs) {
            PropertyWrapper obj = this.context.eval(id);
            map.put(id, obj.read());
        }
        this.context = this.context.createChild(map);
        return wrapper(map);
    }

    @Override
    public PropertyWrapper visitCHILD(ObjectPathParser.CHILDContext ctx) {

        String id = Optional.ofNullable(ctx.ID()).orElseGet(ctx::STRING)
                .getText();
        PropertyWrapper obj = this.context.eval(id);
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
    public PropertyWrapper visitWILDCARD_ALL(ObjectPathParser.WILDCARD_ALLContext ctx) {
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
        Object res = this.context.map(obj -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createContext(this.context.rootValue(), this.context.currentValue(), obj);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            // 然后使用子访问器访问后续操作
            // 提取后续操作
            PropertyWrapper result = childVisitor.context.currentValue();

            for (int i = index + 1; i < cc; i++) {
                ParseTree parseTree = children.get(i);
                result = childVisitor.visit(parseTree);
                if (i < cc - 1) {
                    // 继续切换,上一次的结果作为当前对象,上次被访问的对象作为父对象
                    childVisitor = new ObjectPathVisitor(childVisitor.context.createContext(childVisitor.context.rootValue(), childVisitor.context.currentValue(), result));
                }
            }
            return this.context.getMod().isReadOnly() ? result.read() : result;
        });
        // 聚合型方法需要特殊处理
        return wrapper(res);
    }

    /**
     * <pre>
     * 递归下降,获取所有匹配的子元素,并将子元素的筛选结果作为一个新的集合返回
     *
     * </pre>
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public PropertyWrapper visitRECURSIVE_CHILD(ObjectPathParser.RECURSIVE_CHILDContext ctx) {
        // 递归下降的操作本质上是通过子表达式筛选出符合条件的元素,然后将这些元素作为一个新的集合返回.
        // 他的实现和通配符访问类似,都需要中断后续子表达式的自动执行,并将其作为筛选条件来处理
        // 比如,$..name,本质上是递归获取所有元素的name属性,然后将这些name属性作为一个新的集合返回
        // 再比如$..book[?(@.price<10)],本质上是递归获取所有价格小于10的书籍,然后将这些书籍作为一个新的集合返回
        // 其有一个隐藏的filter操作,即对父元素筛选持有某一个属性的元素
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
        return wrapper(objects);
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
    public PropertyWrapper visitALL(ObjectPathParser.ALLContext ctx) {
        List<Object> list = new ArrayList<>();
        this.context.eachValue(list::add);
        this.context.updateCurrent(list);
        return wrapper(list);
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
    public PropertyWrapper visitMETHOD_CALL(ObjectPathParser.METHOD_CALLContext ctx) {
        this.context.disableWrite();
        String functionName = unquote(ctx.ID().getText());
        List<Object> params = new ArrayList<>();
        Optional.ofNullable(ctx.args())
                .ifPresent(args -> {
                    PropertyWrapper res = this.visit(args);
                    if (res.isInstanceOf(List.class)) {
                        params.addAll(res.read(List.class));
                    } else {
                        params.add(res.read());
                    }
                });
        return this.context.invokeMethod(this.context,functionName, params);
    }

    /**
     * 获取当前元素的索引或者名称
     */
    @Override
    public PropertyWrapper visitMETHOD_INDEX(ObjectPathParser.METHOD_INDEXContext ctx) {
        this.context.disableWrite();
        PropertyWrapper cv = this.context.currentValue();
        return this.context.updateCurrent(cv.getIndexOrKey());
    }

    /**
     * 访问切片,切片操作将会返回一个新的集合,该集合包含了切片范围内的所有元素
     * 切片操作只能在集合上执行
     *
     * @param ctx the parse tree
     * @return the result of the visit
     */
    @Override
    public PropertyWrapper visitSLICE(ObjectPathParser.SLICEContext ctx) {
        // 获取切片的起始位置和结束位置
        List<ObjectPathParser.ExprContext> numbers = ctx.expr();
        int start =  this.createChildVisitor().visit(numbers.get(0)).read(Number.class).intValue();
        int end = this.createChildVisitor().visit(numbers.get(1)).read(Number.class).intValue();
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
        return wrapper(old.subList(start, end));
    }

    /**
     * 这是一种比较特殊的切片操作,通过传入多个使用逗号分隔的数值,来读取集合中的元素
     */
    @Override
    public PropertyWrapper visitSLICE_PICK(ObjectPathParser.SLICE_PICKContext ctx) {
        // 获取当前对象,此时需要判断当前对象是否为集合,如果不是集合,则抛出异常
        List<Object> old = Optional.ofNullable(this.context.covertToList())
                .orElseGet(() -> this.config.isAutoCreateCollectionWhenInvokeMethod()
                        ? new ArrayList<>()
                        : null);

        if (old == null) {
            throw new IllegalArgumentException("The current object is not a collection or the collection is empty.");
        }

        List<ObjectPathParser.ExprContext> index = ctx.expr();
        List<Object> newList = new ArrayList<>(index.size());
        for (ObjectPathParser.ExprContext i : index) {
            int indexValue = this.createChildVisitor().visit(i).read(Number.class).intValue();
            if (indexValue < 0) {
                // 当索引为负数时,表示倒数第几个元素,需要将索引转换为正数
                indexValue = old.size() + indexValue;
            }
            if (indexValue < 0 || indexValue >= old.size()
                                  && (this.config.isSkipOutOfRangeIndexForList())
            ) {
                continue;

            }
            newList.add(old.get(indexValue));
        }
        return wrapper(newList);
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
    public PropertyWrapper visitFILTER(ObjectPathParser.FILTERContext ctx) {
        // 获取过滤条件
        ObjectPathParser.FilterExprContext f = ctx.filterExpr();
        Object res = this.visit(f);
        this.context.updateCurrent(res);
        return wrapper(res);
    }

    @Override
    public PropertyWrapper visitFilterExpr(ObjectPathParser.FilterExprContext ctx) {
        if (this.config.isAutoCreateCollectionWhenInvokeMethod()
            && (!Optional.ofNullable(this.context.currentValue()).isPresent())) {
            this.context.updateCurrent(new ArrayList<>());

        }
        Object res = this.context.filter((o -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            PropertyWrapper match = childVisitor.visit(ctx.expr());
            return match.read(Boolean.class);
        }));
        this.context.updateCurrent(res);
        return wrapper(res);
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
    public PropertyWrapper visitEQ(ObjectPathParser.EQContext ctx) {
        this.context.disableWrite();
        PropertyWrapper left = visit(ctx.getChild(0));
        PropertyWrapper right = visit(ctx.getChild(2));
        if (left.isNull()) {
            return wrapper(right == null);
        }
        if (right.isNull()) {
            return wrapper(false);
        }
        // 尝试将二者转换为数字进行比较
        Number ln = context.covertToNumber(left);
        Number rn = context.covertToNumber(right);
        if (ln != null && rn != null) {
            return wrapper(ln.doubleValue() == rn.doubleValue());
        }
        // 转换为字符串进行比较
        String ls = left.toString();
        String rs = right.toString();
        return wrapper(ls.equals(rs));
    }

    @Override
    public PropertyWrapper visitNEQ(ObjectPathParser.NEQContext ctx) {
        this.context.disableWrite();
        PropertyWrapper left = visit(ctx.getChild(0));
        PropertyWrapper right = visit(ctx.getChild(2));
        if (left.isNull()) {
            return wrapper(right != null);
        }
        if (right.isNull()) {
            return wrapper(false);
        }
        if (ReflectionHelper.isAssignableFrom(Number.class, left.readType())
            && ReflectionHelper.isAssignableFrom(Number.class, right.readType())
        ) {
            return wrapper((left.read(Number.class).doubleValue() != right.read(Number.class).doubleValue()));
        }
        if (ReflectionHelper.isAssignableFrom(String.class, left.readType())
            && ReflectionHelper.isAssignableFrom(String.class, right.readType())) {
            return wrapper(!left.read(String.class).equals(right.read(String.class)));
        }
        if (ReflectionHelper.isAssignableFrom(Boolean.class, left.readType())
            && ReflectionHelper.isAssignableFrom(Boolean.class, right.readType())
        ) {
            return wrapper(!Objects.equals(left.read(Boolean.class), right.read(Boolean.class)));
        }
        return wrapper(!Objects.equals(left.read(), right.read()));
    }

    @Override
    public PropertyWrapper visitLT(ObjectPathParser.LTContext ctx) {
        this.context.disableWrite();
        ParseTree left = ctx.getChild(0);
        ParseTree right = ctx.getChild(2);
        return wrapper(compareNumber(left, right, (l, r) -> l.doubleValue() < r.doubleValue()));
    }

    @Override
    public PropertyWrapper visitLE(ObjectPathParser.LEContext ctx) {
        this.context.disableWrite();
        ParseTree left = ctx.getChild(0);
        ParseTree right = ctx.getChild(2);
        return wrapper(compareNumber(left, right, (l, r) -> l.doubleValue() <= r.doubleValue()));
    }

    @Override
    public PropertyWrapper visitGT(ObjectPathParser.GTContext ctx) {
        this.context.disableWrite();
        ParseTree left = ctx.getChild(0);
        ParseTree right = ctx.getChild(2);
        return wrapper(compareNumber(left, right, (l, r) -> l.doubleValue() > r.doubleValue()));
    }

    @Override
    public PropertyWrapper visitGE(ObjectPathParser.GEContext ctx) {
        this.context.disableWrite();
        ParseTree left = ctx.getChild(0);
        ParseTree right = ctx.getChild(2);
        return wrapper(compareNumber(left, right, (l, r) -> l.doubleValue() >= r.doubleValue()));
    }

    protected boolean compareNumber(ParseTree leftTree, ParseTree rightTree, NumberCompare compare) {
        PropertyWrapper left = visit(leftTree);
        PropertyWrapper right = visit(rightTree);
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
    public PropertyWrapper visitUP(ObjectPathParser.UPContext ctx) {
        int number = Optional.ofNullable(ctx.expr())
                .map(e-> this.createChildVisitor().visit(e).read(Number.class).intValue())
                .orElse(-1);
        // 获取当前对象,要求当前对象必须是一个集合
        // 然后开始降低数组维度
        List<Object> objects = up(context.covertToList(), number);
        this.context.updateParent();
        this.context.updateCurrent(objects);
        return wrapper(objects);
    }

    protected List<Object> up(Collection<?> objects, int number) {
        List<Object> newObjects = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof Collection) {
                Collection<?> list = (Collection<?>) object;
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
    public PropertyWrapper visitISNULL(ObjectPathParser.ISNULLContext ctx) {
        this.context.disableWrite();
        return wrapper(independentContextVisit(ctx.getChild(0)).isNull());
    }

    @Override
    public PropertyWrapper visitLREGEX(ObjectPathParser.LREGEXContext ctx) {
        this.context.disableWrite();
        String regex = ctx.getChild(0).getText();
        PropertyWrapper str = independentContextVisit(ctx.getChild(2));
        if (str.isNull() || regex == null) {
            return wrapper(false);
        }
        return wrapper(useRegexTest(regex, str.read()));
    }

    @Override
    public PropertyWrapper visitRREGEX(ObjectPathParser.RREGEXContext ctx) {
        this.context.disableWrite();
        PropertyWrapper str = independentContextVisit(ctx.getChild(0));
        String regex = ctx.getChild(2).getText();
        if (str.isNull() || regex == null) {
            return wrapper(false);
        }
        return wrapper(useRegexTest(regex, str.read()));
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
    public PropertyWrapper visitBETWEEN(ObjectPathParser.BETWEENContext ctx) {
        this.context.disableWrite();
        PropertyWrapper target =this.context.currentValue();

        PropertyWrapper left = independentContextVisit(ctx.expr(0));
        PropertyWrapper right = independentContextVisit(ctx.expr(1));
        if (target.isNull() || left.isNull() || right.isNull()) {
            return wrapper(false);
        }
        Number l = context.covertToNumber(left);
        Number r = context.covertToNumber(right);
        Number t = context.covertToNumber(target);
        if (l == null || r == null || t == null) {
            return wrapper(false);
        }
        return wrapper(l.doubleValue() <= t.doubleValue() && t.doubleValue() <= r.doubleValue());
    }

    @Override
    public PropertyWrapper visitLIKE(ObjectPathParser.LIKEContext ctx) {
        this.context.disableWrite();
        PropertyWrapper target = independentContextVisit(ctx.expr(0));
        String likeStr = unquote(independentContextVisit(ctx.expr(1)).read(String.class));
        return parseLike(likeStr, target);
    }

    private PropertyWrapper parseLike(String likeStr, PropertyWrapper target) {
        if (likeStr.startsWith("%") && likeStr.endsWith("%")) {
            return wrapper(target.read().toString().contains(likeStr.substring(1, likeStr.length() - 1)));
        }
        if (likeStr.startsWith("%")) {
            return wrapper(target.read().toString().endsWith(likeStr.substring(1)));
        }
        if (likeStr.endsWith("%")) {
            return wrapper(target.read().toString().startsWith(likeStr.substring(0, likeStr.length() - 1)));
        }
        return wrapper(false);
    }

    @Override
    public PropertyWrapper visitLIKE_METHOD(ObjectPathParser.LIKE_METHODContext ctx) {
        this.context.disableWrite();
        PropertyWrapper target = this.context.currentValue();
        String likeStr = unquote(independentContextVisit(ctx.expr()).read(String.class));
        return parseLike(likeStr, target);
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
    public PropertyWrapper visitIN(ObjectPathParser.INContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.ArgsContext args = ctx.args();
        PropertyWrapper list = independentContextVisit(args);
        PropertyWrapper target = this.context.currentValue();
        if (list.isNull() || target.isNull()) {
            return wrapper(false);
        }
        if (ReflectionHelper.isAssignableFrom(List.class, list.readType())) {
            return wrapper((list.read(List.class).contains(target.read())));
        }
        return wrapper(false);
    }

    @Override
    public PropertyWrapper visitNOTIN(ObjectPathParser.NOTINContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.ArgsContext args = ctx.args();
        PropertyWrapper list = independentContextVisit(args);
        PropertyWrapper target = this.context.currentValue();
        if (list.isNull() || target.isNull()) {
            return wrapper(false);
        }
        if (ReflectionHelper.isAssignableFrom(List.class, list.readType())) {
            return wrapper(!list.read(List.class).contains(target.read()));
        }
        return wrapper(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyWrapper visitCONTAINS(ObjectPathParser.CONTAINSContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.ArgsContext args = ctx.args();
        PropertyWrapper target = this.context.currentValue();
        PropertyWrapper filter = independentContextVisit(args);
        if (filter.isNull() || target.isNull()) {
            return wrapper(false);
        }
        if (ReflectionHelper.isAssignableFrom(Collection.class, target.readType())) {
            return wrapper((target.read(Collection.class).containsAll(filter.read(Collection.class))));
        }
        if (ReflectionHelper.isAssignableFrom(String.class, target.readType())) {
            String text = target.read(String.class);
            return wrapper(filter.read(Collection.class)
                    .stream()
                    .allMatch(s -> text.contains(s.toString())));
        }
        return wrapper(false);
    }

    @Override
    public PropertyWrapper visitSTARTSWITH(ObjectPathParser.STARTSWITHContext ctx) {
        this.context.disableWrite();
        PropertyWrapper target = this.context.currentValue();
        PropertyWrapper prefix = independentContextVisit(ctx.arg());
        if (target.isNull() || prefix.isNull()) {
            return wrapper(false);
        }
        return wrapper(target.read().toString().startsWith(prefix.read().toString()));
    }

    @Override
    public PropertyWrapper visitENDSWITH(ObjectPathParser.ENDSWITHContext ctx) {
        this.context.disableWrite();
        PropertyWrapper target = this.context.currentValue();
        PropertyWrapper suffix = independentContextVisit(ctx.arg());
        if (target.isNull() || suffix.isNull()) {
            return wrapper(false);
        }
        return wrapper(target.read().toString().endsWith(suffix.read().toString()));
    }

    @Override
    public PropertyWrapper visitMATCHES(ObjectPathParser.MATCHESContext ctx) {
        this.context.disableWrite();
        PropertyWrapper target = this.context.currentValue();
        PropertyWrapper regex = independentContextVisit(ctx.arg());
        if (target.isNull() || regex.isNull()) {
            return wrapper(false);
        }
        return wrapper(target.read().toString().matches(regex.read().toString()));
    }

    @Override
    public PropertyWrapper visitAND(ObjectPathParser.ANDContext ctx) {
        this.context.disableWrite();
        // 需要创建子上下文来评估表达式
        PropertyWrapper left = independentContextVisit(ctx.getChild(0));
        PropertyWrapper right = independentContextVisit(ctx.getChild(2));
        if (left.isNull() || right.isNull()) {
            return wrapper(false);
        }
        return wrapper(left.read(boolean.class)&&right.read(boolean.class));
    }

    @Override
    public PropertyWrapper visitOR(ObjectPathParser.ORContext ctx) {
        this.context.disableWrite();
        // 需要创建子上下文来评估表达式
        ObjectPathParserContext lc = this.context.createChild(this.context.currentValue());
        ObjectPathVisitor lv = new ObjectPathVisitor(lc);

        ObjectPathParserContext rc = this.context.createChild(this.context.currentValue());
        ObjectPathVisitor rv = new ObjectPathVisitor(rc);

        PropertyWrapper left = lv.visit(ctx.getChild(0));
        PropertyWrapper right = rv.visit(ctx.getChild(2));
        if (left.isNull() || right.isNull()) {
            return wrapper(false);
        }
        return wrapper(left.read(boolean.class)||right.read(boolean.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyWrapper visitMIN(ObjectPathParser.MINContext ctx) {
        // 获取集合中的最小值,其要求当前元素必须是一个集合
        List<Object> objects = this.context.covertToList();
        // 此时需要对集合中的元素进行比较,找到最小值
        // 此时会有两个分支,一个是提供了筛选表达式,一个是没有提供筛选表达式
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Optional.ofNullable(expr).isPresent()) {
            // 提供了筛选表达式,则需要先筛选元素,然后再比较元素的大小
            // 这里的表达式访问的是集合中的每一个元素
            Object res = objects.stream().min((o1, o2) -> {
                // 创建子上下文
                ObjectPathParserContext child1 = this.context.createChild(o1);
                ObjectPathParserContext child2 = this.context.createChild(o2);

                // 创建子访问器
                ObjectPathVisitor childVisitor1 = new ObjectPathVisitor(child1);
                ObjectPathVisitor childVisitor2 = new ObjectPathVisitor(child2);

                PropertyWrapper o1Pw = childVisitor1.visit(expr);
                PropertyWrapper o2Pw = childVisitor2.visit(expr);
                if (o1Pw.isInstanceOf(Comparable.class) && o2Pw.isInstanceOf(Comparable.class)) {
                    return ((Comparable<Object>) o1Pw.read()).compareTo(o2Pw.read());
                }
                return 0;
            }).orElse(null);
            this.context.updateCurrent(res);
            return wrapper(res);
        }
        // 没有提供筛选表达式,则直接比较元素的大小,找到最小值,这要求元素必须是可比较的,即实现了Comparable接口
        Object res = objects.stream().min((o1, o2) -> {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
            return 0;
        }).orElse(null);
        this.context.updateCurrent(res);
        return wrapper(res);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyWrapper visitMAX(ObjectPathParser.MAXContext ctx) {
        // 获取集合中的最大值,其要求当前元素必须是一个集合
        List<Object> objects = this.context.covertToList();
        // 此时需要对集合中的元素进行比较,找到最大值
        // 此时会有两个分支,一个是提供了筛选表达式,一个是没有提供筛选表达式
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Optional.ofNullable(expr).isPresent()) {
            // 提供了筛选表达式,则需要先筛选元素,然后再比较元素的大小
            // 这里的表达式访问的是集合中的每一个元素
            Object res = objects.stream().max((o1, o2) -> {
                // 创建子上下文
                ObjectPathParserContext child1 = this.context.createChild(o1);
                ObjectPathParserContext child2 = this.context.createChild(o2);

                // 创建子访问器
                ObjectPathVisitor childVisitor1 = new ObjectPathVisitor(child1);
                ObjectPathVisitor childVisitor2 = new ObjectPathVisitor(child2);

                PropertyWrapper o1Pw = childVisitor1.visit(expr);
                PropertyWrapper o2Pw = childVisitor2.visit(expr);
                if (o1Pw.isInstanceOf(Comparable.class) && o2Pw.isInstanceOf(Comparable.class)) {
                    return ((Comparable<Object>) o1Pw.read()).compareTo(o2Pw.read());
                }
                return 0;
            }).orElse(null);
            this.context.updateCurrent(res);
            return wrapper(res);
        }
        // 没有提供筛选表达式,则直接比较元素的大小,找到最大值,这要求元素必须是可比较的,即实现了Comparable接口
        Object res = objects.stream().max((o1, o2) -> {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return ((Comparable<Object>) o1).compareTo(o2);
            }
            return 0;
        }).orElse(null);
        this.context.updateCurrent(res);
        return wrapper(res);
    }

    @Override
    public PropertyWrapper visitAVG(ObjectPathParser.AVGContext ctx) {
        this.context.disableWrite();
        List<?> objects = this.context.covertToList();

        // 计算平均值,要求元素必须是数字
        double avg = objects.stream().map(o -> Optional.ofNullable(ctx.expr()).map(e -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            return childVisitor.visit(e);
        }).orElse(wrapper(o))).mapToDouble(o -> this.context.covertToNumber(o).doubleValue()).average().orElse(0);
        this.context.updateCurrent(avg);
        return wrapper(avg);
    }

    @Override
    public PropertyWrapper visitSUM(ObjectPathParser.SUMContext ctx) {
        this.context.disableWrite();
        List<?> objects = this.context.covertToList();
        // 计算和,要求元素必须是数字
        double sum = objects.stream().map(o -> Optional.ofNullable(ctx.expr()).map(e -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            return childVisitor.visit(e);
        }).orElse(wrapper(o))).mapToDouble(o -> this.context.covertToNumber(o).doubleValue()).sum();
        this.context.updateCurrent(sum);
        return wrapper(sum);
    }

    @Override
    public PropertyWrapper visitSIZE(ObjectPathParser.SIZEContext ctx) {
        this.context.disableWrite();
        List<?> objects = this.context.covertToList();
        int size = objects.size();
        this.context.updateCurrent(size);
        return wrapper(size);
    }

    @Override
    public PropertyWrapper visitISEMPTY(ObjectPathParser.ISEMPTYContext ctx) {
        this.context.disableWrite();
        List<?> objects = this.context.covertToList();
        boolean empty = objects.isEmpty();
        this.context.updateCurrent(empty);
        return wrapper(empty);
    }

    @Override
    public PropertyWrapper visitLENGTH(ObjectPathParser.LENGTHContext ctx) {
        this.context.disableWrite();
        // 字符串,数组,集合,Map的长度
        PropertyWrapper obj = this.context.currentValue();
        if (ReflectionHelper.isAssignableFrom(String.class, obj.readType())) {
            int length = obj.read().toString().length();
            this.context.updateCurrent(length);
            return wrapper(length);
        }
        if (obj instanceof Map) {
            int length = ((Map<?, ?>) obj).size();
            this.context.updateCurrent(length);
            return wrapper(length);
        }
        List<Object> objects = this.context.covertToList();
        int length = objects.size();
        this.context.updateCurrent(length);
        return wrapper(length);
    }

    /**
     * 获取map的keys集合或者对象的字段集合
     *
     * @param ctx the parse tree
     * @return 集合
     */
    @Override
    public PropertyWrapper visitKEYS(ObjectPathParser.KEYSContext ctx) {
        this.context.disableWrite();
        List<Object> keys = new ArrayList<>();
        this.context.eachKey(keys::add);
        return this.context.updateCurrent(keys);
    }

    @Override
    public PropertyWrapper visitVALUES(ObjectPathParser.VALUESContext ctx) {
        List<Object> values = new ArrayList<>();
        this.context.eachValue(values::add);
        return this.context.updateCurrent(values);
    }

    @Override
    public PropertyWrapper visitREVERSE(ObjectPathParser.REVERSEContext ctx) {
        List<Object> objects = this.context.covertToList();
        // 不能直接修改原有的集合,需要创建一个新的集合,否则会影响到原有的集合
        List<Object> newArrays=new ArrayList<>(objects);
        Collections.reverse(newArrays);
        return this.context.updateCurrent(newArrays);
    }

    @Override
    public PropertyWrapper visitSORT(ObjectPathParser.SORTContext ctx) {
        List<ObjectPathParser.ExprContext> exprs = ctx.expr();
        List<?> sorted = sortByExprs(exprs);
        return this.context.updateCurrent(sorted);
    }

    @SuppressWarnings("all")
    public List<?> sortByExprs(List<ObjectPathParser.ExprContext> exprs) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return objects;
        }

        List<ObjectPathParser.ExprContext> eps=new ArrayList<>(exprs);
        Collections.reverse(eps);
        AtomicReference<List<?>> objRef=new AtomicReference<>(objects);
        eps.stream().forEach(expr->{
             objRef.set(objRef.get().stream().sorted((o1, o2) -> {
                if (Optional.ofNullable(expr).isPresent()) {
                    // 创建子访问器
                    ObjectPathVisitor o1v = new ObjectPathVisitor(this.context.createChild(o1));
                    ObjectPathVisitor o2v = new ObjectPathVisitor(this.context.createChild(o2));
                    int i = ((Comparable<Object>) o1v.visit(expr).read()).compareTo(o2v.visit(expr).read());
                    if (i == 0) {
                        // 如果两个元素的排序值相同,则继续使用后续的排序表达式进行计算
                        for (int j = 1; j < exprs.size(); j++) {
                            ObjectPathParser.ExprContext e = exprs.get(j);
                            i = ((Comparable<Object>) o1v.visit(e).read()).compareTo(o2v.visit(e).read());
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
            }).collect(Collectors.toList()));
        });
        return objRef.get();
    }


    @Override
    public PropertyWrapper visitRSORT(ObjectPathParser.RSORTContext ctx) {
        List<ObjectPathParser.ExprContext> exprs = ctx.expr();
        List<?> sorted = sortByExprs(exprs);
        Collections.reverse(sorted);
        return this.context.updateCurrent(sorted);
    }

    /**
     * 对集合进行分组,分组操作将会将集合中的元素按照指定的表达式进行分组
     */
    @Override
    public PropertyWrapper visitGROUPBY(ObjectPathParser.GROUPBYContext ctx) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return wrapper(objects);
        }
        ObjectPathParser.ExprContext expr = ctx.expr();
        Map<Object, List<Object>> groups = objects.stream().collect(Collectors.groupingBy(o -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            return childVisitor.visit(expr).read();
        }));
        this.context.updateCurrent(groups);
        return wrapper(groups);
    }

    @Override
    public PropertyWrapper visitDISTINCT(ObjectPathParser.DISTINCTContext ctx) {
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return wrapper(objects);
        }
        if (!Optional.ofNullable(ctx.expr()).isPresent()) {
            List<Object> distinct = objects.stream().distinct().collect(Collectors.toList());
            return this.context.updateCurrent(distinct);
        }
        // 如果提供了表达式,则需要根据表达式进行去重
        ObjectPathParser.ExprContext expr = ctx.expr();
        List<Object> distinctCache = new ArrayList<>();
        List<Object> distinct = objects.stream().filter(o -> {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(o);
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            PropertyWrapper res = childVisitor.visit(expr);
            Object obj = res.read();
            if (distinctCache.contains(obj)) {
                return false;
            }
            distinctCache.add(obj);
            return true;
        }).distinct().collect(Collectors.toList());
        return this.context.updateCurrent(distinct);
    }

    @Override
    public PropertyWrapper visitJOIN(ObjectPathParser.JOINContext ctx) {
        List<Object> objects = new ArrayList<>( this.context.covertToList());
        ObjectPathParser.ArgsContext args = ctx.args();
        for (ObjectPathParser.ArgContext arg : args.arg()) {
            PropertyWrapper obj = visit(arg);
            if (obj.isInstanceOf(List.class)) {
                objects.addAll((obj.read(List.class)));
            } else {
                objects.add(obj.read());
            }
        }
        return this.context.updateCurrent(objects);
    }

    @Override
    public PropertyWrapper visitFIRST(ObjectPathParser.FIRSTContext ctx) {
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return wrapper(null);
        }
        Object first = objects.get(0);
        return this.context.updateCurrent(first);
    }

    @Override
    public PropertyWrapper visitLAST(ObjectPathParser.LASTContext ctx) {
        List<Object> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return wrapper(null);
        }
        Object last = objects.get(objects.size() - 1);
        return this.context.updateCurrent(last);
    }

    @Override
    public PropertyWrapper visitINDEXOF(ObjectPathParser.INDEXOFContext ctx) {
        List<Object> objects = this.context.covertToList();


        Number index =  this.createChildVisitor().visit(ctx.expr()).read(Number.class);
        if (objects.isEmpty()) {
            return this.context.eval(String.valueOf(index.intValue()));
        }
        if (index == null) {
            this.context.disableWrite();
            return wrapper(null);
        }
        int i = index.intValue();
        // 如果索引小于0,则从后往前数
        if (i < 0) {
            i = objects.size() + i;
        }
        if (i < 0 || i >= objects.size()) {
            return this.context.eval(String.valueOf(i));
        }
        Object obj = objects.get(i);
        return this.context.updateCurrent(obj);
    }

    /**
     * 映射方法,将当前对象映射为另一个对象
     */
    @Override
    public PropertyWrapper visitMAP(ObjectPathParser.MAPContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.ExprContext expr = ctx.expr();
        // 创建子上下文?解析表达式,获取结果,然后更新当前值
        PropertyWrapper pw = visit(expr);
        return this.context.updateCurrent(pw);
    }

    /**
     * 尝试将当前对象转换为一个map集合,支持java对象和map集合,但不支持基本类型
     */
    @Override
    public PropertyWrapper visitTOMAP_METHOD(ObjectPathParser.TOMAP_METHODContext ctx) {
        Map<Object,Object> map=new HashMap<>();
        this.context.each(entity-> map.put(entity.key(),entity.value()));
        PropertyWrapper pw = wrapper(map);
        pw.setIndexOrKey(this.context.currentValue().getIndexOrKey());
        return this.context.updateCurrent(pw);
    }

    /**
     * 对字符串进行分割操作
     */
    @Override
    public PropertyWrapper visitSPLIT(ObjectPathParser.SPLITContext ctx) {
        this.context.disableWrite();
        String s = this.context.covertToString();
        String split = unquote(visit(ctx.expr()).read(String.class));
        // 移除开头和结尾的引号
        String[] splitStr = s.split(split);
        return this.context.updateCurrent(Arrays.asList(splitStr));
    }

    @Override
    public PropertyWrapper visitREPLACE(ObjectPathParser.REPLACEContext ctx) {
        this.context.disableWrite();
        String old = independentContextVisit(ctx.expr(0)).read(String.class);
        String want = independentContextVisit(ctx.expr(1)).read(String.class);
        String s = this.context.covertToString();
        String replace = s.replace(old, want);
        return this.context.updateCurrent(replace);
    }

    @Override
    public PropertyWrapper visitTOUPPER(ObjectPathParser.TOUPPERContext ctx) {
        this.context.disableWrite();
        String upperCase = this.context.covertToString().toUpperCase();
        return this.context.updateCurrent(upperCase);
    }

    @Override
    public PropertyWrapper visitTOLOWER(ObjectPathParser.TOLOWERContext ctx) {
        this.context.disableWrite();
        String lowerCase = this.context.covertToString().toLowerCase();
        return this.context.updateCurrent(lowerCase);
    }

    @Override
    public PropertyWrapper visitSUBSTRING(ObjectPathParser.SUBSTRINGContext ctx) {
        this.context.disableWrite();
        String s = this.context.covertToString();
        int start = Optional.ofNullable(ctx.expr(0))
                .map(e-> this.createChildVisitor().visit(e).read(Number.class).intValue())
                .orElse(0);

        int end =  Optional.ofNullable(ctx.expr(1))
                .map(e-> this.createChildVisitor().visit(e).read(Number.class).intValue())
                .orElse(s.length());
        String substring = s.substring(start, end);
        return this.context.updateCurrent(substring);
    }

    @Override
    public PropertyWrapper visitLTRIM(ObjectPathParser.LTRIMContext ctx) {
        this.context.disableWrite();
        String s = this.context.covertToString();
        String trim = s.replaceAll("^\\s+", "");
        return this.context.updateCurrent(trim);
    }

    @Override
    public PropertyWrapper visitRTRIM(ObjectPathParser.RTRIMContext ctx) {
        this.context.disableWrite();
        String s = this.context.covertToString();
        String trim = s.replaceAll("\\s+$", "");
        return this.context.updateCurrent(trim);
    }

    @Override
    public PropertyWrapper visitTRIM(ObjectPathParser.TRIMContext ctx) {
        this.context.disableWrite();
        String s = this.context.covertToString();
        String trim = s.trim();
        return this.context.updateCurrent(trim);
    }

    @Override
    public PropertyWrapper visitCONCAT(ObjectPathParser.CONCATContext ctx) {
        this.context.disableWrite();
        String s = this.context.covertToString();
        PropertyWrapper exprValue = this.independentContextVisit(ctx.expr());
        if (exprValue.isNull()){
            return this.context.updateCurrent(wrapper(s));
        }
        String cs = StringHelper.unquote(exprValue.read(String.class));
        assert cs != null;
        String concat = s.concat(cs);
        return this.context.updateCurrent(concat);
    }


    @Override
    public PropertyWrapper visitScripts(ObjectPathParser.ScriptsContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.Scripts_startContext ssc = ctx.scripts_start();
        String scriptName = ssc.ID().getText();
        ObjectPathParser.NamedArgsContext namedArgsContext = ssc.namedArgs();
        PropertyWrapper nas = visit(namedArgsContext);
        String text = ctx.SCRIPT_CONTENT().getText();
        // 移除脚本中前置的>以及后置</
        if (text.startsWith(">")) {
            text = text.substring(1);
        }
        if (text.endsWith("</")) {
            text = text.substring(0, text.length() - 2);
        }
        // 此处时就获取了完整的脚本数据,脚本类型,脚本参数,脚本内容
        Object res = this.context.executeScript(scriptName, nas.read(), text);
        return this.context.updateCurrent(res);
    }

    @Override
    public PropertyWrapper visitIS_NULL_METHOD(ObjectPathParser.IS_NULL_METHODContext ctx) {
        this.context.disableWrite();
        return Optional.ofNullable(ctx.expr())
                .map(e->wrapper(this.createChildVisitor().visit(e).isNull()))
                .orElseGet(()-> wrapper(this.context.currentValue().isNull()));
    }

    @Override
    public PropertyWrapper visitNOT_NULL_METHOD(ObjectPathParser.NOT_NULL_METHODContext ctx) {
        this.context.disableWrite();
        return Optional.ofNullable(ctx.expr())
                .map(e->wrapper(this.createChildVisitor().visit(e).isNotNull()))
                .orElseGet(()-> wrapper(this.context.currentValue().isNotNull()));
    }

    @Override
    public PropertyWrapper visitAND_METHOD(ObjectPathParser.AND_METHODContext ctx) {
        this.context.disableWrite();
        for (ObjectPathParser.ExprContext expr : ctx.expr()) {
            ObjectPathParserContext lc = this.context.createChild(this.context.currentValue());
            ObjectPathVisitor lv = new ObjectPathVisitor(lc);
            PropertyWrapper result = lv.visit(expr);
            if (Boolean.FALSE.equals(result.read(boolean.class))){
                return wrapper(false);
            }
        }
        return wrapper(true);
    }

    @Override
    public PropertyWrapper visitOR_METHOD(ObjectPathParser.OR_METHODContext ctx) {
        this.context.disableWrite();
        for (ObjectPathParser.ExprContext expr : ctx.expr()) {
            ObjectPathParserContext lc = this.context.createChild(this.context.currentValue());
            ObjectPathVisitor lv = new ObjectPathVisitor(lc);
            PropertyWrapper result = lv.visit(expr);
            boolean match = result.read(boolean.class);
            if (match) {
                return wrapper(true);
            }
        }
        return wrapper(false);
    }

    @Override
    public PropertyWrapper visitNOT_METHOD(ObjectPathParser.NOT_METHODContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Objects.isNull(expr)){
            Boolean match = this.context.currentValue().read(Boolean.class);
            return wrapper(Objects.isNull(match)||!match);
        }

        ObjectPathParserContext lc = this.context.createChild(this.context.currentValue());
        ObjectPathVisitor lv = new ObjectPathVisitor(lc);

        PropertyWrapper result = lv.visit(expr);
        Boolean match = result.read(Boolean.class);
        return wrapper(Objects.isNull(match)||!match);
    }

    @Override
    public PropertyWrapper visitFILTER_METHOD(ObjectPathParser.FILTER_METHODContext ctx) {
        // 获取上下文对象,并重新构建为集合对象用于过滤操作
        List<?> objects = this.context.covertToList();
        if (objects.isEmpty()) {
            return wrapper(objects);
        }
        List<Object> newValues = new ArrayList<>();
        objects.forEach(obj -> {
            boolean mat = ctx.expr().stream().allMatch(expr -> {
                ObjectPathParserContext lc = this.context.createContext(
                        this.context.rootValue(),
                        this.context.currentValue(),
                        wrapper(obj)
                );
                ObjectPathVisitor lv = new ObjectPathVisitor(lc);
                PropertyWrapper result = lv.visit(expr);
                return result.read(Boolean.class);
            });
            if (mat) {
                newValues.add(obj);
            }
        });
        return wrapper(newValues);
    }

    @Override
    public PropertyWrapper visitTOSTRING_METHOD(ObjectPathParser.TOSTRING_METHODContext ctx) {
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Objects.isNull(expr)){
            return wrapper(this.context.currentValue().toString());
        }

        List<?> pw =Optional.ofNullable(ctx.args()).map(a->visit(a).read(List.class))
                .orElse(Collections.emptyList());

        List<String> args = pw
                .stream()
                .map(o->{
                    IConvert convert = this.context.getConfiguration().getConvert();
                    return convert.convert(o,String.class);
                })
                .collect(Collectors.toList());

        int size = args.size();
        String concatText = size>0?args.get(0):"";
        String startText=size>1?args.get(1):"";
        String endText=size>2?args.get(2):"";
        StringBuilder stringBuilder=new StringBuilder(startText);
        this.context.each(entity -> {
            ObjectPathVisitor cv = this.createChildVisitor(this.context.createChild(entity));
            stringBuilder.append(cv.visit(expr).read(String.class))
                    .append(concatText);
        });
        String text=stringBuilder.toString();
        if(text.endsWith(endText)){
            text=text.substring(0,text.length()-endText.length());
        }
        text=text+endText;
        return wrapper(text);
    }

    @Override
    public PropertyWrapper visitNamedArgs(ObjectPathParser.NamedArgsContext ctx) {
        Map<Object, Object> namedArgs = new HashMap<>();
        List<ObjectPathParser.NamedArgContext> namedArg = ctx.namedArg();
        for (ObjectPathParser.NamedArgContext nac : namedArg) {
            String name = nac.ID().getText();
            PropertyWrapper obj = visit(nac.arg());
            namedArgs.put(name, obj.read());
        }
        return wrapper(namedArgs);
    }

    @Override
    public PropertyWrapper visitREVERT_EXPR(ObjectPathParser.REVERT_EXPRContext ctx) {
        PropertyWrapper res = visit(ctx.expr());
        return wrapper(!res.read(Boolean.class));
    }

    @Override
    public PropertyWrapper visitRAW_METHOD(ObjectPathParser.RAW_METHODContext ctx) {
        this.context.disableWrite();
        ObjectPathParser.ExprContext methodName = ctx.methodName;
        String name = unquote(independentContextVisit(methodName).read(String.class));
        // 获取调用时的参数列表,是一个参数集合
        List<Object> args = new ArrayList<>();
        ObjectPathParser.ArgsContext argsCtx = ctx.args();
        if (Objects.nonNull(argsCtx)){
            PropertyWrapper argsPW = independentContextVisit(ctx.args());
            args=argsPW.read(List.class);
        }
        // 调用方法
        PropertyWrapper res = this.context.invokeRawMethod(name, args);
        return this.context.updateCurrent(res);
    }

    @Override
    public PropertyWrapper visitArgs(ObjectPathParser.ArgsContext ctx) {
        List<Object> args = new ArrayList<>();
        List<ObjectPathParser.ArgContext> arg = ctx.arg();
        // 此处需要为每一个参数创建子上下文来访问
        for (ObjectPathParser.ArgContext a : arg) {
            // 创建子上下文
            ObjectPathParserContext child = this.context.createChild(this.context.currentValue());
            // 创建子访问器
            ObjectPathVisitor childVisitor = new ObjectPathVisitor(child);
            PropertyWrapper res = childVisitor.visit(a);
            args.add(res.read());
        }
        return wrapper(args);
    }

    @Override
    public PropertyWrapper visitArg(ObjectPathParser.ArgContext ctx) {
        ObjectPathParser.ExprContext expr = ctx.expr();
        if (Objects.nonNull(expr)) {
            return visit(expr);
        }
        ObjectPathParser.ComplexValueContext complexValue = ctx.complexValue();
        if (Objects.nonNull(complexValue)) {
            return visit(complexValue);
        }
        ObjectPathParser.ValueContext value = ctx.value();
        if (Objects.nonNull(value)) {
            return visit(value);
        }
        throw new PropertyAccessorRuntimeException("");
    }

    @Override
    public PropertyWrapper visitComplexValue(ObjectPathParser.ComplexValueContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public PropertyWrapper visitJson(ObjectPathParser.JsonContext ctx) {
        return visit(ctx.getChild(0));
    }

    @Override
    public PropertyWrapper visitObject(ObjectPathParser.ObjectContext ctx) {
        return this.context.parseJson(ctx.getText());
    }

    @Override
    public PropertyWrapper visitArray(ObjectPathParser.ArrayContext ctx) {
        return this.context.parseJson(ctx.getText());
    }

    @Override
    public PropertyWrapper visitValue(ObjectPathParser.ValueContext ctx) {
        if (Optional.ofNullable(ctx.ID()).isPresent()) {
            String id = ctx.ID().getText();
            id = unquote(id);
            this.context = this.context.createChild(id);
            return wrapper(id);
        }
        if (Optional.ofNullable(ctx.NUMBER()).isPresent()) {
            String number = ctx.NUMBER().getText();
            if (number.endsWith("l") || number.endsWith("L")) {
                number = number.substring(0, number.length() - 1);
                long l = Long.parseLong(number);
                this.context = this.context.createChild(l);
                return wrapper(l);
            }
            if (number.contains(".")) {
                double d = Double.parseDouble(number);
                this.context = this.context.createChild(d);
                return wrapper(d);
            }
            if (number.endsWith("d") || number.endsWith("D")) {
                number = number.substring(0, number.length() - 1);
                double d = Double.parseDouble(number);
                this.context = this.context.createChild(d);
                return wrapper(d);
            }
            Integer i = Integer.parseInt(number);
            this.context = this.context.createChild(i);
            return wrapper(i);
        }
        if (Optional.ofNullable(ctx.STRING()).isPresent()) {
            String string = StringHelper.unquote(ctx.STRING().getText());
            this.context = this.context.createChild(wrapper(string));
            return wrapper(string);
        }

        if (Optional.ofNullable(ctx.BOOL()).isPresent()) {
            String boolStr = ctx.BOOL().getText();
            boolean b = Boolean.parseBoolean(boolStr);
            this.context = this.context.createChild(b);
            return wrapper(b);
        }
        return wrapper(null);
    }

    protected PropertyWrapper wrapper(Object object) {
        if (this.context.getMod().isReadOnly()) {
            return this.config.getWrapperFactory().createReadOnly(object);
        }
        if (object instanceof PropertyWrapper){
            return (PropertyWrapper)object;
        }
        return this.config.getWrapperFactory().create(object);
    }

    protected ObjectPathVisitor createChildVisitor(){
        return createChildVisitor(this.context.createChild());
    }

    protected ObjectPathVisitor createChildVisitor(ObjectPathParserContext ctx){
        return new ObjectPathVisitor(ctx);
    }

    protected PropertyWrapper independentContextVisit(ParseTree ctx){
        return this.createChildVisitor().visit(ctx);
    }

}
