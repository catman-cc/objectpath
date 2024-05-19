package cc.catman.object.core;

/**
 * 一些功能特性开关
 * @author jpanda
 * @since 0.0.1
 */
public enum Features {
    /**
     * 当访问null属性时,抛出异常
     */
    THROW_EXCEPTION_WHEN_ACCESS_NULL_PROPERTY,
    /**
     * 当属性不存在时,抛出异常
     */
    THROW_EXCEPTION_WHEN_PROPERTY_NOT_FOUND,
    /**
     * 忽略类型转换异常,如果转换异常,则返回null
     */
    IGNORE_TYPE_CAST_EXCEPTION,

    /**
     * 允许使用加号连接字符串
     */
    CONNECT_STRING_USE_PLUS_SIGN,

}
