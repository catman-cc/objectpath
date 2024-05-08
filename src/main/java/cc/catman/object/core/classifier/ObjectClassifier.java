package cc.catman.object.core.classifier;

/**
 * 对象分类器,用于对对象进行分类,比如,判断一个对象是字符串,数值,集合,MAP,标准对象等等
 * @author jpanda
 * @since 0.0.1
 */
public interface ObjectClassifier {

    /**
     * 对对象进行分类
     * @param object 对象
     * @return 对象分类
     */
    ClassifierObject classify(Object object);
}
