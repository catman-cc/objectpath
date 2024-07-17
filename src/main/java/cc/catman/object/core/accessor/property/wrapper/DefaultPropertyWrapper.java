package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.NotNull;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyAccessorCache;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.accessor.property.accessor.PropertyAccessorFactory;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.instance.InstanceFactory;
import cc.catman.object.core.util.ReflectionHelper;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * 一个抽象的属性包装器,提供一些公共的抽象方法和属性定义
 *
 * @author jpanda
 * @since 0.0.3
 */
public class DefaultPropertyWrapper<I, T> implements PropertyWrapper {
    /**
     * 当前属性对应的索引或者名称,理论上不是Integer就是String类型,当然,也有可能是null.
     */
    @Getter
    protected I indexOrKey;
    /**
     * 当前属性的类型
     */
    protected Class<?> type;

    /**
     * 当前属性的泛型类型定义
     */
    protected Class<?>[] genericTypes;

    protected T object;

    /**
     * 当前属性所属的对象,如果当前属性是顶层属性,那么该属性为null
     */
    protected PropertyWrapper belong;

    /**
     * 当前属性对应的属性访问器
     */
    protected PropertyAccessor accessor;

    /**
     * 属性路径配置
     */
    protected ObjectPathConfiguration configuration;

    /**
     * 属性值实例化工厂,用于实例化属性值
     */
    protected InstanceFactory instanceFactory;

    /**
     * 属性包装器创建工厂
     */
    protected PropertyWrapperFactory wrapperFactory;

    /**
     * 属性访问器缓存
     */
    protected PropertyAccessorCache accessorCache;

    /**
     * 属性访问器工厂
     */
    protected PropertyAccessorFactory accessorFactory;

    protected Map<Object, PropertyWrapper> childWrappers = new WeakHashMap<>();

    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , T object
    ) {
        this.object = object;
        this.configuration = configuration;
        this.instanceFactory = configuration.getInstanceFactory();
        this.wrapperFactory = configuration.getWrapperFactory();
        this.accessorCache = configuration.getPropertyAccessorCache();
        this.accessorFactory = configuration.getPropertyAccessorFactory();

        this.postConstruct();
    }

    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , T object
            , I indexOrKey
    ) {
        this.object = object;
        this.indexOrKey = indexOrKey;
        this.configuration = configuration;
        this.instanceFactory = configuration.getInstanceFactory();
        this.wrapperFactory = configuration.getWrapperFactory();
        this.accessorCache = configuration.getPropertyAccessorCache();
        this.accessorFactory = configuration.getPropertyAccessorFactory();

        this.postConstruct();
    }


    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , PropertyWrapper belong
            , I indexOrKey
    ) {
        this.indexOrKey = indexOrKey;
        this.belong = belong;

        this.configuration = configuration;
        this.instanceFactory = configuration.getInstanceFactory();
        this.wrapperFactory = configuration.getWrapperFactory();
        this.accessorCache = configuration.getPropertyAccessorCache();
        this.accessorFactory = configuration.getPropertyAccessorFactory();
        this.postConstruct();
    }

    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , PropertyWrapper belong
            , I indexOrKey
            , PropertyAccessor accessor
    ) {
        this.indexOrKey = indexOrKey;
        this.belong = belong;
        this.accessor = accessor;

        this.configuration = configuration;
        this.instanceFactory = configuration.getInstanceFactory();
        this.wrapperFactory = configuration.getWrapperFactory();
        this.accessorCache = configuration.getPropertyAccessorCache();
        this.accessorFactory = configuration.getPropertyAccessorFactory();
        this.postConstruct();
    }

    protected void postConstruct() {
        if (Objects.isNull(this.accessor)){
            if (Objects.isNull(this.belong) && Objects.nonNull(this.object)) {
                this.accessor = this.accessorFactory.create(object, null);
            }
        }
       if (Objects.nonNull(this.belong)){
           Optional.ofNullable(this.accessor).ifPresent(a->{
               this.object = (T) a.get(this.belong);
           });
       }
       Optional.ofNullable(this.object).ifPresent(o->{
           this.type=o.getClass();
       });
    }

    /**
     * 判断当前属性是否为null
     *
     * @return 是否为null
     */
    @Override
    public boolean isNull() {
        this.init();
        return object == null;
    }

    /**
     * 是否允许设置属性值
     */
    @Override
    public boolean canWrite() {
        return Objects.nonNull(this.belong);
    }

    /**
     * 获取属性所属的对象
     *
     * @return 属性所属的对象
     */
    @Override
    public Optional<PropertyWrapper> getBelong() {
        return Optional.ofNullable(belong);
    }

    /**
     * 获取被包装对象中的子属性值
     *
     * @param indexOrName 索引或名称
     * @return 属性值对应的包装器
     */
    @Override
    public PropertyWrapper read(Object indexOrName) {
        PropertyWrapper cache = this.childWrappers.get(indexOrName);
        if (Objects.nonNull(cache)) {
            return cache;
        }

        // 当前属性尚未被初始化
        // 考虑为子属性创建一个访问器
        PropertyAccessor ca = this.accessor.createChildAccessor(this, indexOrName);
        // 再将其包装为一个完整的包装器
        cache = this.wrapperFactory.create(this, indexOrName, ca);
        this.childWrappers.put(indexOrName, cache);
        return cache;
    }

    /**
     * 获取属性值类型
     *
     * @param indexOrName 索引或名称
     * @return 属性值类型
     */
    @Override
    public Class<?> readType(Object indexOrName) {
        initBeforeRead();
        PropertyWrapper cpw = this.childWrappers.get(indexOrName);
        if (Objects.isNull(cpw)){
            cpw = accessor.get(this, indexOrName);
            this.childWrappers.put(indexOrName, cpw);
        }
        return cpw.readType();
    }


    /**
     * 读取被包装对象的原始属性值
     *
     * @return 原始对象的属性值
     */
    @Override
    public Object read() {
        if (!this.getBelong().isPresent() && this.isNotNull()) {
                return this.object;
            }

        if (Objects.isNull(this.belong)) {
            return null;
        }
        // 有可能是当前属性尚未被初始化,尝试初始化操作
        // 初始化操作是先判断是否有所属的对象,如果存在所属的对象,则通过当前访问器获取属性值

        initBeforeRead();

        Object belongObj = this.belong.read();
        if (Objects.isNull(belongObj)) {
            return null;
        }

        this.object = (T) this.accessor.get(belongObj);
        return this.object;
    }

    @Override
    @SuppressWarnings({"unchecked","java:S4977"})
    public <T> T read(Class<T> clazz) {
        Object obj = this.read();
        if (Objects.nonNull(obj)){
                return (T)obj;
        }
        return null;
    }

    /**
     * 获取属性值类型
     *
     * @return 属性值类型
     */
    @Override
    public Class<?> readType() {
        if (Objects.nonNull(this.type)) {
            return this.type;
        }
        // 尝试初始化属性的类型,属性的类型需要从所属的对象中获取
        // 读取所属对象,需要初始化当前的访问器,
        if (Objects.nonNull(this.object)) {
            this.type = this.object.getClass();
            return this.type;
        }
        if (Objects.nonNull(this.accessor)) {
            if (Objects.isNull(this.belong)){
                return null;
            }
            Object o = this.accessor.get(this.belong);
            if (Objects.nonNull(o)) {
                this.type = o.getClass();
                return this.type;
            }
            // 无法读取所属对象,只能尝试从其中获取所属对象的类型,再由accessor来获取当前类型
            return this.accessor.getType(this.belong,this.indexOrKey);
        }
        // 尝试从所属对象来获取当前属性的类型
        if (Objects.nonNull(this.belong)) {
            this.type = this.belong.readType(this.indexOrKey);
            return this.type;
        }
        return null;
    }

    /**
     * 尝试读取属性值
     *
     * @param indexOrName 索引或名称
     * @return 属性值对应的包装器
     */
    protected PropertyWrapper doRead(Object indexOrName) {
        return this.accessor.get(this.object, indexOrName);
    }

    /**
     * 为当前属性设置值
     *
     * @param value 新的属性值
     * @return 是否赋值成功
     */
    @Override
    public boolean set(Object value) {
        if (Objects.nonNull(this.belong)){
            this.belong.instance();
        }
        if (Objects.isNull(this.belong)){
            return false;
        }
        return belong.set(this.indexOrKey, value);
    }

    /**
     * 为当前属性的指定位置设置值
     *
     * @param indexOrName 索引或名称
     * @param value       新的属性值
     * @return 是否赋值成功
     */
    @Override
    public boolean set(Object indexOrName, Object value) {
        if (Objects.nonNull(this.belong)){
            this.belong.instance();
        }
        return this.doSet(indexOrName, value);
    }

    /**
     * 尝试更新特定的属性值
     *
     * @param indexOrName 索引或名称
     * @param value       新的属性值
     * @return 是否更新成功
     */
    protected boolean doSet(Object indexOrName, Object value) {
        this.accessor.set(this, indexOrName, value);
        return true;
    }

    /**
     * 初始化属性所属的对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean init() {
        Optional.ofNullable(this.belong).ifPresent(b -> {
            if (b.isNull() && !b.init()) {
                    throw new PropertyAccessorRuntimeException("can not init null property");
                }

        });
        if (this.object==null) {
            if (Objects.nonNull(this.belong)){
                this.object=(T)this.accessor.get(this.belong.read());
            }
            if (Objects.nonNull(this.object)){
                this.type=this.object.getClass();
            }
            if (Objects.isNull(this.type)){
                this.type=this.readType();
            }


        }
        return true;
    }

    @Override
    public boolean instance(){
        this.init();
        if (Objects.isNull(this.object)){
            this.object=(T)this.instanceFactory.create(this.type);
            if (this.getBelong().isPresent()) {
                belong.set(this.indexOrKey, this.object);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInstanceOf(Class<?> clazz) {
        Class<?> ct = this.readType();
        if (Objects.nonNull(ct)){
            return ReflectionHelper.isAssignableFrom(clazz,ct);
        }
        return false;
    }

    /**
     * 包装一个对象为对象包装器
     * @param object 被包装的对象
     * @return 对象包装器
     */
    @Override
    public PropertyWrapper wrapper(Object object) {
        return this.configuration.getWrapperFactory().create(object);
    }

    /**
     * 在读取属性值之前执行的操作
     */
    public void initBeforeRead() {
        // 初始化当前属性的accessor
        initAccessor();
    }

    private void initAccessor() {
        if (Optional.ofNullable(this.accessor).isPresent()) {
            return;
        }
        // 如果accessor为空,尝试创建当前对象的访问器,创建当前访问器需要获取当前属性的类型
        // 为了获取当前属性的type,则需要先初始化所属的对象,随后再获取属性的类型

        // 此时需要分为两步进行处理,首先是当前属性无所属对象,此时要求当前属性对应的对象必须存在
        if (Objects.isNull(this.belong)) {
            // 当前属性无所属对象,这就意味着该属性不支持赋值操作,但支持读取操作
            // 此时创建当前对象的访问器表面上是没有意义的,因为简单的读取操作不需要访问器来获取属性值
            // 当为了统一性,依然会创建一个访问器
            this.accessor = accessorFactory.createRoot();
        }
        // 然后是当前属性有所属的对象,此时需要创建当前对象的访问器
        this.accessor = this.accessorFactory.create(this.belong, this.indexOrKey);
    }

    /**
     * 尝试将indexOrName转换为整数
     *
     * @param indexOrName 索引或名称
     * @return 整数值
     */
    protected Integer tryConvertIndex(Object indexOrName) {
        if (indexOrName instanceof Integer) {
            return (Integer) indexOrName;
        }
        if (indexOrName instanceof Number) {
            Number n = ((Number) indexOrName);
            // 判断是否包含有效浮点数
            String text = n.toString();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                throw new PropertyAccessorRuntimeException("can not convert to index:" + indexOrName, e);
            }
        }

        if (indexOrName instanceof String) {
            try {
                return Integer.parseInt((String) indexOrName);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        throw new PropertyAccessorRuntimeException("can not convert to index:" + indexOrName);
    }
}
