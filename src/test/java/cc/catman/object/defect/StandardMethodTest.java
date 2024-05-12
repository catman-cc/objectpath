package cc.catman.object.defect;

import cc.catman.object.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class StandardMethodTest extends BaseTest {
    @Test
    public void minWhenArrayIsEmpty() {
        Object res = IObjectPath.parse("$.min()").eval(new int[0]);
        Assert.assertNull(res);
    }

    @Test
    public void minWhenArrayIsNotEmpty() {
        Object res = IObjectPath.parse("$.min()").eval(new int[]{1, 2, 3});
        Assert.assertEquals(1, res);
    }

    @Test
    public void maxWhenArrayIsEmpty() {
        Object res = IObjectPath.parse("$.max()").eval(new int[]{1, 2, 3});
        Assert.assertEquals(3, res);
    }

}
