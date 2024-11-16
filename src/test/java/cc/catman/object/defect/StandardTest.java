package cc.catman.object.defect;

import cc.catman.OP;
import cc.catman.object.BaseTest;
import cc.catman.object.Cases;
import cc.catman.object.cases.basic.Book;
import cc.catman.object.cases.basic.Store;
import cc.catman.object.cases.basic.StoreHolder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

    @Test
    public void useExprFillAnyPart() {
        StoreHolder sh= Cases.storeHolder();
        Store store = sh.getStore();
        Book want = store.getBook().get(0);
        Stream.of(
                "$[book][0]"
                ,"$['b'+'ook'][1-1]"
                ,"$['b'.concat('o')+'ok'][1-1]"
        ).forEach(expr->{
            Book find = OP.parse(expr).eval(store, Book.class);
            Assert.assertEquals(want,find);
        });
    }

    @Test
    public void testSimpleReadFromMap(){
        Map<Object,Object> args=new HashMap<>();
        args.put(0,"name");
        args.put("args0","name");
        args.put("name","name");
        Map<Object,Object> root=new HashMap<>();
        root.put("args",args);
        Object v = OP.parse("$args[0]").eval(root);
        Assert.assertEquals("name",v);
    }

}
