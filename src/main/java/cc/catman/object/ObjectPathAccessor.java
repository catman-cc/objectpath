package cc.catman.object;

import cc.catman.object.path.standard.ObjectPathParser;

/**
 * 对象路径访问器
 * @author jpanda
 * @since 0.0.1
 */
public interface ObjectPathAccessor {


    /**
     * 获取对应的解析器
     * @return 获取对应的解析器
     */
    ObjectPathParser getParser();

    /**
     * 计算
     * @param object 对象
     * @return 结果
     */
    default Object eval(Object object){
        return eval(object, Object.class);
    }

    /**
     * 计算
     * @param object 对象
     * @param clazz 类型
     * @param <T> 类型
     * @return 结果
     */
    <T> T eval(Object object, Class<T> clazz);

    /**
     * 设值,并会将过滤后的数据传递给赋值表达式,在完成计算后,重新赋值
     * @param object 对象
     * @param expr 表达式
     * @return 修改后的对象
     */
    Object setExpr(Object object, String expr);

    /**
     * 设置,直接传入一个被缓存的objectpath对象来取代表达式
     * @param object 对象
     * @param objectPath 缓存后表达式对象
     * @return 修改后的对象
     */
    Object setExpr(Object object, ObjectPathParser.ObjectPathContext objectPath);

    /**
     * 设值
     * @param object 对象
     * @param value 新值
     * @return 被赋值后的值
     */
    Object setValue(Object object, Object value);
}
