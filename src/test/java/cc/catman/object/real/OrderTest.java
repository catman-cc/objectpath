package cc.catman.object.real;

import cc.catman.object.BaseTest;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderItem;
import cc.catman.object.cases.Order.OrderMock;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTest extends BaseTest {

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
