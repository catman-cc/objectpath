package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;

import java.util.Optional;

/**
 * 未完成的属性访问器
 * 有些时候,我们需要实现一个属性访问器,但是在创建属性访问器的时候,因为各种各样的原因.无法完成
 * 比如,因为泛型擦除导致无法获取到属性的类型,或者因为属性的类型是接口,无法确定具体的实现类
 * 这时候,就需要有个未完成的属性访问器,来表示这个属性的访问器还没有完成
 * 其完成时机在调用其get,set方法时,传入了具体的类型信息时.
 * @author jpanda
 * @since 0.0.3
 */
public class UnfinishedPropertyAccessor extends AbstractPropertyAccessor{

    private PropertyAccessor realAccessor;

    public UnfinishedPropertyAccessor(ObjectPathConfiguration configuration, Object indexOrName) {
        super(configuration);
        this.indexOrName = indexOrName;
    }

    /**
     * 获取属性值
     *
     * @param object 对象
     * @return 属性值
     */
    @Override
    public Object get(Object object) {
        if (!Optional.ofNullable(this.realAccessor).isPresent()){
            // 尝试初始化真正的属性访问器
            this.realAccessor = this.propertyAccessorFactory.create(object, indexOrName);
        }
        return this.realAccessor.get(object);
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
        if (!Optional.ofNullable(this.realAccessor).isPresent()){
            // 尝试初始化真正的属性访问器
            this.realAccessor = this.propertyAccessorFactory.create(belong, indexOrName);
        }
        return this.realAccessor.get(belong,indexOrName);
    }

    @Override
    public Class<?> getType(PropertyWrapper belong, Object indexOrName) {
        if (!Optional.ofNullable(this.realAccessor).isPresent()){
            // 尝试初始化真正的属性访问器
            this.realAccessor = this.propertyAccessorFactory.create(belong, indexOrName);
        }
        return this.realAccessor.getType(belong, indexOrName);
    }

    /**
         * 创建子属性访问器
         *
         * @param indexOrName 子属性或者子元素的名称或者索引
         * @return 子属性访问器
         */
    @Override
    public PropertyAccessor createChildAccessor(Object indexOrName) {
        return new UnfinishedPropertyAccessor(configuration,indexOrName);
    }

    /**
     * 创建一个子属性访问器
     * @param belong  所属对象
     * @param indexOrName 子属性主键
     * @return 子属性访问器
     */
    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return null;
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
        if (!Optional.ofNullable(this.realAccessor).isPresent()){
            // 尝试初始化真正的属性访问器
            this.realAccessor = this.propertyAccessorFactory.create(object, indexOrName);
        }
        return this.realAccessor.get(object, indexOrName);
    }

    /**
     * 设置属性值
     *
     * @param object 对象
     * @param value  属性值
     * @return 设置后的属性值
     */
    @Override
    public Object set(Object object, Object value) {
        if (!Optional.ofNullable(this.realAccessor).isPresent()){
            // 尝试初始化真正的属性访问器
            this.realAccessor = this.propertyAccessorFactory.create(object, indexOrName);
        }
        return this.realAccessor.set(object,value);
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
    public Object set(PropertyWrapper object, Object indexOrName, Object value) {
        return null;
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
        if (!Optional.ofNullable(this.realAccessor).isPresent()){
            // 尝试初始化真正的属性访问器
            this.realAccessor = this.propertyAccessorFactory.create(object, indexOrName);
        }
        return this.realAccessor.set(object,indexOrName,value);
    }
}
