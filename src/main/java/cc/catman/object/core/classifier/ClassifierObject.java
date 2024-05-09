package cc.catman.object.core.classifier;

import java.util.List;
import java.util.Map;

/**
 * 被分类的对象
 * @author jpanda
 * @since 0.0.1
 */
public interface ClassifierObject {
    /**
     * 获取对象的分类
     * @return 对象分类
     */
    EObjectClassification getClassification();

    /**
     * 判断对象是否被分类
     * @return 是否被分类
     */
    boolean classified();

    default boolean isNull(){
        return getClassification() == EObjectClassification.NULL||getRaw()==null;
    }
    /**
     * 获取原始对象
     * @return 原始对象
     */
    Object getRaw();
    /**
     * 将对象转换为字符串
     * @return 字符串
     */
    String toText();
    /**
     * 将对象转换为数字
     * @return 数字
     */
    Number toNumber();
    /**
     * 将对象转换为列表
     * @return 列表
     */
    @SuppressWarnings({"java:S1452"})
    List<?> toList();
    /**
     * 将对象转换为Map
     * @return Map
     */
    Object toObject();
    /**
     * 将对象转换为Map
     * @return Map
     */
    @SuppressWarnings({"java:S1452"})
    Map<?, ?> toMap();
}
