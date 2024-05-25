package cc.catman.object.core.json;

import cc.catman.object.ObjectPathConfiguration;

/**
 * JSON编码器
 *
 * @author jpanda
 * @since 0.0.1
 */
public interface JsonCoder {
    /**
     * 编码对象为JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    String encode(Object object);

    /**
     * 解码JSON字符串为对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>  对象类型
     * @return 对象
     */
    <T> T decode(String json, Class<T> clazz);

    /**
     * 当该编码器被配置时调用
     * @param configuration 所属的配置
     */
    default void init(ObjectPathConfiguration configuration){}
}
