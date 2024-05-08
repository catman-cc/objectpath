package cc.catman.object.core.classifier;

/**
 * 对象类型,用于对对象进行分类,比如,判断一个对象是字符串,数值,集合,MAP,标准对象等等
 * @author jpanda
 * @since 0.0.1
 */
public enum EObjectClassification {
    STRING,
    NUMBER,
    COLLECTION,
    MAP,
    STANDARD_OBJECT,
    NULL,
    UNKNOWN
}
