package cc.catman.object.core.accessor.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * 类名匹配器
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultClassNameMatcher implements IClassNameMatcher {
    private static final String CLASS_FILE_SUFFIX = ".class";

    protected List<String> packages;

    protected List<String> classes;

    protected Map<String, Pattern> regex;

    public DefaultClassNameMatcher() {
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.regex = new HashMap<>();
    }


    public static DefaultClassNameMatcher create() {
        return new DefaultClassNameMatcher();
    }

    public static DefaultClassNameMatcher of(String... packages) {
        return new DefaultClassNameMatcher().addPackage(packages);
    }

    public DefaultClassNameMatcher addPackage(String... packages) {
        for (String p : packages) {
            if (!this.packages.contains(p)) {
                this.packages.add(p);
            }
        }
        return this;
    }

    public DefaultClassNameMatcher addPackage(Package p){
        return addPackage(p.getName());
    }

    public DefaultClassNameMatcher addClasses(Class<?>... classes) {
        for (Class<?> c : classes) {
            addClasses(c.getName());
        }
        return this;
    }

    protected DefaultClassNameMatcher addClasses(String classNames) {
        for (String c : classes) {
            if (!this.classes.contains(c)) {
                this.classes.add(c);
            }
        }
        return this;
    }

    protected DefaultClassNameMatcher addRegexs(String... regexs) {
        for (String r : regexs) {
            if (!this.regex.containsKey(r)) {
                this.regex.put(r, Pattern.compile(r));
            }
        }
        return this;
    }

    @Override
    public boolean match(String className) {
        final String cn = className.endsWith(CLASS_FILE_SUFFIX)
                ? className.substring(0, className.length() - CLASS_FILE_SUFFIX.length())
                : className;

        return this.packages.stream().anyMatch(cn::startsWith) ||
               this.classes.stream().anyMatch(cn::equals) ||
               this.regex.values().stream().anyMatch(r -> r.matcher(cn).matches());
    }
}
