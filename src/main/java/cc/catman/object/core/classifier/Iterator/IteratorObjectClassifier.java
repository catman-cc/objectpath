package cc.catman.object.core.classifier.Iterator;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.UnknownClassifierObject;
import cc.catman.object.core.classifier.ClassifierObject;

import java.util.Iterator;
/**
 * 迭代器对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class IteratorObjectClassifier implements ObjectClassifier {
    @Override
    public ClassifierObject classify(Object object) {
        if (object instanceof Iterator) {
            return new IteratorClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
