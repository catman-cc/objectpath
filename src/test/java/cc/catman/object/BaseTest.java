package cc.catman.object;

import cc.catman.object.core.accessor.ClassifierObjectAccessor;
import cc.catman.object.core.function.DefaultFunctionManager;
import cc.catman.object.core.rewrite.AggregationObjectRewrite;

public class BaseTest {

    protected static ObjectPath objectPath;

    static {
        ObjectPathConfiguration configuration = ObjectPathConfiguration.builder()
                .jsonCoder(new GsonCoder())
                .functionManager(new DefaultFunctionManager())
                .objectAccessor(ClassifierObjectAccessor.defaultAccessor())
                .objectRewrite(new AggregationObjectRewrite())
                .build();

        objectPath =DefaultObjectPath.builder()
                .config(configuration)
                .build();
    }

}
