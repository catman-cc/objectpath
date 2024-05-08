package cc.catman.object.po;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者信息
 */
@Data
public class Author {
    /**
     * 作者名
     */
    private String name;
    /**
     * 作者年龄
     */
    private int age;
    /**
     * 作者的书籍
     */
    private List<Book> books=new ArrayList<>();
}
