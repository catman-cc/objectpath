package cc.catman.object.benchmark;

import com.alibaba.fastjson2.JSON;
import com.jayway.jsonpath.JsonPath;

import java.util.List;

public class JsonPathReadBenchmark implements Benchmark {

    private String expr;

    public JsonPathReadBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        StopAndWatch saw = StopAndWatch.start(getClass().getSimpleName());
        objects.forEach(object -> {
            JsonPath.parse(JSON.toJSONString(object)).read(expr);
        });
        return saw.stop();
    }
}
