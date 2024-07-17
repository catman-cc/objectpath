package cc.catman.object.core;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个用于标志参数可为null的注解
 * @author jpanda
 * @since 0.0.3
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER,ElementType.METHOD})
public @interface Nullable {
}
