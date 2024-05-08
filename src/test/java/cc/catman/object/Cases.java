package cc.catman.object;

import cc.catman.object.cases.basic.Bicycle;
import cc.catman.object.cases.basic.Book;
import cc.catman.object.cases.basic.Store;
import cc.catman.object.cases.basic.StoreHolder;

import java.util.ArrayList;
import java.util.List;

public class Cases {

    public static StoreHolder storeHolder() {
        StoreHolder storeHolder = new StoreHolder();
        Store store = new Store();
        storeHolder.setStore(store);
        storeHolder.setExpensive(10D);
        store.getBook().addAll(books());
        store.getBicycle().addAll(bicycles());
        return storeHolder;
    }

    public static List<Book> books() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("文学", "鲁迅", "呐喊", 10.0));
        books.add(new Book("文学", "鲁迅", "彷徨", 15.0));
        books.add(new Book("文学", "鲁迅", "故事新编", 20.0));
        books.add(new Book("文学", "鲁迅", "狂人日记", 11.0));
        books.add(new Book("文学", "鲁迅", "阿Q正传", 12.0));
        books.add(new Book("文学", "鲁迅", "阿Q外传", 13.0));
        books.add(new Book("文学", "鲁迅", "华盖集", 14.0));
        books.add(new Book("文学", "鲁迅", "朝花夕拾", 15.0));
        books.add(new Book("文学", "鲁迅", "野草", 8.0));
        books.add(new Book("文学", "鲁迅", "且介亭杂文", 9.0));
        books.add(new Book("文学", "鲁迅", "中国小说史略", 10.0));

        books.add(new Book("文学", "余华", "活着", 20.0));
        books.add(new Book("文学", "余华", "许三观卖血记", 15.0));
        books.add(new Book("文学", "余华", "兄弟", 25.0));
        books.add(new Book("文学", "余华", "许三观卖血记", 12.0));

        books.add(new Book("文学", "莫言", "红高粱家族", 30.0));
        books.add(new Book("文学", "莫言", "丰乳肥臀", 35.0));
        books.add(new Book("文学", "莫言", "蛙", 40.0));
        books.add(new Book("文学", "莫言", "生死疲劳", 45.0));

        books.add(new Book("文学", "老舍", "骆驼祥子", 50.0));
        books.add(new Book("文学", "老舍", "四世同堂", 55.0));
        books.add(new Book("文学", "老舍", "茶馆", 60.0));

        books.add(new Book("诗歌", "杜甫", "三吏诗", 12.0));
        books.add(new Book("诗歌", "杜甫", "咏怀古迹", 15.0));
        books.add(new Book("诗歌", "杜甫", "祭十二郎文稿", 17.0));
        books.add(new Book("诗歌", "杜甫", "登高", 14.0));

        books.add(new Book("经典名著", "罗曼·罗兰", "论财富", 30.0));
        books.add(new Book("经典名著", "罗曼·罗兰", "论祖国", 29.0));
        books.add(new Book("经典名著", "罗曼·罗兰", "论智慧", 27.0));

        books.add(new Book("科幻小说", "阿西莫夫", "机器人系列", 25.0));
        books.add(new Book("科幻小说", "阿西莫夫", "基地系列", 22.0));
        books.add(new Book("科幻小说", "阿西莫夫", "银河帝国系列", 20.0));
        books.add(new Book("科幻小说", "菲利普·迪克", "安卓们", 18.0));

        books.add(new Book("历史故事", "司马迁", "史记", 28.0));
        books.add(new Book("历史故事", "司马迁", "资治通鉴", 30.0));
        books.add(new Book("历史故事", "列夫·托尔斯泰", "战争与和平", 32.0));

        books.add(new Book("悬疑小说", "阿加莎·克里斯蒂", "东方快车谋杀案", 15.0));
        books.add(new Book("悬疑小说", "阿加莎·克里斯蒂", "尼罗河谋杀案", 17.0));
        books.add(new Book("悬疑小说", "阿加莎·克里斯蒂", "无人生还", 20.0));
        return books;
    }

    public static List<Bicycle> bicycles() {
        List<Bicycle> bicycles = new ArrayList<>();
        bicycles.add(new Bicycle("red", 100.0));
        bicycles.add(new Bicycle("blue", 200.0));
        bicycles.add(new Bicycle("yellow", 300.0));
        bicycles.add(new Bicycle("green", 400.0));
        bicycles.add(new Bicycle("purple", 800.0));
        bicycles.add(new Bicycle("orange", 900.0));
        bicycles.add(new Bicycle("gray", 1000.0));
        bicycles.add(new Bicycle("black", 500.0));
        bicycles.add(new Bicycle("white", 600.0));
        bicycles.add(new Bicycle("pink", 700.0));
        return bicycles;
    }
}
