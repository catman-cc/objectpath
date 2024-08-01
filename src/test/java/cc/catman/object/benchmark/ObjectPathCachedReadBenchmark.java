package cc.catman.object.benchmark;

import cc.catman.object.ObjectPath;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.ObjectPathConfiguration;

import java.util.List;

public class ObjectPathCachedReadBenchmark implements Benchmark{

    private String expr;


    public ObjectPathCachedReadBenchmark(String expr) {
        this.expr = expr;
    }

    @Override
    public StopAndWatch exec(List<Object> objects) {
        ObjectPathConfiguration opc=ObjectPathConfiguration.create();
        opc.setUseCacheForReflect(true);
        opc.setEnableObjectClassifierCache(true);
        opc.setEnableObjectAccessorCache(true);
        opc.inject();
        ObjectPathAccessor accessor = ObjectPath.parse(expr, opc);

        StopAndWatch saw = StopAndWatch.start(getClass().getSimpleName());
        objects.forEach(accessor::eval);
        return saw.stop();
    }

}
