package cc.catman.object.positive;

import cc.catman.object.BaseTest;
import cc.catman.object.cases.basic.Book;
import cc.catman.object.Cases;
import cc.catman.object.cases.basic.StoreHolder;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标准方法测试
 */
public class StandardMethodTest extends BaseTest {
    /**
     * 店铺
     */
    private final StoreHolder ROOT= Cases.storeHolder();


    @Test
    public void min(){
        Object root = objectPath.parse("$.store.book.min(@.price)").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().stream().map(Book::getPrice).min(Double::compareTo).get());
    }

    @Test
    public void max(){
        Object root = objectPath.parse("$.store.book.max(@.price)").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().stream().map(Book::getPrice).max(Double::compareTo).get());
    }

    @Test
    public void sum(){
        Object root = objectPath.parse("$.store.book.sum(@.price)").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().stream().map(Book::getPrice).reduce(Double::sum).get());
    }

    @Test
    public void avg(){
        Object root = objectPath.parse("$.store.book.avg(@.price)").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().stream().map(Book::getPrice).reduce(Double::sum).get()/ROOT.getStore().getBook().size());
    }

    @Test
    public void size(){
        Object root = objectPath.parse("$.store.book.size()").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().size());
    }

    @Test
    public void isEmpty(){
        Object root = objectPath.parse("$.store.book.isEmpty()").eval(ROOT);
        assert root instanceof Boolean;
        assert root.equals(ROOT.getStore().getBook().isEmpty());
    }

    @Test
    public void length(){
        Object root = objectPath.parse("$.store.book.length()").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().size());
    }

    @Test
    public void strLength(){
        Object root = objectPath.parse("$.store.book[0].title.length()").eval(ROOT);
        assert root instanceof Number;
        assert root.equals(ROOT.getStore().getBook().get(0).getTitle().length());
    }

    @Test
    public void keys(){
        Object root = objectPath.parse("$.store.book.keys()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        for (int i = 0; i < list.size(); i++) {
            assert list.get(i).equals(i);
        }
    }

    @Test
    public void values(){
        Object root = objectPath.parse("$.store.book.values()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        for (int i = 0; i < list.size(); i++) {
            assert list.get(i).equals(ROOT.getStore().getBook().get(i));
        }
    }

    @Test
    public void reverse(){
        Object root = objectPath.parse("$.store.book.reverse()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        List<?> want=ROOT.getStore().getBook();
        Collections.reverse(want);
        assert list.size() == ROOT.getStore().getBook().size();
        for (int i = 0; i < list.size(); i++) {
            assert list.get(i).equals(want.get(i));
        }
    }

    @Test
    public void sort(){
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
            assert list.get(i).equals(want.get(i));
        }
    }

    @Test
    public void rsort(){
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
            assert list.get(i).equals(want.get(i));
        }
    }

    @Test
    public void reverseSort(){
        Object root = objectPath.parse("$.store.book.sort(@.price,@.title).reverse()").eval(ROOT);
        assert root instanceof Iterable;
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size();
        List<Book> want = ROOT.getStore().getBook().stream().sorted(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                if (o1.getPrice() != o2.getPrice()) {
                    return Double.compare(o2.getPrice(), o1.getPrice());
                }
                return o2.getTitle().compareTo(o1.getTitle());
            }
        }).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            assert list.get(i).equals(want.get(i));
        }
    }
    @Test
    public void groupBy(){
        Object root = objectPath.parse("$.store.book.groupBy(@.category)").eval(ROOT);
        assert root instanceof Map;
    }

    @Test
    public void join(){
        Object root = objectPath.parse("$.store.book[*].price|@.join(10086D)").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().size()+1;
    }

    @Test
    public void split(){
        Object root = objectPath.parse("$.store.book[35].author.split('·')").eval(ROOT);
        assert root instanceof List;
    }

    @Test
    public void distinct(){
        Object root = objectPath.parse("$.store.book.distinct()").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().stream().distinct().count();
    }

    @Test
    public void distinctWithFilter(){
        Object root = objectPath.parse("$.store.book.distinct(@.category)").eval(ROOT);
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == ROOT.getStore().getBook().stream().map(Book::getCategory).distinct().count();
    }

}
