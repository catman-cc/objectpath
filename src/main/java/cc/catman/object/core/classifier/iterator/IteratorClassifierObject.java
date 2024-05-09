package cc.catman.object.core.classifier.iterator;

import cc.catman.object.core.classifier.AbstractCollectionClassifierObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 迭代器分类对象
 * @author jpanda
 * @since 0.0.1
 */
public class IteratorClassifierObject extends AbstractCollectionClassifierObject {
    public IteratorClassifierObject(Object raw) {
        super(raw);
    }

    @Override
    public List<?> toList() {
        Iterator<?> iterator = (Iterator<?>) raw;
        List<Object> list=new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
