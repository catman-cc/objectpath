package cc.catman.object.core.classifier;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 分类器对象,用于包装被分类的对象
 *
 * @author jpanda
 * @since 0.0.1
 */
@Getter
public abstract class AbstractClassifierObject implements ClassifierObject {
    /**
     * 原始对象
     */
    protected Object raw;
    /**
     * 分类
     */
    protected EObjectClassification classification;

    /**
     * 不支持的操作
     */
    private static final String UNSUPPORTED_OPERATION = "不支持的操作";

    protected AbstractClassifierObject(Object raw, EObjectClassification classification) {
        this.raw = raw;
        this.classification = classification;
    }

    /**
     * 判断被包装的对象是否已经被分类
     *
     * @return 是否已经被分类
     */
    @Override
    public boolean classified() {
        return classification != null && classification != EObjectClassification.UNKNOWN;
    }

    @Override
    public String toText() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public Number toNumber() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public List<?> toList() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public Map<?, ?> toMap() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public Object toObject() {
        return raw;
    }
}
