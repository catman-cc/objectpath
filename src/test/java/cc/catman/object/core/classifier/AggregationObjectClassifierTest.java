package cc.catman.object.core.classifier;

import org.junit.Test;

/**
 * 聚合对象分类器测试类
 *
 * @author jpanda
 * @since 0.0.1
 */
public class AggregationObjectClassifierTest {

    @Test
    public void testArray() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        Object[] array = new Object[]{1, 2, 3};
        ClassifierObject co = oc.classify(array);
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.COLLECTION);
        assert co.toList().size() == 3;
    }

    @Test
    public void testCollection() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new java.util.HashSet<>());
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.COLLECTION);
    }

    @Test
    public void testIterable() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new java.util.ArrayList<>());
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.COLLECTION);
    }

    @Test
    public void testMap() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new java.util.HashMap<>());
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.MAP);
    }

    @Test
    public void testNull() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(null);
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.NULL);
    }

    @Test
    public void testNumber() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(1);
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.NUMBER);
    }

    @Test
    public void testString() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify("test");
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.STRING);
    }

    @Test
    public void testObject(){
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new Object());
        assert co.classified();
        assert co.getClassification().equals(EObjectClassification.STANDARD_OBJECT);
    }
}