package cc.catman.object.benchmark;

import cc.catman.OP;

import java.util.List;

public class ObjectPathReadBenchmark implements Benchmark{
    private String expr;

    public ObjectPathReadBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        StopAndWatch saw = StopAndWatch.start(getClass().getSimpleName());
        objects.forEach(object -> {
            OP.parse(expr).eval(object);
        });
        return saw.stop();
    }
}
