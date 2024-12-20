package cc.catman.object.positive;

import cc.catman.object.*;
import cc.catman.object.core.function.DateFormatFunctionProvider;
import cc.catman.object.core.function.FunctionManager;
import cc.catman.object.core.script.javascript.JavaScriptExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class AdvanceTest extends BaseTest {

    @Test
    public void customMethodTest() {
        ObjectPathConfiguration configuration = ObjectPathConfiguration.create();

        FunctionManager fm = configuration.getFunctionManager();
        fm.register(new DateFormatFunctionProvider("format"));

        ObjectPath op = ObjectPath.builder()
                .config(configuration)
                .build();
        long now = System.currentTimeMillis();
        Map<String,Long> map=new HashMap<>();
        map.put("time",now);
        ObjectPathAccessor ac = op.parse("$.time.format('yyyy-MM-dd HH:mm:ss')");
        Object res = ac.eval(map);
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now),res);
    }

    @Test
    public void jsScriptTest() {
        ObjectPathConfiguration configuration = ObjectPathConfiguration.create();

        configuration.getScriptExecutorManager().addExecutor("js",new JavaScriptExecutor());

        ObjectPath op = ObjectPath.builder()
                .config(configuration)
                .build();
        Map<String,String> map=new HashMap<>();
        map.put("name","world");
        ObjectPathAccessor ac = op.parse("$.name.[<js name=$.name>return \"hello,\"+name;</js>]");
        Object res = ac.eval(map);
        Assert.assertEquals("hello,world", res);
    }
}
