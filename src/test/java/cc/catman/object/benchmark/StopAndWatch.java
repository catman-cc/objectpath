package cc.catman.object.benchmark;

import lombok.Data;

@Data
public class StopAndWatch {
    private long start;
    private long end;
    private String name;

    public static StopAndWatch start(String name) {
        StopAndWatch sw = new StopAndWatch();
        sw.start = System.currentTimeMillis();
        sw.name = name;
        return sw;
    }

    public StopAndWatch stop() {
        this.end = System.currentTimeMillis();
        return this;
    }

    public long getConsumeTime() {
        return this.end - this.start;
    }

    public String toString() {
        return String.format("任务:[%s],总耗时:[%d ms]", this.name, this.end - this.start);
    }
}
