package cc.catman.object.core.classifier.array;

import cc.catman.object.core.classifier.AbstractCollectionClassifierObject;

import java.util.*;

/**
 * 数组
 * @author jpanda
 * @since 0.0.1
 */
public class ArrayClassifierObject extends AbstractCollectionClassifierObject {
    public ArrayClassifierObject(Object raw) {
        super(raw);
    }

    @Override
    public List<?> toList() {
        Object[] array = ((Object[]) this.raw);
        return Arrays.asList(array);
    }

}
