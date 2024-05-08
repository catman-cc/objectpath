package cc.catman.object.core;

/**
 * 模式
 * 标准模式: 仅提供最基本的支持,包含jsonpath的所有通用操作,以及一些额外的内置函数,管道操作等
 * 增强模式: 在标准模式的基础上,额外扩展了表达式的能力,比如进行数学运算,字符串操作,支持+,-,*,/,%操作,另外支持通过+号连接字符串
 *          ,除此之外,还支持一些表达式语言的特性,比如:
 *          - 三目运算符: @a > @b ? @a : @b
 *          - 简写的三目运算符: @a?: @b 如果a不为空,则返回a,否则返回b
 *          - 简写的三目运算符: @a?b 如果a不为空,则返回b,否则返回null
 *          - 支持更多的内置函数,比如:
 *          - - toJson(),表示将对象转换为json字符串
 *          - - toInt(),表示将对象转换为整数
 *          - - toLong(),表示将对象转换为长整数
 *          - - toDouble(2),表示将对象转换为浮点数,并保留两位小数
 *          - - toBoolean(),表示将对象转换为布尔值
 *          - - format("yyyy-MM-dd"),表示将对象转换为日期字符串
 *          - - parse("yyyy-MM-dd"),表示将字符串解析为日期
 *          - 支持命名管道操作,比如:
 *          - - $.time.now()|!str @.format("yyyy-MM-dd")|!date @.parse("yyyy-MM-dd")|{data: !str, date: !date}
 *          - - - 表示先获取当前时间,然后转换为字符串,然后格式化为日期,最后将结果封装为一个对象,其中!表示为当前管道起名,
 *          - - - 然后后面通过!name的方式引用
 *          - 支持自定义函数,比如:
 *          - - $.name.query("select * from table where id = ?", @)
 *          - - 表示调用了一个名为query的自定义函数,并传入了两个参数,第一个是字符串,第二个是当前上下文对象@,即name的值
 *
 * @author jpanda
 * @since 0.0.1
 */
public enum EModel {
    /**
     * 标准模式
     */
    STANDARD,
    /**
     * 增强模式
     */
    ENHANCED
}
