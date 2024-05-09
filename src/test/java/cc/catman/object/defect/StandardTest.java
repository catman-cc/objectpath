package cc.catman.object.defect;

import cc.catman.object.BaseTest;
import cc.catman.object.Cases;
import cc.catman.object.cases.basic.StoreHolder;
import org.junit.Test;

@SuppressWarnings("java:S2699")
public class StandardTest extends BaseTest {

    /**
     * 店铺
     */
    private final StoreHolder ROOT= Cases.storeHolder();


    @Test
    public void readNull() {
        Object res = objectPath.parse("$.store.notExist").eval(ROOT);
        assert res == null;
    }

    @Test
    public void readWhenParentNull() {
        Object root = objectPath.parse("$.store.notExist.some").eval(ROOT);
        assert root == null;
        root = objectPath.parse("$.store.notExist[0]").eval(ROOT);
        assert root == null;
        root = objectPath.parse("$.store.book[100]").eval(ROOT);
        assert root == null;
        root = objectPath.parse("$.store.notExist[?(@price<100)]").eval(ROOT);
        assert root == null;
    }

}
