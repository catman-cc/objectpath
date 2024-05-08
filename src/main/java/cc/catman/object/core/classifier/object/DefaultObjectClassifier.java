package cc.catman.object.core.classifier.object;

import cc.catman.object.core.classifier.ObjectClassifier;
import cc.catman.object.core.classifier.ClassifierObject;
import cc.catman.object.core.classifier.nil.NullClassifierObject;
import cc.catman.object.core.classifier.nil.NullObjectClassifier;

/**
 * 默认对象分类器,用于对对象进行分类
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultObjectClassifier implements ObjectClassifier {
    /**
     * 对对象进行分类
     * @param object 对象
     * @return 对象分类
     */
    @Override
    public ClassifierObject classify(Object object) {
        if (object == null) {
            return new NullClassifierObject(object);
        }
        return new StandardObjectClassifierObject(object);
    }
}
