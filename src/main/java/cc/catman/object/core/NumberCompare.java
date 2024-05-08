package cc.catman.object.core;

/**
 * 数字比较
 */
@FunctionalInterface
public interface NumberCompare {
    boolean compare(Number number1, Number number2);
}
