package cc.catman.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    /**
     * 类别
     */
    private String category;
    /**
     * 作者
     */
    private String author;
    /**
     * 书名
     */
    private String title;
    /**
     * 价格
     */
    private double price;
}
