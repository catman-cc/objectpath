package cc.catman.object.core;

import cc.catman.object.ObjectPathConfiguration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * ObjectPath解析器上下文
 *
 * @author jpanda
 */
public interface ObjectPathParserContext {

    /**
     * 当前上下文所属的父上下文,注意,这里的父上下文不是指当前上下文的父对象,而是指当前上下文的创建者
     * 同时跟上下文没有父上下文,即跟上下文的父上下文为null
     *
     * @return 父上下文
     */
    ObjectPathParserContext parent();

    /**
     * 获取根对象
     *
     * @return 根对象
     */
    Object rootValue();

    /**
     * 获取父对象
     *
     * @return 父对象
     */
    Object parentValue();

    /**
     * 获取当前对象
     *
     * @return 当前对象
     */
    Object currentValue();

    /**
     * 获取配置
     *
     * @return 配置
     */
    ObjectPathConfiguration getConfiguration();

    void updateCurrent(Object current);

    default void updateParent() {
        updateParent(currentValue());
    }

    void updateParent(Object parent);

    /**
     * 创建派生子上下文
     *
     * @return 子上下文
     */
    ObjectPathParserContext createChildContext(ELocation location);

    /**
     * 创建上下文
     *
     * @param root    根对象
     * @param parent  父对象
     * @param current 当前对象
     * @return 上下文
     */
    ObjectPathParserContext createContext(Object root, Object parent, Object current);

    /**
     * 创建子上下文
     *
     * @param current 当前对象
     * @return 子上下文
     */
    ObjectPathParserContext createChild(Object current);

    /**
     * 在指定上下文中解析表达式,并获取结果
     *
     * @return 解析结果
     */
    Object eval(String path);

    default void eachKey(Consumer<Object> consumer) {
        eachKey(this, consumer);
    }

    void eachKey(ObjectPathParserContext context, Consumer<Object> consumer);


    void each(ObjectPathParserContext context, Consumer<Entity> consumer);

    default void each(Consumer<Entity> consumer) {
        each(this, consumer);
    }

    /**
     * 在当前上下文中解析表达式,并获取结果
     */
    default void eachValue(Consumer<Object> consumer) {
        eachValue(this, consumer);
    }


    /**
     * 在指定上下文中解析表达式,并获取结果
     */
    void eachValue(ObjectPathParserContext context, Consumer<Object> consumer);

    @SuppressWarnings("all")
    default Object map(Function<Object, Object> mapper) {
        return map(this, mapper);
    }
    @SuppressWarnings("all")
    Object map(ObjectPathParserContext context, Function<Object, Object> mapper);

    default Object filter(Predicate<Object> filter) {
        return filter(this, filter);
    }

    Object filter(ObjectPathParserContext context, Predicate<Object> filter);

    /**
     * 获取当前上下文对象的大小,如果是集合则返回集合的大小,如果是map则返回map的大小
     * 如果是普通对象,则返回字段的数量,这里后面应该会做调整
     *
     * @return 大小
     */
    int size();

    /**
     * 获取当前上下文对象的所有key
     *
     * @return key列表
     */
    List<String> keys();

    /**
     * 获取当前上下文对象的所有value
     *
     * @return value列表
     */
    List<String> values();

    /**
     * 反转集合中的数据
     */
    List<String> reverse();

    /**
     * 将当前上下文对象转换为List
     *
     * @return List
     */
    List<Object> covertToList();

    /**
     * 将对象转换为字符串,默认情况下,不会处理对象,集合,数组等
     *
     * @return 字符串
     */
    String covertToString(Object object);

    default String covertToString() {
        return covertToString(currentValue());
    }

    /**
     * 将对象转换为数值
     *
     * @return 数值
     */
    Number covertToNumber(Object object);

    /**
     * 将字符串转换为对象
     *
     * @param json 字符串
     * @return 对象
     */
    Object parseJson(String json);

    /**
     * 重写对象
     *
     * @param object 对象
     * @return 重写对象
     */
    Object rewrite(Object object);

    Object invokeMethod(String functionName, List<Object> params);

    Object executeScript(String scriptName, Object nas, String text);
}
