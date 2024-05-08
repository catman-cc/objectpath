package cc.catman.object.core.classifier.array;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.UnknownClassifierObject;
import cc.catman.object.core.classifier.ClassifierObject;
/**
 * 数组对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class ArrayObjectClassifier implements ObjectClassifier {
    @Override
    public ClassifierObject classify(Object object) {
        if (object.getClass().isArray()){
            return new ArrayClassifierObject(object);
        }
        if (object instanceof Object[]) {
            return new ArrayClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
