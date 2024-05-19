package cc.catman.object.cases.Order;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
@Data
@Builder
public class Order {
    /**
     * id
     */
    private String id;

    @Builder.Default
    private List<OrderItem> items=new ArrayList<>();

}
