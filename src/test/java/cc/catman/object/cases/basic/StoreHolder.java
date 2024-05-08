package cc.catman.object;

import lombok.Data;

/**
 * 商店持有者
 */
@Data
public class StoreHolder {
    /**
     * 商店
     */
    private Store store;
    /**
     * 价格
     */
    private double expensive;
}
