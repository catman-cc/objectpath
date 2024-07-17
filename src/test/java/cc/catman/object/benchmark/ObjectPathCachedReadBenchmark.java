package cc.catman.object.benchmark;

import cc.catman.OP;
import cc.catman.object.ObjectPathAccessor;

import java.util.List;

public class ObjectPathCachedReadBenchmark implements Benchmark{

    private String expr;


    public ObjectPathCachedReadBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        ObjectPathAccessor accessor = OP.parse(expr);
        StopAndWatch saw = StopAndWatch.start(getClass().getSimpleName());
        objects.forEach(accessor::eval);
        return saw.stop();
    }
}
