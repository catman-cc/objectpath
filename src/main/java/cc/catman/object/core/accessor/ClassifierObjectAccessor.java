package cc.catman.object.core.accessor;

import cc.catman.object.core.Entity;
import cc.catman.object.core.classifier.AggregationObjectClassifier;
import cc.catman.object.core.classifier.ClassifierObject;
import cc.catman.object.core.classifier.EObjectClassification;
import cc.catman.object.core.classifier.ObjectClassifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
/**
 * 分类对象访问器
 * 通过分类器对对象进行分类,然后根据分类结果获取对应的访问器
 * @author jpanda
 * @since 0.0.1
 */
public class ClassifierObjectAccessor extends AbstractObjectAccessor {
    /**
     * 对象分类器
     */
    private final ObjectClassifier objectClassifier;
    /**
     * 分类访问器
     */
    private final Map<EObjectClassification,List<ObjectAccessor>> accessors;

    public static ClassifierObjectAccessor defaultAccessor(){
        // 获取默认的分类器
        ObjectClassifier oc= AggregationObjectClassifier.defaultClassifier();
        ClassifierObjectAccessor classifierObjectAccessor= new ClassifierObjectAccessor(oc);
        // 添加默认的访问器
        classifierObjectAccessor.add(EObjectClassification.STRING, BasicObjectAccessor.defaultAccessor());
        classifierObjectAccessor.add(EObjectClassification.NUMBER,BasicObjectAccessor.defaultAccessor());

        classifierObjectAccessor.add(EObjectClassification.COLLECTION,new ArrayObjectAccessor());
        classifierObjectAccessor.add(EObjectClassification.MAP,new MapObjectAccessor());

        classifierObjectAccessor.add(EObjectClassification.STANDARD_OBJECT,new DefaultClassObjectAccessor());
        // null 和unknown 没有访问器
        return classifierObjectAccessor;
    }

    public ClassifierObjectAccessor(ObjectClassifier objectClassifier) {
        this(objectClassifier, new EnumMap<>(EObjectClassification.class));
    }

    public ClassifierObjectAccessor(ObjectClassifier objectClassifier, Map<EObjectClassification, List<ObjectAccessor>> accessors) {
        this.objectClassifier = objectClassifier;
        this.accessors = accessors;
    }

    public ClassifierObjectAccessor add(EObjectClassification classification, ObjectAccessor accessor){
        this.accessors.computeIfAbsent(classification, k -> new ArrayList<>()).add(accessor);
        return this;
    }

    @Override
    public boolean isSupport(Object object, Object key, EAccessorKind kind) {
        List<ObjectAccessor> as = findAccessors(object);
        return as.stream().anyMatch(accessor -> accessor.isSupport(object, key, kind));
    }


    private List<ObjectAccessor> findAccessors(Object object) {
        // 对对象进行分类
        ClassifierObject classify = this.objectClassifier.classify(object);
        // 获取该分类下的访问器
        return this.accessors.getOrDefault(classify.getClassification(),new ArrayList<>());
    }

    @Override
    public Object get(Object object, Object key) {
        return this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, key)).findFirst()
                .map(accessor -> accessor.get(object, key)).orElse(null);
    }

    @Override
    public void eachKey(Object object, Consumer<Object> consumer) {
        this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_KEY))
                .findFirst().ifPresent(accessor -> accessor.eachKey(object, consumer));
    }

    @Override
    public void eachValue(Object object, Consumer<Object> consumer) {
        this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_VALUE))
                .findFirst().ifPresent(accessor -> accessor.eachValue(object, consumer));
    }

    @Override
    public void eachEntry(Object object, Consumer<Entity> consumer) {
        this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_ENTRY))
                .findFirst().ifPresent(accessor -> accessor.eachEntry(object, consumer));
    }

    @Override
    public Object filter(Object object,  Predicate<Object> filter) {
        return this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.FILTER))
                .findFirst().map(accessor -> accessor.filter(object, filter)).orElse(null);
    }

    @Override
    public Object map(Object object, Function<Object, Object> mapper) {
        return this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.MAP))
                .findFirst()
                .map(accessor -> accessor.map(object, mapper))
                .orElse(null);
    }

    @Override
    public int size(Object object) {
        return this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, null))
                .findFirst().map(accessor -> accessor.size(object)).orElse(-1);
    }

    @Override
    public List<Object> covertToList(Object object) {
        return this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.COVERT_TO_LIST))
                .findFirst().map(accessor -> accessor.covertToList(object)).orElse(null);
    }

    @Override
    public String covertToString(Object object) {
        return this.findAccessors(object).stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.COVERT_TO_STRING))
                .findFirst().map(accessor -> accessor.covertToString(object)).orElse(null);
    }
}
