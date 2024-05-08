package cc.catman.object.core.classifier.nil;

import cc.catman.object.core.classifier.EObjectClassification;
import cc.catman.object.core.classifier.AbstractClassifierObject;
/**
 * 空对象分类器对象
 * @author jpanda
 * @since 0.0.1
 */
public class NullClassifierObject extends AbstractClassifierObject {

    public NullClassifierObject(Object raw) {
        super(raw, EObjectClassification.NULL);
    }
}
