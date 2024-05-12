package cc.catman.object;

/**
 * 对象路径
 * @author jpanda
 * @since 0.0.1
 */
public interface IObjectPath {
    /**
     * 解析
     * @param path   路径
     * @return 对象
     */
    ObjectPathAccessor parse(String path);
}
