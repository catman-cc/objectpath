package cc.catman.object.cases.circle;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 图书馆信息
 */
@Data
public class Library {
    private String name;
    private String address;
    private List<Book> books= new ArrayList<>();
}
