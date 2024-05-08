package cc.catman.object.core.classifier.collection;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.UnknownClassifierObject;
import cc.catman.object.core.classifier.ClassifierObject;

import java.util.Collection;
/**
 * 集合对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class CollectionObjectClassifier implements ObjectClassifier {

    @Override
    public ClassifierObject classify(Object object) {
        if (object instanceof Collection) {
            return new CollectionClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
