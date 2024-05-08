package cc.catman.object.core.classifier.number;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.UnknownClassifierObject;
import cc.catman.object.core.classifier.ClassifierObject;

/**
 * 数字对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class NumberObjectClassifier implements ObjectClassifier {
    @Override
    public ClassifierObject classify(Object object) {
        if (object instanceof Number) {
            return new NumberClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
