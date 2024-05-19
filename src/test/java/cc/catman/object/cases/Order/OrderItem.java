package cc.catman.object.cases.Order;

import lombok.Builder;
import lombok.Data;

/**
 * 订单项
 */
@Data
@Builder
public class OrderItem {
    /**
     * 订单项id
     */
    private String id;
    /**
     * 订单项名称
     */
    private String name;
    /**
     * 订单项价格
     */
    private int price;
    /**
     * 订单项成本价
     */
    private int costPrice;
    /**
     * 订单项数量
     */
    private int quantity;

}
