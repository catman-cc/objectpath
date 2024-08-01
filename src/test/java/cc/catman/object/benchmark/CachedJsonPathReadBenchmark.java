package cc.catman.object.benchmark;

import com.alibaba.fastjson2.JSON;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.stream.Collectors;

public class CachedJsonPathReadBenchmark implements Benchmark{
    private String expr;

    public CachedJsonPathReadBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        List<String> orderStrList = objects.stream().map(JSON::toJSONString).collect(Collectors.toList());

        List<Object> preParedOrderDocuments=orderStrList
                .stream()
                .map(j-> Configuration.defaultConfiguration().jsonProvider().parse(j))
                .collect(Collectors.toList());
        StopAndWatch saw= StopAndWatch.start(getClass().getSimpleName());
        preParedOrderDocuments.forEach(d->JsonPath.read(d,expr));
        return saw.stop();
    }
}
