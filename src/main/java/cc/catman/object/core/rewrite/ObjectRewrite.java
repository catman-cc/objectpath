package cc.catman.object.core.rewrite;
/**
 * 对象重写接口
 * 通过对象重写的方式来实现对指定对象的重写操作
 * @author jpanda
 * @since 0.0.1
 */
@FunctionalInterface
public interface ObjectRewrite {

    /**
     * 重写对象
     * @param object 对象
     * @return 重写对象
     */
    RewriteObject rewrite(Object object);
}
