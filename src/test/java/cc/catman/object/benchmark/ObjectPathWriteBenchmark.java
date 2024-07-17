package cc.catman.object.benchmark;

import cc.catman.OP;

import java.util.List;

public class ObjectPathWriteBenchmark implements Benchmark{
    private String expr;

    public ObjectPathWriteBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        StopAndWatch saw= StopAndWatch.start(getClass().getSimpleName());
        objects.forEach(object -> {
            OP.parse(this.expr).setValue(object,100);
        });
        return saw.stop();
    }
}
