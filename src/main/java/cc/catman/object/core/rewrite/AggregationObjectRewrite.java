package cc.catman.object.core.rewrite;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
/**
 * 聚合对象重写器,将多个对象重写器聚合在一起,当重写对象时,会依次调用每个对象重写器
 * 如果一个对象被重写了,那么重写后的对象会被继续重写,直到没有对象被重写
 * @author jpanda
 * @since 0.0.1
 */
@Getter
@Slf4j
public class AggregationObjectRewrite implements ObjectRewrite{
    /**
     * 重写器集合
     */
    private final List<ObjectRewrite> rewrites;

    public AggregationObjectRewrite() {
        this(new ArrayList<>());
    }

    public AggregationObjectRewrite(List<ObjectRewrite> rewrites) {
        this.rewrites = rewrites;
    }

    /**
     * 重写对象,如果一个对象被重写了,那么重写后的对象会被继续重写,直到没有对象被重写
     * @param object 对象
     * @return 重写对象
     */
    @Override
    public RewriteObject rewrite(final Object object) {
        return this.rewrites.stream()
                .map(rewrite -> rewrite.rewrite(object))
                .filter(RewriteObject::isRewrite)
                .findFirst()
                .map(RewriteObject::getRewrite)
                .map(this::rewrite)
                .orElseGet(() -> RewriteObject.of(object));
    }

    public AggregationObjectRewrite add(ObjectRewrite rewrite){
        rewrites.add(rewrite);
        return this;
    }
}
