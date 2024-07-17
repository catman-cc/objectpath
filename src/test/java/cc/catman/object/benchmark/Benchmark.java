package cc.catman.object.benchmark;

import java.util.List;

public interface Benchmark {

   default void before(){

    }
    StopAndWatch exec(List<Object> objects);
}
