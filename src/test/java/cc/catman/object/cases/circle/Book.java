package cc.catman.object.po;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 书本信息
 */
@Data
public class Book {
    /**
     * 书名
     */
    private String name;
    /**
     * ISBN,国际标准书号
     */
    private String isbn;
    /**
     * 主要作者
     */
    private Author mainAuthor;
    /**
     * 其他作者
     */
    private List<Author> authors= new ArrayList<>();
    /**
     * 价格
     */
    private int price;
    /**
     * 折扣
     */
    private double discount;
    /**
     * 出版日期
     */
    private Date publishDate;
}
