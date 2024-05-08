package cc.catman.object.core.classifier;

import java.util.*;

/**
 * 集合对象
 * @author jpanda
 * @since 0.0.1
 */
public abstract class AbstractCollectionClassifierObject extends AbstractClassifierObject{

    public AbstractCollectionClassifierObject(Object raw) {
        super(raw, EObjectClassification.COLLECTION);
    }


    @Override
    public Map<?, ?> toMap() {
        Map<Integer,Object> map=new LinkedHashMap<>();
        List<?> list=this.toList();
        for (int i = 0; i < list.size(); i++) {
            map.put(i,list.get(i));
        }
        return map;
    }
}
