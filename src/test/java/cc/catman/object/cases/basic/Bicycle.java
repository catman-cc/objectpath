package cc.catman.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自行车
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bicycle {
    /**
     * 颜色
     */
    private String color;
    /**
     * 价格
     */
    private double  price;
}
