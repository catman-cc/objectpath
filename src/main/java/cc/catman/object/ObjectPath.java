package cc.catman.object;

/**
 * 对象路径
 */
public interface ObjectPath {
    /**
     * 解析
     * @param path   路径
     * @return 对象
     */
    ObjectPathAccessor parse(String path);
}
