package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.accessor.property.wrapper.PropertyWrapperFactory;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * 抽象,公共,属性访问器
 * @author jpanda
 * @since 0.0.3
 */
public abstract class AbstractPropertyAccessor implements PropertyAccessor {

    /**
     * 当前属性对应的索引或者名称,如果是顶层属性,该值为null
     */
    protected Object indexOrName;

    protected ObjectPathConfiguration configuration;

    protected PropertyAccessorFactory propertyAccessorFactory;

    protected PropertyWrapperFactory propertyWrapperFactory;

    /**
     * 缓存子属性访问器,以便后续快速访问
     * 是否缓存子属性访问器,取决于具体的对象是否需要缓存子属性访问器
     */
    protected Map<Object,PropertyAccessor> childAccessors=new WeakHashMap<>();

    protected AbstractPropertyAccessor(ObjectPathConfiguration configuration) {
        this.configuration = configuration;
        this.propertyAccessorFactory=configuration.getPropertyAccessorFactory();
        this.propertyWrapperFactory= configuration.getWrapperFactory();
    }

    protected AbstractPropertyAccessor( ObjectPathConfiguration configuration,Object indexOrName) {
        this.configuration = configuration;
        this.indexOrName = indexOrName;
        this.propertyAccessorFactory=configuration.getPropertyAccessorFactory();
        this.propertyWrapperFactory= configuration.getWrapperFactory();
    }

    /**
     * 访问子属性/子元素
     *
     * @param belong      所属的属性包装器
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @return 子属性或者子元素的属性包装器
     */
    @SuppressWarnings("unchecked")
    @Override
    public PropertyWrapper get(PropertyWrapper belong, Object indexOrName) {
        // 尝试从指定的对象中获取当前对象,并进一步获取当前对象的子属性包装器
        PropertyWrapper current = isRoot()?
                belong
                : belong.read(this.indexOrName);
        // 然后获取子属性的包装器
        return current.read(indexOrName);
    }

    @Override
    public Class<?> getType(PropertyWrapper belong,Object indexOrName){
        PropertyWrapper current = this.get(belong, indexOrName);
        return current.readType();
    }


    @Override
    public Object get(PropertyWrapper belong) {
        Object bo = belong.read();
        if (Objects.isNull(bo)){
            return null;
        }
        return this.get(bo);
    }

    /**
     * 访问子属性/子元素
     *
     * @param object      对象
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @return 子属性或者子元素的属性包装器
     */
    @Override
    public PropertyWrapper get(Object object, Object indexOrName) {
        // 如果需要向下访问,则需要先创建子属性包装器
        return this.get(propertyWrapperFactory.create(object),indexOrName);
    }



    @Override
    public PropertyAccessor createChildAccessor(Object indexOrName) {
        return new UnfinishedPropertyAccessor(configuration,indexOrName);
    }


    /**
     * 创建子属性访问器
     *
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param belong      所属的对象
     * @return 子属性访问器
     */
    @Override
    public PropertyAccessor createChildAccessor(Object indexOrName, Object belong) {
        return this.propertyAccessorFactory.create(belong, indexOrName);
    }

    /**
     * 创建子属性访问器
     *
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param belong      所属的对象类型
     * @return 子属性访问器
     */
    @Override
    public PropertyAccessor createChildAccessor(Object indexOrName, Class<?> belong) {
        return this.propertyAccessorFactory.create(belong, indexOrName);
    }

    /**
     * 设置子属性
     *
     * @param object      对象
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param value       属性值
     * @return 设置后的属性值
     */
    @Override
    public Object set(Object object, Object indexOrName, Object value) {
        PropertyAccessor ca =  configuration
                .getPropertyAccessorCache()
                .getOrSet(object.getClass(),indexOrName,()-> this.propertyAccessorFactory.create(object, indexOrName));
        ca.set(object,value);
        return value;
    }

    protected boolean isRoot(){
        return Objects.isNull(this.indexOrName);
    }
}
