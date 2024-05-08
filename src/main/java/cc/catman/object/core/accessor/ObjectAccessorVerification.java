package cc.catman.object.core.accessor;

/**
 * 对象访问器验证器,创建该接口的目的是为了简化对象访问器中判断逻辑的复杂度
 * @author jpanda
 * @since 2024-04-23
 */
@FunctionalInterface
public interface ObjectAccessorVerification {
    /**
     * 验证对象是否支持访问
     * @param object 需要验证的对象
     * @param key 需要访问的索引
     * @return 验证结果
     */
    ObjectAccessorVerificationResult verify(Object object, Object key);
}
