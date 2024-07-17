package cc.catman.object.core.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 数组助手测试
 * @author jpanda
 * @since 0.0.3
 */
public class ArrayHelperTest {

    @Test
    public void get() {
        Assert.assertEquals(1,(int)ArrayHelper.get(new int[]{1,2,3,4,5},0));
        Assert.assertEquals(5,(int)ArrayHelper.get(new int[]{1,2,3,4,5},4));
        Assert.assertNull(ArrayHelper.get(new int[]{1,2,3,4,5},5));
        Assert.assertNull(ArrayHelper.get(new int[]{1,2,3,4,5},-1));
    }

    @Test
    public void set() {
        int[] array = new int[] {1,2,3,4,5};
        Assert.assertTrue(ArrayHelper.set(array,0,6));
        assertEquals(6,array[0]);
    }

    @Test
    public void toArray() {
        int[] intArr={1,2,3,4,5};
        byte[] byteArr={1,2,3,4,5};
        short[] shortArr={1,2,3,4,5};
        long[] longArr={1,2,3,4,5};
        float[] floatArr={1,2,3,4,5};
        double[] doubleArr={1,2,3,4,5};
        char[] charArr={'a','b','c','d','e'};
        boolean[] booleanArr={true,false,true,false,true};
        String[] stringArr={"a","b","c","d","e"};
        Object[] objectArr={1,2,3,4,5};
        Object[] iA = ArrayHelper.toArray(intArr);
        for (int i = 0; i < intArr.length; i++) {
            Assert.assertEquals(intArr[i],iA[i]);
        }

        Object[] bA = ArrayHelper.toArray(byteArr);
        for (int i = 0; i < byteArr.length; i++) {
            Assert.assertEquals(byteArr[i],bA[i]);
        }

        Object[] sA = ArrayHelper.toArray(shortArr);
        for (int i = 0; i < shortArr.length; i++) {
            Assert.assertEquals(shortArr[i],sA[i]);
        }

        Object[] lA = ArrayHelper.toArray(longArr);
        for (int i = 0; i < longArr.length; i++) {
            Assert.assertEquals(longArr[i],lA[i]);
        }

        Object[] fA = ArrayHelper.toArray(floatArr);
        for (int i = 0; i < floatArr.length; i++) {
            Assert.assertEquals(floatArr[i],fA[i]);
        }

        Object[] dA = ArrayHelper.toArray(doubleArr);
        for (int i = 0; i < doubleArr.length; i++) {
            Assert.assertEquals(doubleArr[i],dA[i]);
        }

        Object[] cA = ArrayHelper.toArray(charArr);
        for (int i = 0; i < charArr.length; i++) {
            Assert.assertEquals(charArr[i],cA[i]);
        }

        Object[] bbA = ArrayHelper.toArray(booleanArr);
        for (int i = 0; i < booleanArr.length; i++) {
            Assert.assertEquals(booleanArr[i],bbA[i]);
        }

        Object[] ssA = ArrayHelper.toArray(stringArr);
        for (int i = 0; i < stringArr.length; i++) {
            Assert.assertEquals(stringArr[i],ssA[i]);
        }

        Object[] oA = ArrayHelper.toArray(objectArr);
        for (int i = 0; i < objectArr.length; i++) {
            Assert.assertEquals(objectArr[i],oA[i]);
        }
    }
}