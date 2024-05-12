package cc.catman;

import cc.catman.object.ObjectPath;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.ObjectPathConfiguration;

/**
 * 一个简化ObjectPath操作的类
 * @author jpanda
 * @since 0.0.1
 */
public final class OP {
    /**
     * 配置对象
     */
    private static ObjectPathConfiguration config;

    private OP() {
    }

    /**
     * 设置配置对象
     * @param config 配置对象
     */
    public static void setConfig(ObjectPathConfiguration config) {
        OP.config = config;
    }

    /**
     * 解析路径
     * @param path 路径
     * @return ObjectPathAccessor
     */
    public static ObjectPathAccessor parse(final String path) {
        return ObjectPath.parse(path, config);
    }

}
