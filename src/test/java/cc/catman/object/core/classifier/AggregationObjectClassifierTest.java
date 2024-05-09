package cc.catman.object.core.classifier;

import org.junit.Assert;
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
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.COLLECTION, co.getClassification());
        Assert.assertEquals(3, co.toList().size());
    }

    @Test
    public void testCollection() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new java.util.HashSet<>());
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.COLLECTION, co.getClassification());
    }

    @Test
    public void testIterable() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new java.util.ArrayList<>());
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.COLLECTION, co.getClassification());
    }

    @Test
    public void testMap() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new java.util.HashMap<>());
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.MAP, co.getClassification());
    }

    @Test
    public void testNull() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(null);
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.NULL, co.getClassification());
    }

    @Test
    public void testNumber() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(1);
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.NUMBER, co.getClassification());
    }

    @Test
    public void testString() {
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify("test");
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.STRING, co.getClassification());
    }

    @Test
    public void testObject(){
        AggregationObjectClassifier oc = AggregationObjectClassifier.defaultClassifier();
        ClassifierObject co = oc.classify(new Object());
        Assert.assertTrue(co.classified());
        Assert.assertEquals(EObjectClassification.STANDARD_OBJECT, co.getClassification());
    }
}