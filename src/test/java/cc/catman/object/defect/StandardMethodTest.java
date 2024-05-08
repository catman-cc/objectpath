package cc.catman.object.defect;

import cc.catman.object.BaseTest;
import org.junit.Test;

public class StandardMethodTest extends BaseTest {
    @Test
    public void minWhenArrayIsEmpty() {
        Object res = objectPath.parse("$.min()").eval(new int[0]);
        assert res == null;
    }

    @Test
    public void minWhenArrayIsNotEmpty() {
        Object res = objectPath.parse("$.min()").eval(new int[]{1, 2, 3});
        assert res.equals(1);
    }

    @Test
    public void maxWhenArrayIsEmpty() {
        Object res = objectPath.parse("$.max()").eval(new int[]{1,2,3});
        assert 3 == (int) res;
    }

}
