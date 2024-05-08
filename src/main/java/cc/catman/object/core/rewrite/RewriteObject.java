package cc.catman.object.core.rewrite;

import lombok.Data;

/**
 * 重写对象
 * @author jpanda
 * @since 0.0.1
 */
@Data
public class RewriteObject {
    /**
     * 原始对象
     */
    private final Object raw;
    /**
     * 重写对象
     */
    private final Object rewrite;


    public static RewriteObject of(Object raw){
        return new RewriteObject(raw);
    }
    public static RewriteObject of(Object raw,Object rewrite){
        return new RewriteObject(raw,rewrite);
    }

    public RewriteObject(Object raw) {
        this(raw,raw);
    }

    public RewriteObject(Object raw, Object rewrite) {
        this.raw = raw;
        this.rewrite = rewrite;
    }


    /**
     * 是否重写了对象,如果重写了对象则返回true,此处直接比较对象的引用
     * @return 是否重写了对象
     */
    boolean isRewrite(){
        if (raw==null){
            return rewrite!=null;
        }
        return raw!=rewrite;
    }

}
