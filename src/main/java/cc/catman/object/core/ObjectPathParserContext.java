package cc.catman.object.core;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.util.StringHelper;

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
    PropertyWrapper rootValue();

    /**
     * 获取父对象
     *
     * @return 父对象
     */
    PropertyWrapper parentValue();

    /**
     * 获取当前对象
     *
     * @return 当前对象
     */
    PropertyWrapper currentValue();

    /**
     * 获取配置
     *
     * @return 配置
     */
    ObjectPathConfiguration getConfiguration();

    PropertyWrapper updateCurrent(Object current);

    default void updateParent() {
        updateParent(currentValue());
    }

    @SuppressWarnings("java:S3776")
    default Class<?> analyzerType(String typeDesc){
        if (typeDesc == null) {
            return Object.class;
        }
        typeDesc=StringHelper.trim(typeDesc,"(",")");
        boolean isArray=typeDesc.endsWith("[]");
        if ("String".equalsIgnoreCase(typeDesc)
            || "str".equals(typeDesc)
        ){
            return isArray? String[].class : String.class;
        }
        switch (typeDesc) {
            case "Integer":
            case "I":
                return isArray ? Integer[].class : Integer.class;
            case "int":
            case "i":
                return isArray ? int[].class : int.class;
            case "Double":
            case "D":
                return isArray ? Double[].class : Double.class;
            case "double":
            case "d":
                return isArray ? double[].class : double.class;
            case "Float":
            case "F":
                return isArray ? Float[].class : Float.class;
            case "float":
            case "f":
                return isArray ? float[].class : float.class;
            case "Boolean":
            case "B":
                return isArray ? Boolean[].class : Boolean.class;
            case "boolean":
            case "b":
                return isArray ? boolean[].class : boolean.class;
            case "Long":
            case "L":
                return isArray ? Long[].class : Long.class;
            case "long":
            case "l":
                return isArray ? long[].class : long.class;
            case "Short":
            case "S":
                return isArray ? Short[].class : Short.class;
            case "short":
            case "s":
                return isArray ? short[].class : short.class;
            case "Byte":
                return isArray ? Byte[].class : Byte.class;
            case "byte":
                return isArray ? byte[].class : byte.class;
            case "Character":
            case "C":
                return isArray ? Character[].class : Character.class;
            case "char":
            case "c":
                return isArray ? char[].class : char.class;
            default:
                return isArray?Object[].class:Object.class;
        }
    }

    void updateParent(Object parent);

    /**
     * 创建派生子上下文
     *
     * @param location 寻址标志
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
     * 复制一个当前的上下文,供调用方使用
     */
    default ObjectPathParserContext createChild(){
        return createChild(this.currentValue());
    }
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
     * @param path 表达式
     * @return 解析结果
     */
    PropertyWrapper eval(String path);

    /**
     * 在当前上下文中获取当前对象的属性,并将属性名传递给消费者
     *
     * @param consumer 消费者
     */
    default void eachKey(Consumer<Object> consumer) {
        eachKey(this, consumer);
    }

    /**
     * 在指定上下文中获取当前对象的属性,并将属性名传递给消费者
     *
     * @param context  上下文
     * @param consumer 消费者
     */
    void eachKey(ObjectPathParserContext context, Consumer<Object> consumer);


    /**
     * 在指定上下文中获取当前对象的属性,并将属性名和属性值传递给消费者
     *
     * @param context 上下文
     * @param consumer 消费者
     */
    void each(ObjectPathParserContext context, Consumer<Entity> consumer);

    /**
     * 在当前上下文中获取当前对象的属性,并将属性名和属性值传递给消费者
     *
     * @param consumer 消费者
     */
    default void each(Consumer<Entity> consumer) {
        each(this, consumer);
    }

    /**
     * 在当前上下文中解析表达式,并获取结果
     *
     * @param consumer 消费者
     */
    default void eachValue(Consumer<Object> consumer) {
        eachValue(this, consumer);
    }


    /**
     * 在指定上下文中解析表达式,并获取结果
     *
     * @param context  上下文
     * @param consumer 消费者
     */
    void eachValue(ObjectPathParserContext context, Consumer<Object> consumer);

    /**
     * 在当前上下文中获取当前对象的属性,并将属性名和属性值传递给消费者
     *
     * @param mapper 消费者
     * @return 映射后的对象
     */
    @SuppressWarnings("all")
    default Object map(Function<Object, Object> mapper) {
        return map(this, mapper);
    }

    /**
     * 在指定上下文中获取当前对象的属性,并将属性名和属性值传递给消费者
     *
     * @param context 上下文
     * @param mapper  消费者
     * @return 映射后的对象
     */
    @SuppressWarnings("all")
    Object map(ObjectPathParserContext context, Function<Object, Object> mapper);

    /**
     * 在当前上下文中获取当前对象的属性,并将属性名和属性值传递给消费者
     *
     * @param filter 过滤器
     * @return 过滤后的对象
     */
    default Object filter(Predicate<Object> filter) {
        return filter(this, filter);
    }

    /**
     * 在指定上下文中获取当前对象的属性,并将属性名和属性值传递给消费者
     *
     * @param context 上下文
     * @param filter  过滤器
     * @return 过滤后的对象
     */
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
     * @return 反转后的数据
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
     * @param object 对象
     * @return 字符串
     */
    String covertToString(PropertyWrapper object);

    default String covertToString() {
        return covertToString(currentValue());
    }

    /**
     * 将对象转换为数值
     * @param object 对象
     * @return 数值
     */
    Number covertToNumber(PropertyWrapper object);

    boolean isTrue(PropertyWrapper pw);
    /**
     * 将字符串转换为对象
     *
     * @param json 字符串
     * @return 对象
     */
    PropertyWrapper parseJson(String json);

    /**
     * 重写对象
     *
     * @param object 对象
     * @return 重写对象
     */
    PropertyWrapper rewrite(Object object);

    PropertyWrapper invokeMethod(String functionName, List<Object> params);

    /**
     * 调用当前对象的原始方法
     * @param name 方法mc
     * @param args 参数列表
     * @return 执行结果
     */
    PropertyWrapper invokeRawMethod(String name,List<Object> args);

    PropertyWrapper executeScript(String scriptName, Object nas, String text);

    /**
     * 当前模式是否是只读的
     */
    EContextMod  getMod();

    void setMod(EContextMod mod);

    boolean readOnly();

    void disableWrite();

    void checkWrite();
}
