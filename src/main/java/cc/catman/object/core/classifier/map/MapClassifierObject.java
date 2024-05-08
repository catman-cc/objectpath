package cc.catman.object.core.classifier.map;

import cc.catman.object.core.classifier.EObjectClassification;
import cc.catman.object.core.classifier.AbstractClassifierObject;

import java.util.Map;

/**
 * Map对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class MapClassifierObject extends AbstractClassifierObject {
    public MapClassifierObject(Object raw) {
        super(raw, EObjectClassification.MAP);
    }

    @Override
    public Map<?, ?> toMap() {
        return (Map<?, ?>) raw;
    }
}
