package cc.catman.object.defect;

import cc.catman.object.BaseTest;
import cc.catman.object.Cases;
import cc.catman.object.cases.basic.StoreHolder;
import org.junit.Test;

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
    }

    @Test
    public void readWhenParentIsArray() {
        Object root = objectPath.parse("$.store.notExist[0]").eval(ROOT);
        assert root == null;
    }

    @Test
    public void readWhenParentIsArrayAndIndexNotExist() {
        Object root = objectPath.parse("$.store.book[100]").eval(ROOT);
        assert root == null;
    }

    @Test
    public void readWhenParentIsNullAndFilter() {
        Object root = objectPath.parse("$.store.notExist[?(@price<100)]").eval(ROOT);
        assert root == null;
    }

}
