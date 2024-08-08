package cc.catman.object.core.accessor.property;

import cc.catman.object.core.Nullable;

import java.util.Optional;

/**
 * 属性包装对象,用于对属性进行包装操作.
 * 通过该包装对象,可以对属性进行读写一致的读写操作
 * 正常情况下,我们除了访问属性本身之外,可能还会访问属性的子属性,比如当属性是一个集合,POJO对象时.
 * @author jpanda
 * @since 0.0.2
 */
public interface PropertyWrapper {
    /**
     * 获取属性所属的对象
     * @return 属性所属的对象
     */
    Optional<PropertyWrapper> getBelong();

    default void setBelong(PropertyWrapper belong){

    }

    /**
     *  对应的值是null
     * @return 是否为null
     */
    boolean isNull();

    /**
     * 属性对应的值不是null
     * @return 是否不为空
     */
   default boolean isNotNull(){
         return !isNull();
   }

    /**
     * 当前上下文是否是只读的
     * @return 当前上下文是否是只读的
     */
   boolean readOnly();

    /**
     * 是否允许设置属性值
     * @return 是否允许设置属性值
     */
    boolean canWrite();

    /**
     * 获取属性值
     * @return 被包装的对象,可能为null
     */
    Object read();

    /**
     * 获取被包装的对象,并将其转换为指定类型
     * @param clazz 期望类型
     * @return 转型后的对象
     * @param <T> 期望类型
     */
    <T> T read(Class<T> clazz);

    /**
     * 获取属性值类型
     * @return 属性值类型
     */
    @Nullable Class<?> readType();

    /**
     * 当属性是一个集合/对象/map时,通过该方法来获取指定位置的值
     * @param indexOrName 索引或名称
     * @return 属性值对应的包装器
     */
    PropertyWrapper read(Object indexOrName);

    /**
     * 获取属性值类型
     * @param indexOrName 索引或名称
     * @return 属性值类型
     */
    Class<?> readType(Object indexOrName);

    /**
     * 更新属性值
     * @param value 新的属性值
     * @return 是否更新成功
     */
    boolean set(Object value);

    /**
     * 当属性是一个集合/对象/map时,通过该方法来修改指定位置的值
     * @param indexOrName 索引或名称
     * @param value 新的属性值
     * @return 是否更新成功
     */
   default boolean set(Object indexOrName,Object value){
       return false;
   }

    /**
     * 初始化属性所属的对象
     * @return 是否初始化成功
     */
   boolean init();

    boolean instance();

    boolean isInstanceOf(Class<?> clazz);

   PropertyWrapper wrapper(Object object);

   Object getIndexOrKey();
    void  setIndexOrKey(Object key);
}
