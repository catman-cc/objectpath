package cc.catman.object;

/**
 * 测试基类
 *
 * @author jpanda
 * @since 0.0.1
 */
public abstract class BaseTest {

    protected static IObjectPath objectPath;

    static {
        objectPath = ObjectPath.builder()
                .config(ObjectPathConfiguration.create()
                )
                .build();
    }

}
