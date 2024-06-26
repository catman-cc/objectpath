package cc.catman.object;

import cc.catman.object.core.accessor.ClassifierObjectAccessor;
import cc.catman.object.core.function.DefaultFunctionManager;
import cc.catman.object.core.rewrite.AggregationObjectRewrite;
/**
 * 测试基类
 * @author jpanda
 * @since 0.0.1
 */
public abstract class BaseTest {

    protected static IObjectPath objectPath;

    static {
        ObjectPathConfiguration configuration = ObjectPathConfiguration.builder()
                .jsonCoder(new GsonCoder())
                .functionManager(new DefaultFunctionManager())
                .objectAccessor(ClassifierObjectAccessor.defaultAccessor())
                .objectRewrite(new AggregationObjectRewrite())
                .build();

        objectPath = ObjectPath.builder()
                .config(configuration)
                .build();
    }

}
