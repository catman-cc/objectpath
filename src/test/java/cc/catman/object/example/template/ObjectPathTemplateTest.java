package cc.catman.object.example.template;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectPathTemplateTest {
    @Test
   public void format() {
        String template= "<div>${@.names.toString('<span>'+ @.value +'</span>')}</div>";
        Map<String, Object> context = new HashMap<>();
        context.put("names",Arrays.asList("a","b","c"));
        ObjectPathTemplate opt = new ObjectPathTemplate();
        String format = opt.format(template, context);
        Assert.assertEquals("<div><span>a</span><span>b</span><span>c</span></div>", format);
    }
}
