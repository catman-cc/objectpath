package cc.catman.object.cases.basic;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商店
 */
@Data
public class Store {
    /**
     * 书
     */
    private List<Book> book=new ArrayList<>();
    /**
     * 自行车
     */
    private List<Bicycle> bicycle=new ArrayList<>();
}
