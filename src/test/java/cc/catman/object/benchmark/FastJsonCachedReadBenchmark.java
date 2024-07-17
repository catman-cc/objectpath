package cc.catman.object.benchmark;

import com.alibaba.fastjson2.JSONPath;

import java.util.List;

public class FastJsonCachedReadBenchmark implements Benchmark{
    private String expr;
private JSONPath jp;
    public FastJsonCachedReadBenchmark(String expr) {
        this.expr = expr;
        this.jp=JSONPath.of(expr);
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        StopAndWatch saw=StopAndWatch.start(this.getClass().getSimpleName());
        objects.forEach(object -> {
            jp.eval(object);
        });
        return saw.stop();
    }
}
