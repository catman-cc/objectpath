package cc.catman.object.core.classifier;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.cache.ICache;
import cc.catman.object.core.cache.DefaultICache;
import cc.catman.object.core.classifier.iterator.IteratorObjectClassifier;
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
 *
 * @author jpanda
 * @since 0.0.1
 */
public class AggregationObjectClassifier implements ObjectClassifier {

    private final List<ObjectClassifier> classifiers;

    private ICache<ObjectClassifier> cache;

    private ObjectPathConfiguration configuration;

    public static final String DEFAULT_CACHE_KEY = "";

    public static AggregationObjectClassifier defaultClassifier() {
        List<ObjectClassifier> classifiers = new ArrayList<>();
        classifiers.add(new NullObjectClassifier());
        classifiers.add(new NumberObjectClassifier());
        classifiers.add(new StringObjectClassifier());
        classifiers.add(new ArrayObjectClassifier());
        classifiers.add(new CollectionObjectClassifier());
        classifiers.add(new MapObjectClassifier());
        classifiers.add(new IteratorObjectClassifier());
        classifiers.add(new DefaultObjectClassifier());
        AggregationObjectClassifier aoc = new AggregationObjectClassifier(classifiers);
        aoc.cache = new DefaultICache<>();
        return aoc;
    }

    public AggregationObjectClassifier() {
        this(new ArrayList<>());
    }

    public AggregationObjectClassifier(List<ObjectClassifier> classifiers) {
        this.classifiers = classifiers;
    }

    /**
     * 对对象进行分类
     *
     * @param object 对象
     * @return 对象分类
     */
    @Override
    public ClassifierObject classify(Object object) {
        boolean canUseCache = Objects.nonNull(this.configuration)
                              && this.configuration.isEnableObjectClassifierCache()
                              && Objects.nonNull(object);
        if (canUseCache) {
            ClassifierObject cached = tryReadFromCache(object);
            if (Objects.nonNull(cached)) {
                return cached;
            }
        }

        return this.classifiers.stream()
                .map(classifier -> classifier.classify(object))
                .filter(co -> {
                    if (co == null) {
                        throw new NullPointerException("分类器返回的对象不能为空");
                    }
                    return true;
                })
                .filter(ClassifierObject::classified)
                .findFirst()
                .orElseGet(() -> UnknownClassifierObject.create(object));
    }

    private ClassifierObject tryReadFromCache(Object object) {
        return cache.get(object.getClass(), DEFAULT_CACHE_KEY)
                .map(classifier -> classifier.classify(object))
                .orElseGet(() -> {
                    for (ObjectClassifier classifier : this.classifiers) {
                        ClassifierObject classify = classifier.classify(object);
                        if (classify.classified()) {
                            cache.put(object.getClass(), DEFAULT_CACHE_KEY, classifier);
                            return classify;
                        }
                    }
                    return null;
                });
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration = configuration;
        this.cache = configuration.getObjectClassifierCache();
        this.classifiers.forEach(c -> c.injectConfiguration(configuration));
    }
}
