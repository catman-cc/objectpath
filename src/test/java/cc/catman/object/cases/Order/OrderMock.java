package cc.catman.object.cases.Order;

import java.util.Random;
import java.util.UUID;

public class OrderMock {

    public static Order mockOrder(){
        String id = UUID.randomUUID().toString();
        Order order = Order.builder().id(id).build();
        for (int i = 0; i < randomInt(100); i++) {
            order.getItems().add(mockItem());
        }
        return order;
    }

    public static OrderItem mockItem(){
        String id = UUID.randomUUID().toString();
        String name = randomName();
        int price = randomInt(500);
        int costPrice = price - randomInt(price);
        int quantity = new Random().nextInt(10);
        return OrderItem.builder().id(id).name(name).price(price).costPrice(costPrice).quantity(quantity).build();
    }

    /**
     * 随机生成价格,最大100
     */
    private static int randomInt(int bound){
        return new Random().nextInt(bound);
    }

    /**
     * 一个简单的商品名称随机生成方法
     */
    private static String randomName(){
        return UUID.randomUUID().toString();
    }
}
