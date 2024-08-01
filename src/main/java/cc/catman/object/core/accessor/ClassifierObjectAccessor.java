package cc.catman.object.core.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.Entity;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.cache.ICache;
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
    private ICache<ObjectAccessor> cache;
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
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        super.injectConfiguration(configuration);
        this.cache=configuration.getObjectAccessorCache();
        this.objectClassifier.injectConfiguration(configuration);
        this.accessors.values().forEach(l-> l.forEach(a->a.injectConfiguration(configuration)));
    }

    @Override
    public boolean isSupport(PropertyWrapper object, Object key, EAccessorKind kind) {
        List<ObjectAccessor> as = findAccessors(object);
        return as.stream().anyMatch(accessor -> accessor.isSupport(object, key, kind));

    }


    private List<ObjectAccessor> findAccessors(PropertyWrapper pw) {
        Object object = pw.read();
        // 对对象进行分类
        ClassifierObject classify = this.objectClassifier.classify(object);
        // 获取该分类下的访问器
        return this.accessors.getOrDefault(classify.getClassification(),new ArrayList<>());
    }

    @Override
    public PropertyWrapper get(PropertyWrapper object, Object key) {
       return findAccessionWithCache(object,key,null,findAccessors(object))
                .map(accessor -> accessor.get(object, key)).orElse(null);
    }

    @Override
    public void eachKey(PropertyWrapper object, Consumer<Object> consumer) {
        findAccessionWithCache(object,EAccessorKind.EACH_KEY,findAccessors(object))
                .ifPresent(a-> a.eachKey(object,consumer));
    }

    @Override
    public void eachValue(PropertyWrapper object, Consumer<Object> consumer) {
        findAccessionWithCache(object,EAccessorKind.EACH_VALUE,findAccessors(object))
                .ifPresent(a-> a.eachValue(object,consumer));
    }

    @Override
    public void eachEntry(PropertyWrapper object, Consumer<Entity> consumer) {
         findAccessionWithCache(object,EAccessorKind.EACH_ENTRY,findAccessors(object))
                 .ifPresent(a-> a.eachEntry(object,consumer));

    }

    @Override
    public PropertyWrapper filter(PropertyWrapper object,  Predicate<Object> filter) {
        return findAccessionWithCache(object,EAccessorKind.FILTER,findAccessors(object))
                .map(a->a.filter(object,filter))
                .orElse(null);
    }

    @Override
    public PropertyWrapper map(PropertyWrapper object, Function<Object, Object> mapper) {
        return findAccessionWithCache(object,EAccessorKind.MAP,findAccessors(object))
                .map(a->a.map(object,mapper))
                .orElse(null);
    }

    @Override
    public int size(PropertyWrapper object) {
        return findAccessionWithCache(object,null,findAccessors(object))
                .map(a->a.size(object))
                .orElse(-1);
    }

    @Override
    public List<Object> covertToList(PropertyWrapper object) {
        return findAccessionWithCache(object,EAccessorKind.COVERT_TO_LIST,findAccessors(object))
                .map(a->a.covertToList(object))
                .orElse(null);
    }

    @Override
    public String covertToString(PropertyWrapper object) {
        return findAccessionWithCache(object,EAccessorKind.COVERT_TO_STRING,findAccessors(object))
                .map(a->a.covertToString(object))
                .orElse(null);
    }

    @Override
    public Number covertToNumber(PropertyWrapper object) {
        return findAccessionWithCache(object,EAccessorKind.COVERT_TO_NUMBER,findAccessors(object))
                .map(a->a.covertToNumber(object))
                .orElse(null);
    }
    protected Optional<ObjectAccessor> findAccessionWithCache(PropertyWrapper object,EAccessorKind kind,List<ObjectAccessor> accessors){
       return findAccessionWithCache(object,null,kind,accessors);
    }

    protected Optional<ObjectAccessor> findAccessionWithCache(PropertyWrapper object,Object key, EAccessorKind kind, List<ObjectAccessor> accessors) {

        if (!this.configuration.isEnableObjectAccessorCache()){
            for (ObjectAccessor accessor:accessors){
                if (accessor.isSupport(object,key,kind)){
                    return Optional.of(accessor);
                }
            }
            return Optional.empty();
        }

        Class<?> oType = object.readType();
        Optional<ObjectAccessor> oaOpt = cache.get(oType, key);
        if (oaOpt.isPresent()){
            return oaOpt;
        }

        for (ObjectAccessor accessor : accessors) {
            if (accessor.isSupport(object,key,kind)) {
                cache.put(oType, key, accessor);
                return Optional.of(accessor);
            }
        }
        return Optional.empty();
    }
}
