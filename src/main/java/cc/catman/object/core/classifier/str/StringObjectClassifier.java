package cc.catman.object.core.classifier.str;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.UnknownClassifierObject;
import cc.catman.object.core.classifier.ClassifierObject;
/**
 * 字符串对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class StringObjectClassifier implements ObjectClassifier{
    @Override
    public ClassifierObject classify(Object object) {
        if (object instanceof String) {
            return new StringClassifierObject(object);
        }
        return UnknownClassifierObject.create(object);
    }
}
