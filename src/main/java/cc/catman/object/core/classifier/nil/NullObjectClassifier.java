package cc.catman.object.core.classifier.nil;

import cc.catman.object.core.classifier.ClassifierObject;
import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.UnknownClassifierObject;
/**
 * 空对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class NullObjectClassifier implements ObjectClassifier {
    @Override
    public ClassifierObject classify(Object object) {
        if (object == null) {
            return new NullClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
