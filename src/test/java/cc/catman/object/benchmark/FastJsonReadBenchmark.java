package cc.catman.object.benchmark;

import com.alibaba.fastjson2.JSONPath;

import java.util.List;

public class FastJsonReadBenchmark implements Benchmark{
    private String expr;

    public FastJsonReadBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        StopAndWatch saw=StopAndWatch.start(this.getClass().getSimpleName());
        objects.forEach(object -> {
            JSONPath.of(expr).eval(object);
        });
        return saw.stop();
    }
}
