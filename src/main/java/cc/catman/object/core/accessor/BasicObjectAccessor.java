package cc.catman.object.core.accessor;

import cc.catman.object.core.Entity;
import cc.catman.object.core.accessor.matcher.IClassMatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * 基础对象访问器,不支持任何操作,被其匹配的对象,将会作为最小单元处理,不会支持任何分解操作
 *
 * @author jpanda
 * @since 0.0.1
 */
public class BasicObjectAccessor implements SpecifyObjectAccessor {
    /**
     * 匹配器
     */
    private final List<IClassMatcher> matchers;

    private final List<IClassMatcher> excludeMatchers;

    public static BasicObjectAccessor defaultAccessor() {
        BasicObjectAccessor boa = new BasicObjectAccessor();
        // java基础类型
        boa.matchers.add(c-> c.isPrimitive() || c.getName().startsWith("java.lang"));
        // 同时常见的一些类型,如日期,时间,枚举等,也作为基础类型处理
        // 实际上,此处使用ObjectRewrite才是最合理的处理方式,比如,将Date类型转换为long或者字符串
        boa.matchers.add(Enum.class::isAssignableFrom);
        boa.matchers.add(java.util.Date.class::isAssignableFrom);
        boa.matchers.add(java.sql.Time.class::isAssignableFrom);
        boa.matchers.add(java.sql.Timestamp.class::isAssignableFrom);

        // 排除迭代器类型
        boa.excludeMatchers.add(Iterator.class::isAssignableFrom);
        return boa;
    }


    public BasicObjectAccessor() {
        this(new ArrayList<>());
    }

    public BasicObjectAccessor(List<IClassMatcher> matchers) {
       this(matchers,new ArrayList<>());
    }

    public BasicObjectAccessor(List<IClassMatcher> matchers, List<IClassMatcher> excludeMatchers) {
        this.matchers = matchers;
        this.excludeMatchers = excludeMatchers;
    }

    @Override
    public boolean isSupport(Object object, Object key, EAccessorKind kind) {
        if (object == null) {
            return true;
        }
        // 排除匹配
        for (IClassMatcher matcher : excludeMatchers) {
            if (matcher.match(object.getClass())) {
                return false;
            }
        }

        for (IClassMatcher matcher : matchers) {
            if (matcher.match(object.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object object, Object key) {
        throw new UnsupportedOperationException("基础类型不支持get操作");
    }

    @Override
    public void eachKey(Object object, Consumer<Object> consumer) {

    }

    @Override
    public void eachValue(Object object, Consumer<Object> consumer) {

    }
    
    @Override
    public void eachEntry(Object object, Consumer<Entity> consumer) {

    }

    @Override
    public String covertToString(Object object) {
        return object == null ? null : object.toString();
    }
}
