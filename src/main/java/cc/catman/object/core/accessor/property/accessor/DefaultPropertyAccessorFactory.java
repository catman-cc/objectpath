package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.cache.ICache;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;

import java.util.*;

/**
 * 默认的属性访问器工厂
 * @author jpanda
 * @since 0.0.3
 */
@SuppressWarnings("java:S1192")
public class DefaultPropertyAccessorFactory implements PropertyAccessorFactory{

    protected ObjectPathConfiguration configuration;

    /**
     * 属性访问器创建器列表
     */
    private final List<PropertyAccessorCreator> creators;

    public static DefaultPropertyAccessorFactory create(){
        return new DefaultPropertyAccessorFactory()
                .add(new UnfinishedPropertyAccessorCreator())
                .add(new ListPropertyAccessorCreator())
                .add(new SetPropertyAccessorCreator())
                .add(new MapPropertyAccessorCreator())
                .add(new CollectionPropertyAccessorCreator())
                .add(new ArrayPropertyAccessorCreator())
                .add(new ObjectPropertyAccessorCreator())
                ;

    }

    public DefaultPropertyAccessorFactory() {
        this.creators=new ArrayList<>();
    }

    /**
     * 创建属性访问器
     *
     * @param type           所属对象的类型
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    @Override
    public PropertyAccessor create(Class<?> type, Object indexOrKeyName) {
        ICache<PropertyAccessor> cache = configuration.getPropertyAccessorCache();
        return cache.getOrSet(type,indexOrKeyName,()->{
            PropertyAccessorCreator pac = this.creators.stream()
                    .parallel()
                    .max(Comparator.comparingInt(a -> a.score(type, indexOrKeyName)))
                    .orElseThrow(() -> new PropertyAccessorRuntimeException(
                            "no property accessor found for type "
                            + type
                            + " and index or key name "
                            + indexOrKeyName
                    ));

            return pac.create(type, indexOrKeyName);
        });
    }

    /**
     * 创建属性访问器
     *
     * @param belong         属性所属的对象
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    @Override
    public PropertyAccessor create(Object belong, Object indexOrKeyName) {
        ICache<PropertyAccessor> cache = configuration.getPropertyAccessorCache();
        return cache.getOrSet(belong.getClass(),indexOrKeyName,()-> {
            PropertyAccessorCreator pac = this.creators.stream()
                    .parallel()
                    .max(Comparator.comparingInt(a -> a.score(belong, indexOrKeyName)))
                    .orElseThrow(() -> new PropertyAccessorRuntimeException(
                            "no property accessor found for object "
                            + belong
                            + " and index or key name "
                            + indexOrKeyName
                    ));

            return pac.create(belong, indexOrKeyName);
        });
    }

    /**
     * 创建属性访问器,尝试从一个属性包装器中来创建属性访问器,
     * 但是属性包装器很可能还没有准备好,因此所创建的属性访问器可能是一个中间状态的
     * @param belong 所属的属性包装器
     * @param indexOrKeyName 属性名称或者索引
     * @return 子属性访问器
     */
    @Override
    public PropertyAccessor create(PropertyWrapper belong, Object indexOrKeyName) {
        ICache<PropertyAccessor> cache = configuration.getPropertyAccessorCache();
        // 不对泛型集合做缓存
        return cache.getOrSet(belong.readType(),indexOrKeyName,()-> {

            Object obj = belong.read();
            if (Objects.nonNull(obj)) {
                return this.create(obj, indexOrKeyName);
            }
            Class<?> type = belong.readType();
            if (Objects.nonNull(type)) {
                return this.create(type, indexOrKeyName);
            }
            PropertyAccessorCreator pac = this.creators.stream()
                    .parallel()
                    .max(Comparator.comparingInt(a -> a.score(belong, indexOrKeyName)))
                    .orElseThrow(() -> new PropertyAccessorRuntimeException(
                            "no property accessor found for object "
                            + belong
                            + " and index or key name "
                            + indexOrKeyName
                    ));

            return pac.create(belong, indexOrKeyName);
        });
    }

    /**
     * 创建一个针对根对象的属性访问器,跟对象的属性访问器是不支持赋值操作的
     *
     * @return 属性访问器
     */
    @Override
    public PropertyAccessor createRoot() {
        return new RootPropertyAccessor(configuration);
    }

    public DefaultPropertyAccessorFactory add(PropertyAccessorCreator creator){
        creator.injectConfiguration(this.configuration);
        this.creators.add(creator);
        return this;
    }

    public DefaultPropertyAccessorFactory remove(PropertyAccessorCreator creator){
        this.creators.remove(creator);
        return this;
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
        this.creators.forEach(c-> c.injectConfiguration(configuration));
    }
}
