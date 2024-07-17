package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.core.ConfigurationAccessor;
import cc.catman.object.core.Nullable;
import cc.catman.object.core.accessor.property.PropertyWrapper;

/**
 * 属性包装器创建器,用于将一个普通对象转换为属性包装器
 * @author jpanda
 * @since 0.0.2
 */
public interface PropertyWrapperCreator extends ConfigurationAccessor {

    /**
     * 评分,用于判断是否适合创建属性包装器,如果返回一个小于等于0的值,则不适合创建属性包装器
     * 否则,返回的值越大,越适合创建属性包装器,最终在创建时,会选择得分最高的创建器创建属性包装器
     * @param type 属性类型
     * @param belong 所属对象
     * @param key 属性名称或索引
     * @param object 被包装的对象
     * @return 评分
     */
    int score(Class<?> type
            , @Nullable PropertyWrapper belong
            , @Nullable Object key
            ,@Nullable Object object
    );

    int score(Class<?> type,Object indexOrKeyName);

    int score(Object object,Object indexOrKeyName);

    int score(PropertyWrapper belong,Object indexOrKeyName);

    /**
     * 创建属性包装器
     * @param object 被包装的对象
     * @return 属性包装器
     */
    default PropertyWrapper create(Object object){
        return create(object, null, null);
    }

    /**
     * 创建属性包装器
     * @param belong 所属对象
     * @param key 属性名称或索引
     * @return 属性包装器
     */
   default PropertyWrapper create(PropertyWrapper belong,Object key){
        return create(null, key, belong);
   }

    /**
     * 创建属性包装器
     * @param object 被包装的对象
     * @param key 属性名称或索引
     * @param belong 所属对象
     * @return 属性包装器
     */
    PropertyWrapper create(Object object, Object key, PropertyWrapper belong);
}
