package cc.catman.object.core.classifier;

import cc.catman.object.core.classifier.Iterator.IteratorObjectClassifier;
import cc.catman.object.core.classifier.array.ArrayObjectClassifier;
import cc.catman.object.core.classifier.collection.CollectionObjectClassifier;
import cc.catman.object.core.classifier.map.MapObjectClassifier;
import cc.catman.object.core.classifier.nil.NullObjectClassifier;
import cc.catman.object.core.classifier.number.NumberObjectClassifier;
import cc.catman.object.core.classifier.object.DefaultObjectClassifier;
import cc.catman.object.core.classifier.str.StringObjectClassifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 聚合对象分类器
 * @author jpanda
 * @since 0.0.1
 */
public class AggregationObjectClassifier implements ObjectClassifier{

    private final List<ObjectClassifier> classifiers;

    public static AggregationObjectClassifier defaultClassifier(){
        List<ObjectClassifier> classifiers=new ArrayList<>();
        classifiers.add(new NullObjectClassifier());
        classifiers.add(new NumberObjectClassifier());
        classifiers.add(new StringObjectClassifier());
        classifiers.add(new ArrayObjectClassifier());
        classifiers.add(new CollectionObjectClassifier());
        classifiers.add(new MapObjectClassifier());
        classifiers.add(new IteratorObjectClassifier());
        classifiers.add(new DefaultObjectClassifier());

        return new AggregationObjectClassifier(classifiers);
    }
    public AggregationObjectClassifier() {
        this(new ArrayList<>());
    }

    public AggregationObjectClassifier(List<ObjectClassifier> classifiers) {
        this.classifiers = classifiers;
    }

    /**
     * 对对象进行分类
     * @param object 对象
     * @return 对象分类
     */
    @Override
    public ClassifierObject classify(Object object) {
        return this.classifiers.stream()
                .map(classifier -> classifier.classify(object))
                .peek(co->{
                    if (co == null) {
                        throw new NullPointerException("分类器返回的对象不能为空");
                    }
                })
                .filter(ClassifierObject::classified)
                .findFirst()
                .orElseGet(() -> UnknownClassifierObject.create(object));
    }
}
