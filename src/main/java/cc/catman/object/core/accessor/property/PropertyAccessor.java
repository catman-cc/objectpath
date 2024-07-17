package cc.catman.object.core.accessor.property;

/**
 * 属性访问器
 * @author jpanda
 * @since 0.0.2
 */
public interface PropertyAccessor {

    /**
     * 获取属性值
     * @param object 对象
     * @return 属性值
     */
    Object get(Object object);

    Class<?> getType(PropertyWrapper belong, Object indexOrName);

    Object get(PropertyWrapper object);

    /**
     * 访问子属性/子元素
     * @param object 对象
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @return 子属性或者子元素的属性包装器
     */
    PropertyWrapper get(Object object,Object indexOrName);

    /**
     * 获取子属性的包装器
     * @param belong 所属的对象包装器
     * @param indexOrName 子属性的名称或者索引
     * @return 子属性的属性包装器
     */
    PropertyWrapper get(PropertyWrapper belong,Object indexOrName);
    /**
     * 创建子属性访问器
     *
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @return 子属性访问器
     */
     PropertyAccessor createChildAccessor(Object indexOrName);

    /**
     * 创建子属性访问器
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param belong 所属的对象
     * @return 子属性访问器
     */
    PropertyAccessor createChildAccessor(Object indexOrName,Object belong);

    /**
     * 创建子属性访问器
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param belong 所属的对象类型
     * @return 子属性访问器
     */
    PropertyAccessor createChildAccessor(Object indexOrName,Class<?> belong);

    PropertyAccessor createChildAccessor(PropertyWrapper belong,Object indexOrName);
    /**
     * 获取属性值，如果属性值为null则返回默认值
     * @param object 对象
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    default Object getOrDefault(Object object,Object defaultValue){
        Object value = get(object);
        return value == null? defaultValue : value;
    }

    /**
     * 设置属性值
     * @param object 对象
     * @param value 属性值
     * @return 设置后的属性值
     */
    Object set(Object object,Object value);

    /**
     * 设置子属性
     * @param property 属性包装器
     * @param value 属性值
     * @return 设置后的属性值
     */
    default Object set(PropertyWrapper property,Object value){
        if (property.isNull()) {
            property.init();
        }
        return this.set(property.read(), value);
    }

    /**
     * 设置子属性
     * @param object 对象
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param value 属性值
     * @return 设置后的属性值
     */
    Object set(PropertyWrapper object,Object indexOrName,Object value);

    Object set(Object object,Object indexOrName,Object value);
}
