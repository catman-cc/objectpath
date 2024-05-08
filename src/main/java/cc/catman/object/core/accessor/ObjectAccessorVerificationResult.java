package cc.catman.object.core.accessor;

/**
 * 对象访问器验证结果,包含三种可能性:
 * - 通过验证,支持访问
 * - 未通过验证,不支持访问
 * - 未知,无法判断是否支持访问
 * @author jpanda
 * @since 2024-04-23
 */
public enum ObjectAccessorVerificationResult {
    PASS,
    FAIL,
    UNKNOWN
}
