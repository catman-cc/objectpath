package cc.catman.object.core.classifier.map;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.ClassifierObject;
import cc.catman.object.core.classifier.UnknownClassifierObject;

import java.util.Map;

/**
 * Map对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class MapObjectClassifier implements ObjectClassifier {

    @Override
    public ClassifierObject classify(Object object) {
        if (object instanceof Map) {
            return new MapClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
