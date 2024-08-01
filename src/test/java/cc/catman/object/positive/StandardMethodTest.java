package cc.catman.object.positive;

import cc.catman.object.BaseTest;
import cc.catman.object.cases.basic.Book;
import cc.catman.object.Cases;
import cc.catman.object.cases.basic.StoreHolder;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标准方法测试
 */
@SuppressWarnings("java:S2699")
public class StandardMethodTest extends BaseTest {
    /**
     * 店铺
     */
    private final StoreHolder ROOT = Cases.storeHolder();


    @Test
    public void min() {
        Object root = objectPath.parse("$.store.book.min(@.price)").eval(ROOT);
        Assert.assertTrue(root instanceof Book);
        ROOT.getStore().getBook().stream().min(Comparator.comparingDouble(Book::getPrice)).ifPresent(o -> Assert.assertEquals(o, root));
    }

    @Test
    public void minWithNoArg(){
        Object root = objectPath.parse("$.store.book.min(@.price).price").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        ROOT.getStore().getBook().stream().map(Book::getPrice).min(Double::compareTo).ifPresent(o -> Assert.assertEquals(o, root));
    }

    @Test
    public void max() {
        // 获取所有书籍中,价格最大的书籍
        Object root = objectPath.parse("$.store.book.max(@.price)").eval(ROOT);
        Assert.assertTrue(root instanceof Book);
        ROOT.getStore().getBook().stream().max(Comparator.comparingDouble(Book::getPrice)).ifPresent(o -> Assert.assertEquals(o, root));
    }

    @Test
    public void maxWithChild(){
        // 获取所有书籍中,价格最大的书籍
        Object root = objectPath.parse("$.store.book.max(@.price).price").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        ROOT.getStore().getBook().stream().max(Comparator.comparingDouble(Book::getPrice)).ifPresent(o -> Assert.assertEquals(o.getPrice(), root));
    }

    @Test
    public void sum() {
        Object root = objectPath.parse("$.store.book.sum(@.price)").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        ROOT.getStore().getBook().stream().map(Book::getPrice).reduce(Double::sum).ifPresent(o -> Assert.assertEquals(o, root));
    }

    @Test
    public void sumWithPipe() {
        Object root = objectPath.parse("$.store.book[*].price|$.sum()").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        ROOT.getStore().getBook().stream().map(Book::getPrice).reduce(Double::sum).ifPresent(o -> Assert.assertEquals(o, root));
    }

    @Test
    public void avg() {
        Object root = objectPath.parse("$.store.book.avg(@.price)").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        ROOT.getStore().getBook().stream().map(Book::getPrice).reduce(Double::sum)
                .ifPresent(o -> Assert.assertEquals(o / ROOT.getStore().getBook().size(), root)
                );
    }

    @Test
    public void size() {
        Object root = objectPath.parse("$.store.book.size()").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        Assert.assertEquals(ROOT.getStore().getBook().size(), root);
    }

    @Test
    public void isEmpty() {
        Object root = objectPath.parse("$.store.book.isEmpty()").eval(ROOT);
        Assert.assertTrue(root instanceof Boolean);
        Assert.assertEquals(ROOT.getStore().getBook().isEmpty(), root);
    }

    @Test
    public void length() {
        Object root = objectPath.parse("$.store.book.length()").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        Assert.assertEquals(ROOT.getStore().getBook().size(), root);
    }

    @Test
    public void strLength() {
        Object root = objectPath.parse("$.store.book[0].title.length()").eval(ROOT);
        Assert.assertTrue(root instanceof Number);
        Assert.assertEquals(ROOT.getStore().getBook().get(0).getTitle().length(), root);
    }

    @Test
    public void keys() {
        Object root = objectPath.parse("$.store.book.keys()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(i, list.get(i));
        }
    }

    @Test
    public void values() {
        Object root = objectPath.parse("$.store.book.values()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(ROOT.getStore().getBook().get(i), list.get(i));
        }
    }

    @Test
    public void reverse() {
        Object root = objectPath.parse("$.store.book.reverse()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        List<?> want = ROOT.getStore().getBook();
        Collections.reverse(want);
        assert list.size() == ROOT.getStore().getBook().size();
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(want.get(i), list.get(i));
        }
    }

    @Test
    public void sort() {
        Object root = objectPath.parse("$.store.book.sort(@.price,@.title)").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        List<Book> want = ROOT.getStore().getBook().stream().sorted((o1, o2) -> {
            if (o1.getPrice() != o2.getPrice()) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
            return o1.getTitle().compareTo(o2.getTitle());
        }).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(want.get(i), list.get(i));
        }
    }

    @Test
    public void rsort() {
        Object root = objectPath.parse("$.store.book.rsort(@.price,@.title)").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        List<Book> want = ROOT.getStore().getBook().stream().sorted((o1, o2) -> {
            if (o1.getPrice() != o2.getPrice()) {
                return Double.compare(o2.getPrice(), o1.getPrice());
            }
            return o2.getTitle().compareTo(o1.getTitle());
        }).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(want.get(i), list.get(i));
        }
    }

    @Test
    public void reverseSort() {
        Object root = objectPath.parse("$.store.book.sort(@.price,@.title).reverse()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        List<Book> want = ROOT.getStore().getBook().stream().sorted((o1, o2) -> {
            if (o1.getPrice() != o2.getPrice()) {
                return Double.compare(o2.getPrice(), o1.getPrice());
            }
            return o2.getTitle().compareTo(o1.getTitle());
        }).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            assert list.get(i).equals(want.get(i));
        }
    }

    @Test
    public void groupBy() {
        Object root = objectPath.parse("$.store.book.groupBy(@.category)").eval(ROOT);
        assert root instanceof Map;
    }

    @Test
    public void join() {
        Object root = objectPath.parse("$.store.book[*].price|@.join(10086D)").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size() + 1;
    }

    @Test
    public void split() {
        Object root = objectPath.parse("$.store.book[35].author.split('·')").eval(ROOT);
        assert root instanceof List;
    }

    @Test
    public void distinct() {
        Object root = objectPath.parse("$.store.book.distinct()").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().stream().distinct().count();
    }

    @Test
    public void distinctWithFilter() {
        Object root = objectPath.parse("$.store.book.distinct(@.category)").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().stream().map(Book::getCategory).distinct().count();
    }

    @Test
    public void testForMap(){
        Object root = objectPath.parse("$.store.book[*].map(@.category + '-' + @.index() )").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        List<Book> books = ROOT.getStore().getBook();
        List<String> rewirte=new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            rewirte.add(book.getCategory()+"-"+i);
        }
        assert list.size() == rewirte.size();
        Assert.assertEquals(list,rewirte);
    }

}
