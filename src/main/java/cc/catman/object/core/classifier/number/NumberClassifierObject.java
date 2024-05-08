package cc.catman.object.core.classifier.number;

import cc.catman.object.core.classifier.EObjectClassification;
import cc.catman.object.core.classifier.AbstractClassifierObject;

/**
 * 数值对象
 * @author jpanda
 * @since 0.0.1
 */
public class NumberClassifierObject extends AbstractClassifierObject {
    public NumberClassifierObject(Object raw) {
        super(raw, EObjectClassification.NUMBER);
    }

    @Override
    public Number toNumber() {
        return ((Number) raw);
    }

    @Override
    public String toText() {
        return this.toNumber().toString();
    }
}
