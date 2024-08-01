package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.NotNull;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.instance.InstanceFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * 一个抽象的属性包装器,提供一些公共的抽象方法和属性定义
 *
 * @author jpanda
 * @since 0.0.3
 */
public class DefaultPropertyWrapper<I, T> extends AbstractPropertyWrapper<I,T> {

    /**
     * 属性值实例化工厂,用于实例化属性值
     */
    protected InstanceFactory instanceFactory;

    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , T object
    ) {
        super(configuration, object);
        this.instanceFactory = configuration.getInstanceFactory();
        this.postConstruct();
    }


    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , PropertyWrapper belong
            , I indexOrKey
    ) {
       this(configuration, belong, indexOrKey, null);
    }

    public DefaultPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , PropertyWrapper belong
            , I indexOrKey
            , PropertyAccessor accessor
    ) {
        super(configuration,belong,indexOrKey,accessor);
        this.instanceFactory = configuration.getInstanceFactory();
        this.postConstruct();
    }


    @Override
    public boolean readOnly() {
        return false;
    }

    /**
     * 是否允许设置属性值
     */
    @Override
    public boolean canWrite() {
        return Objects.nonNull(this.belong);
    }

    /**
     * 读取被包装对象的原始属性值
     *
     * @return 原始对象的属性值
     */
    @Override
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
