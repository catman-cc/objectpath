package cc.catman.object.core.generic;

/**
 * 泛型类型定义,目前只支持一级泛型,如果是多维泛型,则无法进行推断
 * @author jpanda
 * @since 0.00.3
 */
public @interface Generic {
    /**
     * 泛型类型,用于标记属性的类型
     * @return 泛型类型
     */
    Class<?>[] value() default {Object.class};
}
