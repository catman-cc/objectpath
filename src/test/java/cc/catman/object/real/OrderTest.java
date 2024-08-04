package cc.catman.object.real;

import cc.catman.object.BaseTest;
import cc.catman.object.ObjectPath;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderItem;
import cc.catman.object.cases.Order.OrderMock;
import cc.catman.object.core.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("java:S5976")
public class OrderTest extends BaseTest {
    @Test
    public void testSelectorChild() {
        Order order = OrderMock.mockOrder();
        List<String> exprs = Arrays.asList(
                "$.items",
                "$[items]",
                "$['items']",
                "$[\"items\"]"
        );
        List<OrderItem> want = order.getItems();
        exprs.forEach(e -> {
            ObjectPathAccessor as = objectPath.parse(e);
            Object res = as.eval(order);
            Assert.assertEquals(want, res);
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSelectorConvertToMap() {
        Order order = OrderMock.mockOrder();
        String expr = "$.items[*][name,price,quantity,costPrice]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(ReflectionHelper.isAssignableFrom(List.class, res.getClass()));
        List<Map<String, Object>> parsed = ((List<Map<String, Object>>) res);
        List<Map<String, Object>> want = new ArrayList<>();
        order.getItems().forEach((oi -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", oi.getName());
            map.put("price", oi.getPrice());
            map.put("quantity", oi.getQuantity());
            map.put("costPrice", oi.getCostPrice());
            want.add(map);
        }));
        for (int i = 0; i < parsed.size(); i++) {
            Assert.assertEquals(want.get(i), parsed.get(i));
        }
    }

    @Test
    public void selectorForIndex() {
        Order order = OrderMock.mockOrder();
        List<OrderItem> items = order.getItems();
        String template = "$.items[%d]";
        for (int i = 0; i < items.size(); i++) {
            String expr = String.format(template, i);
            ObjectPathAccessor as = objectPath.parse(expr);
            Object res = as.eval(order);
            Assert.assertEquals(items.get(i), res);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForWildcardAll() {
        List<Order> order = IntStream.range(0, 10)
                .mapToObj(i -> OrderMock.mockOrderWithFixedSize(10))
                .collect(Collectors.toList());
        String expr = "$..price";
        ObjectPathAccessor as = ObjectPath.analyzer(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(100, list.size());
        Assert.assertTrue(list.stream().allMatch(o -> o instanceof Integer));
        List<Integer> prices = order.stream().flatMap(o -> o.getItems().stream())
                .flatMap(i -> Stream.of(i.getPrice())).collect(Collectors.toList());
        Assert.assertEquals(prices, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForSlice() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        String expr = "$.items[0:5]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(5, list.size());
        Assert.assertEquals(order.getItems().subList(0, 5), list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForSlicePick() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        String expr = "$.items[0,1,5,7,9]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(5, list.size());
        Assert.assertEquals(order.getItems().get(0), list.get(0));
        Assert.assertEquals(order.getItems().get(1), list.get(1));
        Assert.assertEquals(order.getItems().get(5), list.get(2));
        Assert.assertEquals(order.getItems().get(7), list.get(3));
        Assert.assertEquals(order.getItems().get(9), list.get(4));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompare() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        String expr = "$.items[?(@.price>100)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> oi.getPrice() > 100).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareEQ() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(10086);
        order.getItems().add(item);
        String expr = "$.items[?(@.price==10086)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() == 10086)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareNotEQ() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(10086);
        order.getItems().add(item);
        String expr = "$.items[?(@.price!=10086)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() != 10086)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareLT() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(199);
        order.getItems().add(item);
        String expr = "$.items[?(@.price<200)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() < 200)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareLE() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(200);
        order.getItems().add(item);
        String expr = "$.items[?(@.price<=200)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() <= 200)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareGT() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(200);
        order.getItems().add(item);
        String expr = "$.items[?(@.price>199)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() > 199)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareGE() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(200);
        order.getItems().add(item);
        String expr = "$.items[?(@.price>=200)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() >= 200)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareIsNull() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName(null);
        order.getItems().add(item);
        String expr = "$.items[?(@.name is null)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (Objects.isNull(oi.getName()))).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareLRegex() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(/j\\w+/ ~= @.name)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (Pattern.compile("j\\w+").matcher(oi.getName()).find())).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareRRegex() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(@.name =~ /j\\w+/)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (Pattern.compile("j\\w+").matcher(oi.getName()).find())).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareBetween() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setPrice(200);
        order.getItems().add(item);
        String expr = "$.items[?(@.price.between(100,200))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() >= 100 && oi.getPrice() <= 200)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareLike() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(@.name like '%pan%')]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> oi.getName().contains("pand")).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareLeftLike() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(@.name like '%panda')]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> oi.getName().endsWith("panda")).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareRightLike() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(@.name.like('jpa%'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> oi.getName().startsWith("jpa")).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareIn() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(@.name.in('jpanda','apple','banana'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream()
                .filter(oi -> Stream.of("jpanda", "apple", "banana")
                        .anyMatch(name -> name.equals(oi.getName())))
                .collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareNotIn() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = OrderMock.mockItem();
        item.setName("jpanda");
        order.getItems().add(item);
        String expr = "$.items[?(@.name.notIn('jpanda','apple','banana'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream()
                .filter(oi -> Stream.of("jpanda", "apple", "banana")
                        .noneMatch(name -> name.equals(oi.getName())))
                .collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareContains() {
        List<List<String>> nameList=new ArrayList<>();
        List<String> nvs = Arrays.asList("jpanda", "banana", "apple");
        nameList.add(nvs);
        nvs.forEach(n -> {
                    List<String> names = new ArrayList<>();
                    names.add(n);
                    IntStream.range(0, 10).forEach(i -> names.add(n + i));
                    nameList.add(names);
                });


        String expr = "$[?(@.contains('jpanda','apple','banana'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nameList);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(Collections.singletonList(nvs),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareContainsForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.contains('j','p','a'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(Collections.singletonList("jpanda"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareStartWithForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.startWith('j'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(Collections.singletonList("jpanda"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareEndWithForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.endWith('a'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(2,list.size());
        Assert.assertEquals(Arrays.asList("jpanda","banana"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareMatchForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.match('^jp.*$'))]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(Collections.singletonList("jpanda"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareAndForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.match('^jp.*$') and @.startWith('j') and @.endWith('a') and @.contains('a') )]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(Collections.singletonList("jpanda"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareAndSymbolForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.match('^jp.*$') && @.startWith('j') && @.endWith('a') && @.contains('a') )]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(Collections.singletonList("jpanda"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareOrForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.match('^jp.*$') or @.startWith('b') or @.endWith('a') or @.contains('an') )]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(2,list.size());
        Assert.assertEquals(Arrays.asList("jpanda","banana"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareOrSymbolForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?(@.match(/^jp.*$/) || @.startWith('b') || @.endWith('a') || @.contains('an') )]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(2,list.size());
        Assert.assertEquals(Arrays.asList("jpanda","banana"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterCompareOrGroupForString() {
        List<String> nvs = Arrays.asList("jpanda", "apple", "banana");
        String expr = "$[?((@.match('^jp.*$') || @.startWith('b') || @.endWith('a') )|| @.contains('a') )]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(nvs);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        Assert.assertEquals(3,list.size());
        Assert.assertEquals(nvs,res);
        // apple not match
        expr = "$[?((@.match('^jp.*$') || @.startWith('b') || @.endWith('a') )|| @.contains('b') )]";
        ObjectPathAccessor as2 = objectPath.parse(expr);
        res = as2.eval(nvs);
        Assert.assertTrue(res instanceof List);
        list = (List<Object>) res;
        Assert.assertEquals(2,list.size());
        Assert.assertEquals(Arrays.asList("jpanda","banana"),res);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void selectorForFilterNotCompare() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        String expr = "$.items[?(!@.price>100)]";
        ObjectPathAccessor as = objectPath.parse(expr);
        Object res = as.eval(order);
        Assert.assertTrue(res instanceof List);
        List<Object> list = (List<Object>) res;
        List<OrderItem> want = order.getItems().stream().filter(oi -> !(oi.getPrice() > 100)).collect(Collectors.toList());
        Assert.assertEquals(want, list);
    }

    @Test
    public void funcForNotNull(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        Arrays.asList(
                "$.notNull()"
                ,"$.notNull(@.items)"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            Boolean res = as.eval(order,Boolean.class);
            Assert.assertTrue(res);
        });
    }

    @Test
    public void funcForIsNull(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        order.setId(null);
        Arrays.asList(
                "$.id.isNull()"
                ,"$.isNull(@.id)"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            Boolean res = as.eval(order,Boolean.class);
            Assert.assertTrue(res);
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForFilter(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = order.getItems().get(3);
        item.setId(null);
        String expr = "$.items.filter(@.id.isNull())";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<Object> res = as.eval(order,List.class);
        Assert.assertEquals(1,res.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForAnd(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        OrderItem item = order.getItems().get(3);
        item.setId(null);
        item.setPrice(100);
        OrderItem item2 = order.getItems().get(5);
        item2.setId(null);
        item2.setPrice(101);
        String expr = "$.items.filter(@.and(@.id.isNull(),@.price>100))";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<Object> res = as.eval(order,List.class);
        Assert.assertEquals(1,res.size());
    }

    @Test
    public void funcForMin(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        Integer min = order.getItems().stream().min(Comparator.comparing(OrderItem::getPrice))
                .map(OrderItem::getPrice)
                .orElse(Integer.MIN_VALUE);
        String expr = "$.items.min(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        OrderItem res = as.eval(order,OrderItem.class);
        Assert.assertEquals(min,Integer.valueOf(res.getPrice()));
        expr = "$.items[*].price|@.min()";
        as = objectPath.parse(expr);
        Integer res2 = as.eval(order,Integer.class);
        Assert.assertEquals(min,res2);
    }

    @Test
    public void funcForMax(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        Integer min = order.getItems().stream().max(Comparator.comparing(OrderItem::getPrice))
                .map(OrderItem::getPrice)
                .orElse(Integer.MAX_VALUE);
        String expr = "$.items.max(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        OrderItem res = as.eval(order,OrderItem.class);
        Assert.assertEquals(min,Integer.valueOf(res.getPrice()));
        expr = "$.items[*].price|@.max()";
        as = objectPath.parse(expr);
        Integer res2 = as.eval(order,Integer.class);
        Assert.assertEquals(min,res2);
    }

    @Test
    public void funcForSum(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        Integer sum= 0;
        for (OrderItem item : order.getItems()) {
            sum+=item.getPrice();
        }

        String expr = "$.items.sum(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        Integer res = as.eval(order,Integer.class);
        Assert.assertEquals(sum,res);
        expr = "$.items[*].price|@.sum()";
        as = objectPath.parse(expr);
        Integer res2 = as.eval(order,Integer.class);
        Assert.assertEquals(sum,res2);
    }

    @Test
    public void funcForAvg(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        int sum= 0;
        for (OrderItem item : order.getItems()) {
            sum+=item.getPrice();
        }
        Integer avg=sum/order.getItems().size();

        String expr = "$.items.avg(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        Integer res = as.eval(order,Integer.class);
        Assert.assertEquals(avg,res);
        expr = "$.items[*].price|@.avg()";
        as = objectPath.parse(expr);
        Integer res2 = as.eval(order,Integer.class);
        Assert.assertEquals(avg,res2);
    }

    @Test
    public void funcForSize(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        int size = order.getItems().size();
        String expr = "$.items.size()";
        ObjectPathAccessor as = objectPath.parse(expr);
        int res = as.eval(order,Integer.class);
        Assert.assertEquals(size,res);
    }

    @Test
    public void funcForCount(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        int size = order.getItems().size();
        String expr = "$.items.count()";
        ObjectPathAccessor as = objectPath.parse(expr);
        int res = as.eval(order,Integer.class);
        Assert.assertEquals(size,res);
    }

    @Test
    public void funcForIsEmpty(){
        Order order = OrderMock.mockOrderWithFixedSize(0);
        String expr = "$.items.isEmpty()";
        ObjectPathAccessor as = objectPath.parse(expr);
        Boolean res = as.eval(order,Boolean.class);
        Assert.assertEquals(order.getItems().isEmpty(),res);
        order.setItems(null);
        Assert.assertEquals(true,as.eval(order,Boolean.class));
    }

    @Test
    public void funcForIndex(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.items[3].index()";
        ObjectPathAccessor as = objectPath.parse(expr);
        Assert.assertEquals(Integer.valueOf(3),as.eval(order,int.class));
    }

    @Test
    public void funcForLength(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.items.length()";
        ObjectPathAccessor as = objectPath.parse(expr);
        Assert.assertEquals(Integer.valueOf(5),as.eval(order,int.class));
        Assert.assertEquals(Integer.valueOf(5),objectPath.parse("$.length()").eval("hello",int.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForKeys(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.keys()";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<String> keys = as.eval(order, List.class);
        Assert.assertEquals(Arrays.asList("id","items"),keys);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForValues(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.values()";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<Object> values = as.eval(order, List.class);
        Assert.assertEquals(Arrays.asList(order.getId(),order.getItems()),values);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForRevert(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.items.reverse()";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<OrderItem> values = as.eval(order, List.class);
        List<OrderItem> revert=new ArrayList<>(order.getItems());
        Collections.reverse(revert);
        Assert.assertEquals(revert,values);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForDistinct(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        for (int i = 0; i < order.getItems().size(); i++) {
            order.getItems().get(i).setPrice(i>5?2:1);
        }
        // 查找价格最低的订单项
        String expr = "$.items.distinct(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<Object> values = as.eval(order, List.class);
        Assert.assertEquals(Arrays.asList(order.getItems().get(0),order.getItems().get(6)),values);
        expr = "$.items[*].price|@.distinct()";
        as = objectPath.parse(expr);
        values=as.eval(order, List.class);
        Assert.assertEquals(Arrays.asList(1,2),values);
        expr = "$.items[*].price|@.dis()";
        as = objectPath.parse(expr);
        values=as.eval(order, List.class);
        Assert.assertEquals(Arrays.asList(1,2),values);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForSort(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.items.sort(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<OrderItem> values = as.eval(order, List.class);
        Assert.assertEquals(
                order.getItems().stream()
                        .sorted(Comparator.comparing(OrderItem::getPrice))
                        .collect(Collectors.toList())
                , values
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForRSort(){
        Order order = OrderMock.mockOrderWithFixedSize(5);
        String expr = "$.items.rsort(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<OrderItem> values = as.eval(order, List.class);
        List<OrderItem> collect = order.getItems().stream()
                .sorted(Comparator.comparing(OrderItem::getPrice))
                .collect(Collectors.toList());
        Collections.reverse(collect);
        Assert.assertEquals(collect, values);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForGroupBy(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        for (int i = 0; i < order.getItems().size(); i++) {
            order.getItems().get(i).setPrice(i>5?2:1);
        }
        // 查找价格最低的订单项
        String expr = "$.items.groupBy(@.price)";
        ObjectPathAccessor as = objectPath.parse(expr);
        Map<Integer,List<OrderItem>> values = as.eval(order, Map.class);
        Assert.assertEquals(2,values.size());
        Map<Integer, List<OrderItem>> collect = order.getItems().stream().collect(Collectors.groupingBy(OrderItem::getPrice));
        Assert.assertEquals(collect,values);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForJoin(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        Order order2= OrderMock.mockOrderWithFixedSize(5);
        Map<String,Order> root=new HashMap<>();
        root.put("order",order);
        root.put("order2",order2);

        for (int i = 0; i < order.getItems().size(); i++) {
            order.getItems().get(i).setPrice(i>5?2:1);
        }
        String expr = "$.order.items.join($.order2.items)";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<OrderItem>  value= as.eval(root, List.class);
        Assert.assertEquals(15,value.size());
        List<OrderItem> ois=new ArrayList<>(order.getItems());
        ois.addAll(order2.getItems());
        for (int i = 0; i < ois.size(); i++) {
            Assert.assertEquals(ois.get(i),value.get(i));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void funcForSplit(){
        String expr="$.split('\\.')";
        ObjectPathAccessor as = objectPath.parse(expr);
        List<String> res = as.eval("hello.world", List.class);
        Assert.assertEquals(2,res.size());
    }

    @Test
    public void funcForReplace(){
        String expr="$.replace('a','o')";
        ObjectPathAccessor as = objectPath.parse(expr);
        String res = as.eval("hella,warld", String.class);
        Assert.assertEquals("hello,world",res);
    }

    @Test
    public void funcForSubstring(){
        Stream.of(
                "$.substring(0,5)",
                "$.substring(0+1-1,$.length()-6)"
                , "$.sub(0,5)",
                "$.sub(0+1-1,$.length()-6)"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval("hello,world", String.class);
            Assert.assertEquals("hello",res);
        });
    }

    @Test
    public void funcToUpper(){
        Stream.of(
                "$.toUpper()"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval("hello,world", String.class);
            Assert.assertEquals("HELLO,WORLD",res);
        });
    }

    @Test
    public void funcToLower(){
        Stream.of(
                "$.toLower()"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval("HELLO,WORLD", String.class);
            Assert.assertEquals("hello,world",res);
        });
    }

    @Test
    public void funcTrim(){
        Stream.of(
                "$.trim()"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval(" hello,world     ", String.class);
            Assert.assertEquals("hello,world",res);
        });
    }

    @Test
    public void funcLTrim(){
        Stream.of(
                "$.ltrim()"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval(" hello,world     ", String.class);
            Assert.assertEquals("hello,world     ",res);
        });
    }

    @Test
    public void funcRTrim(){
        Stream.of(
                "$.rtrim()"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval(" hello,world     ", String.class);
            Assert.assertEquals(" hello,world",res);
        });
    }

    @Test
    public void funcConcat(){
        Stream.of(
                "$.concat(1)",
                "$.concat('1')",
                "$+1",
                "$+'1'"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            String res = as.eval("hello,world", String.class);
            Assert.assertEquals("hello,world1",res);
        });
    }


    @Test
    public void funcFirst() {
        Order order = OrderMock.mockOrder();
        List<String> exprs = Collections.singletonList(
                "$.items.first()"
        );
        List<OrderItem> want = order.getItems();
        exprs.forEach(e -> {
            ObjectPathAccessor as = objectPath.parse(e);
            Object res = as.eval(order);
            Assert.assertEquals(want.get(0), res);
        });
    }

    @Test
    public void funcLast() {
        Order order = OrderMock.mockOrder();
        List<String> exprs = Collections.singletonList(
                "$.items.last()"
        );
        List<OrderItem> want = order.getItems();
        exprs.forEach(e -> {
            ObjectPathAccessor as = objectPath.parse(e);
            Object res = as.eval(order);
            Assert.assertEquals(want.get(order.getItems().size()-1), res);
        });
    }

    @Test
    public void funcIndexOf() {
        Order order = OrderMock.mockOrder();
        List<String> exprs = Collections.singletonList(
                "$.items.indexOf(3)"
        );
        List<OrderItem> want = order.getItems();
        exprs.forEach(e -> {
            ObjectPathAccessor as = objectPath.parse(e);
            Object res = as.eval(order);
            Assert.assertEquals(want.get(3), res);
        });
    }

    @Test
    public void funcMap() {
        Order order = OrderMock.mockOrderWithFixedSize(10);
        List<String> exprs = Collections.singletonList(
                "$.items[*].map((I)@.price-@costPrice)"
        );
        exprs.forEach(e -> {
            ObjectPathAccessor as = objectPath.parse(e);
            Object res = as.eval(order);
            Assert.assertEquals(order.getItems()
                            .stream().map(i-> i.getPrice() - i.getCostPrice())
                            .collect(Collectors.toList())
                    , res);
        });
    }

    @Test
    public void funcToMap(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        ObjectPathAccessor as = objectPath.parse("$.items[*].toMap()");
        Object res = as.eval(order);
        Assert.assertTrue(ReflectionHelper.isAssignableFrom(List.class,res.getClass()));
        List<?> ls = (List<?>) res;
        Assert.assertEquals(10,ls.size());
        Assert.assertTrue(ReflectionHelper.isAssignableFrom(Map.class,ls.get(0).getClass()));
    }

    @Test
    public void funcForNot(){
        Order order = OrderMock.mockOrderWithFixedSize(10);
        order.setId(null);
        Arrays.asList(
                "$.id.notNull().not()"
                ,"$.notNull(@.id).not()"
        ).forEach(expr->{
            ObjectPathAccessor as = objectPath.parse(expr);
            Boolean res = as.eval(order,Boolean.class);
            Assert.assertTrue(res);
        });
    }

@Test
public void  funcForRaw(){
    Order order = OrderMock.mockOrderWithFixedSize(10);
    String expr="$.items.raw('size')";
    ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
    cfg.setAllowExecuteRawMethod(true);
    ObjectPathAccessor as = ObjectPath.parse(expr,cfg);
    Integer res = as.eval(order,Integer.class);
    Assert.assertEquals(10, (int) res);
}

    @Test
    @SuppressWarnings("unchecked")
    public void findProfitMoreThan2() {
        Order order = OrderMock.mockOrder();
        ObjectPathAccessor as = objectPath.parse("$.items[?((@.price-@.costPrice) * @quantity >2 )]");
        Object eval = as.eval(order);
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() - oi.getCostPrice()) * oi.getQuantity() > 2).collect(Collectors.toList());
        Assert.assertTrue(eval instanceof List);
        List<OrderItem> find = (List<OrderItem>) eval;
        Assert.assertEquals(want.size(), find.size());
        for (int i = 0; i < want.size(); i++) {
            Assert.assertEquals(want.get(i), find.get(i));
        }
    }

    @Test
    public void findProfitMoreThan2Count() {
        Order order = OrderMock.mockOrder();
        ObjectPathAccessor as = objectPath.parse("$.items[?((@price-@costPrice) * @quantity >2 )].count()");
        Object eval = as.eval(order);
        long want = order.getItems().stream().filter(oi -> (oi.getPrice() - oi.getCostPrice()) * oi.getQuantity() > 2).count();
        Assert.assertTrue(eval instanceof Integer);
        Integer count = (Integer) eval;
        Assert.assertEquals(want, count.intValue());
    }


}
