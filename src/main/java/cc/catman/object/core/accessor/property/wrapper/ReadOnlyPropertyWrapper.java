package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.NotNull;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;

/**
 * 只读属性包装器,用于包装只读属性
 * @param <I> 索引类型
 * @param <T> 所属对象类型
 * @author jpanda
 * @since 0.0.3
 */
public class ReadOnlyPropertyWrapper<I,T> extends AbstractPropertyWrapper<I,T> {

    public ReadOnlyPropertyWrapper( ObjectPathConfiguration configuration,T object) {
        super(configuration, object);
        this.postConstruct();

    }
    public ReadOnlyPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , PropertyWrapper belong
            , I indexOrKey
    ) {
      this(configuration, belong, indexOrKey, null);
    }

    public ReadOnlyPropertyWrapper(
            @NotNull ObjectPathConfiguration configuration
            , PropertyWrapper belong
            , I indexOrKey
            , PropertyAccessor accessor
    ) {
        super(configuration, belong, indexOrKey, accessor);
        this.postConstruct();
    }

    @Override
    public boolean readOnly() {
        return true;
    }

    /**
     * 是否允许设置属性值
     */
    @Override
    public boolean canWrite() {
        return false;
    }

    /**
     * 获取属性值
     */
    @Override
    public Object read() {
        return this.object;
    }


    /**
     * 更新属性值
     *
     * @param value 新的属性值
     * @return 是否更新成功
     */
    @Override
    public boolean set(Object value) {
        return false;
    }

    /**
     * 初始化属性所属的对象
     *
     * @return 是否初始化成功
     */
    @Override
    public boolean init() {
        return false;
    }

    @Override
    public boolean instance() {
        return false;
    }


    @Override
    public PropertyWrapper wrapper(Object object) {
        return this.configuration.getWrapperFactory().createReadOnly(object);
    }
}
