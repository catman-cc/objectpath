package cc.catman.object.core.classifier.collection;

import cc.catman.object.core.classifier.AbstractCollectionClassifierObject;

import java.util.*;

/**
 * 集合对象
 * @author jpanda
 * @since 0.0.1
 */
public class CollectionClassifierObject extends AbstractCollectionClassifierObject {

    public CollectionClassifierObject(Object raw) {
        super(raw);
    }

    @Override
    public List<?> toList() {
        Collection<?> c= (Collection<?>) raw;
        if (c instanceof List) {
            return (List<?>) c;
        }
        return new ArrayList<>(c);
    }
}
