package cc.catman.object.real;

import cc.catman.object.BaseTest;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderItem;
import cc.catman.object.cases.Order.OrderMock;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderTest extends BaseTest {

    @Test
    @SuppressWarnings("unchecked")
    public void findProfitMoreThan2(){
        Order order = OrderMock.mockOrder();
        ObjectPathAccessor as = objectPath.parse("$.items[?((@.price-@.costPrice) * @quantity >2 )]");
        Object eval = as.eval(order);
        List<OrderItem> want = order.getItems().stream().filter(oi -> (oi.getPrice() - oi.getCostPrice()) * oi.getQuantity() > 2).collect(Collectors.toList());
        Assert.assertTrue(eval instanceof List);
        List<OrderItem> find= (List<OrderItem>) eval;
        Assert.assertEquals(want.size(),find.size());
        for (int i = 0; i < want.size(); i++) {
            Assert.assertEquals(want.get(i),find.get(i));
        }
    }

    @Test
    public void findProfitMoreThan2Count(){
        Order order = OrderMock.mockOrder();
        ObjectPathAccessor as = objectPath.parse("$.items[?((@price-@costPrice) * @quantity >2 )].count()");
        Object eval = as.eval(order);
        long want = order.getItems().stream().filter(oi -> (oi.getPrice() - oi.getCostPrice()) * oi.getQuantity() > 2).count();
        Assert.assertTrue(eval instanceof Integer);
        Integer count= (Integer) eval;
        Assert.assertEquals(want,count.intValue());
    }

    public void  simpleBenchMark(){
        int count=100000;
        List<Order> orders= IntStream.range(0,count).mapToObj(i-> OrderMock.mockOrder()).collect(Collectors.toList());

            long baseStart = System.currentTimeMillis();
            orders.forEach(order -> {
                order.getItems().stream().filter(oi -> (oi.getPrice() - oi.getCostPrice()) * oi.getQuantity() > 2).count();
            });

        long baseTime = System.currentTimeMillis() - baseStart;
        System.out.println("base cost:" + baseTime + "ms");

        long fullStart = System.currentTimeMillis();
        orders.forEach(order -> {
            ObjectPathAccessor as = objectPath.parse("$.items[?((@price-@costPrice) * @quantity >2 )].count()");
            as.eval(order);
        });
        long fullTime= System.currentTimeMillis() - fullStart;
        System.out.println("full cost:"+fullTime+"ms");
        System.out.println("full cost is :"+(fullTime*1.0/baseTime)+" times of base");

        long cacheStart = System.currentTimeMillis();
        ObjectPathAccessor as = objectPath.parse("$.items[?((@price-@costPrice) * @quantity >2 )].count()");
        orders.forEach(as::eval);
        long cacheTime = System.currentTimeMillis()-cacheStart;
        System.out.println("cache cost:"+cacheTime+"ms");
        System.out.println("cache cost is: "+(cacheTime*1.0/baseTime)+" times of base");
    }

    @SneakyThrows
    public void reflectBenchmark(){
        List<Order> orders= IntStream.range(0,100000).mapToObj(i-> OrderMock.mockOrder()).collect(Collectors.toList());

        long start=System.currentTimeMillis();
        for (Order order : orders) {
            String ignored = order.getId();
        }
        long time = System.currentTimeMillis()-start;

        long reflectStart=System.currentTimeMillis();
        Method method = Order.class.getMethod("getId");
        method.setAccessible(true);
        for (Order order : orders) {
            Object ignored =method.invoke(order);
        }
        long reflectTime = System.currentTimeMillis()-reflectStart;

        System.out.println("base cost:"+time+"ms");
        System.out.println("reflect cost:"+reflectTime+"ms");
        System.out.println("reflect cost is :"+(reflectTime*1.0/time)+" times of base");

    }
}
