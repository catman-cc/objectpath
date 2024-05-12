package cc.catman.object.positive;

import cc.catman.object.BaseTest;
import cc.catman.object.cases.basic.Book;
import cc.catman.object.Cases;
import cc.catman.object.cases.basic.StoreHolder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 标准的jsonpath测试,将会测试所有的jsonpath表达式
 */
@SuppressWarnings("java:S2699")
public class StandardTest extends BaseTest {
    /**
     * 店铺
     */
    private final StoreHolder ROOT= Cases.storeHolder();

    /**
     * 读取根节点,期望返回整个store
     */
    @Test
    public void readRoot() {
        Object root = IObjectPath.parse("$").eval(ROOT);
        assert root.equals(ROOT);
    }

    /**
     * 使用 .+属性名 读取属性,期望返回属性值
     */
    @Test
    public void readByDotAndName() {
        Object root = IObjectPath.parse("$.store").eval(ROOT);
        assert root.equals(ROOT.getStore());
    }

    /**
     * 使用 [index] 读取数组元素,期望返回数组元素
     */
    @Test
    public void readByIndex() {
        Object root = IObjectPath.parse("$.store.book[0]").eval(ROOT);
        assert root.equals(ROOT.getStore().getBook().get(0));
    }

    /**
     * 使用 [*] 读取数组所有元素,期望返回数组
     */
    @Test
    public void readAllFromArray() {
        Object root = IObjectPath.parse("$.store.book[*]").eval(ROOT);
        assert root.equals(ROOT.getStore().getBook());
    }

    /**
     * 使用[name]获取子对象,期望返回子对象
     */
    @Test
    public void readByName() {
        Object root = IObjectPath.parse("$.store.book[0].author").eval(ROOT);
        assert root.equals(ROOT.getStore().getBook().get(0).getAuthor());
    }

    /**
     * 使用[start:end]获取数组切片,期望返回切片
     */
    @Test
    public void readBySlice() {
        Object root = IObjectPath.parse("$.store.book[0:2]").eval(ROOT);
        assert root.equals(ROOT.getStore().getBook().subList(0,2));
    }

    /**
     * 使用$.store.book[*].author 获取所有作者,期望返回所有作者
     */
    @Test
    public void readAllAuthors() {
        Object root = IObjectPath.parse("$.store.book[*].author").eval(ROOT);
        List<String> want = ROOT.getStore().getBook().stream()
                .map(Book::getAuthor)
                .collect(Collectors.toList());
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == want.size();
        for (int i = 0; i < want.size(); i++) {
            assert want.get(i).equals(list.get(i));
        }
    }



    /**
     * $.store.book[?(@.price < 10)]	查找价格小于10的书,期望返回价格小于10的书
     */
    @Test
    public void readPriceLessThan10() {
        Object root = IObjectPath.parse("$.store.book[?(@.price < 10)]").eval(ROOT);
        List<Book> want = ROOT.getStore().getBook().stream()
                .filter(book -> book.getPrice() < 10)
                .collect(Collectors.toList());
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == want.size();
        for (int i = 0; i < want.size(); i++) {
            assert want.get(i).equals(list.get(i));
        }
    }

    /**
     * $.store.book[?(@.price <= $['expensive'])]
     * 查找价格小于等于expensive的书,期望返回价格小于等于expensive的书
     * 注意这里expensive是被单引号包裹的,这里在测试访问变量的同时,也在测试是否支持单引号包裹的变量
     */
    @Test
    public void readPriceLessThanExpensive() {
        // 注意这里没有用到.号
        Stream.of("expensive", "'expensive'","\"expensive\"").forEach(expensive -> {
            Object root = IObjectPath.parse("$.store.book[?(@.price <= $" + expensive + ")]").eval(ROOT);
            List<Book> want = ROOT.getStore().getBook().stream()
                    .filter(book -> book.getPrice() <= ROOT.getExpensive())
                    .collect(Collectors.toList());
            assert root instanceof List;
            List<?> list = (List<?>) root;
            assert list.size() == want.size();
            for (int i = 0; i < want.size(); i++) {
                assert want.get(i).equals(list.get(i));
            }
        });
        // 测试包裹在中括号中的变量
        Stream.of("expensive", "'expensive'","\"expensive\"").forEach(expensive -> {
            Object root = IObjectPath.parse("$.store.book[?(@.price <= $[" + expensive + "])]").eval(ROOT);
            List<Book> want = ROOT.getStore().getBook().stream()
                    .filter(book -> book.getPrice() <= ROOT.getExpensive())
                    .collect(Collectors.toList());
            assert root instanceof List;
            List<?> list = (List<?>) root;
            assert list.size() == want.size();
            for (int i = 0; i < want.size(); i++) {
                assert want.get(i).equals(list.get(i));
            }
        });

        // 测试添加了.号的变量
        Stream.of("expensive", "'expensive'","\"expensive\"").forEach(expensive -> {
            Object root = IObjectPath.parse("$.store.book[?(@.price <= $." + expensive + ")]").eval(ROOT);
            List<Book> want = ROOT.getStore().getBook().stream()
                    .filter(book -> book.getPrice() <= ROOT.getExpensive())
                    .collect(Collectors.toList());
            assert root instanceof List;
            List<?> list = (List<?>) root;
            assert list.size() == want.size();
            for (int i = 0; i < want.size(); i++) {
                assert want.get(i).equals(list.get(i));
            }
        });
    }

    /**
     * $.store.book[?(@.author =~ /.*杜/i)]
     * 目前正则修饰符只支持i,g,m
     * 查找作者名字中包含杜的书,期望返回作者名字中包含杜的书
     */
    @Test
    public void readAuthorRRegx() {
        Object root = IObjectPath.parse("$.store.book[?(@.author =~ /.*杜/i)]").eval(ROOT);
        List<Book> want = ROOT.getStore().getBook().stream()
                .filter(book -> book.getAuthor().matches(".*杜.*"))
                .collect(Collectors.toList());
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == want.size();
        for (int i = 0; i < want.size(); i++) {
            assert want.get(i).equals(list.get(i));
        }
    }

    /**
     * $.store.book[?( /.*杜/i ~= @.author)],注意这里左边是正则表达式,右边是属性
     * 目前正则修饰符只支持i,g,m
     * 查找作者名字中包含杜的书,期望返回作者名字中包含杜的书
     */
    @Test
    public void readAuthorLRegx() {
        Object root = IObjectPath.parse("$.store.book[?( /.*杜/i ~= @.author)]").eval(ROOT);
        List<Book> want = ROOT.getStore().getBook().stream()
                .filter(book -> book.getAuthor().matches(".*杜.*"))
                .collect(Collectors.toList());
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == want.size();
        for (int i = 0; i < want.size(); i++) {
            assert want.get(i).equals(list.get(i));
        }
    }

    /**
     * 使用$..author 递归下降获取所有作者,期望返回所有作者
     */
    @Test
    public void readAllAuthorsRecursively() {
        Object root = IObjectPath.parse("$.store..author").eval(ROOT);
        List<String> want = ROOT.getStore().getBook().stream()
                .map(Book::getAuthor)
                .collect(Collectors.toList());
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == want.size();
        for (int i = 0; i < want.size(); i++) {
            assert want.get(i).equals(list.get(i));
        }
    }

    /**
     * $.store.* 读取所有子对象,期望返回所有子对象,
     * 查看其他语言的实现,这一步是直接忽略了store对象中的属性名称,将内部所有数据存放到一个数组中返回.
     * 暂时想不到实际应用场景,所以先不实现
     */
    @Test
    public void readAllChildren() {
        Object root = IObjectPath.parse("$.*").eval(ROOT);
        List<Object> want = Arrays.asList(ROOT.getStore(), ROOT.getExpensive());
        assert root instanceof List;
        List<?> list = (List<?>) root;
        assert list.size() == want.size();
        for (int i = 0; i < want.size(); i++) {
            assert want.get(i).equals(list.get(i));
        }
    }

}
