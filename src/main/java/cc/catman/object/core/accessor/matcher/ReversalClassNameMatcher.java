package cc.catman.object.core.accessor.matcher;

/**
 * 反转类名匹配器
 * @author jpanda
 * @since 0.0.1
 */
public class ReversalClassNameMatcher implements IClassNameMatcher{

    private final IClassNameMatcher matcher;

    public ReversalClassNameMatcher(IClassNameMatcher matcher) {
        this.matcher = matcher;
    }
    public static ReversalClassNameMatcher wrapper(IClassNameMatcher matcher){
        return  new ReversalClassNameMatcher(matcher);
    }
    @Override
    public boolean match(String name) {
        return !matcher.match(name);
    }
}
