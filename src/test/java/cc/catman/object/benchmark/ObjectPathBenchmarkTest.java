package cc.catman.object.benchmark;

import cc.catman.OP;
import cc.catman.object.ObjectPathAccessor;
import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderMock;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@Threads(2)
@SuppressWarnings("java:S2187")
public class ObjectPathBenchmarkTest {
    private String expr="$.items[0].price";
    private Order order;
    private ObjectPathAccessor objectPath;
    private JSONPath fastjson;
    private JsonPath jsonPath;
    private Object document;
    private String jsonString;


    @Setup(Level.Trial)
    public void prepareOrderData(){
        this.order=OrderMock.mockOrder();
        this.objectPath=OP.parse(expr);
        this.fastjson =JSONPath.of(expr);
        this.jsonPath=JsonPath.compile(expr);
        this.jsonString = JSON.toJSONString(order);
        this.document=Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
    }

    @Benchmark
    public void objectPath(){
        ObjectPathAccessor accessor = OP.parse(expr);
        accessor.eval(order);
    }

    @Benchmark
    public void fastjson(){
        JSONPath fj = JSONPath.of(expr);
        fj.eval(order);
    }

    @Benchmark
    public void jsonpath(){
        JsonPath.read(jsonPath,expr);
    }

    @Benchmark
    public void objectPathWithCache(){
        this.objectPath.eval(order);
    }

    @Benchmark
    public void fastjsonWithCache(){
        this.fastjson.eval(order);
    }

    @Benchmark
    public void jsonpathWithCache(){
        this.jsonPath.read(document);
    }

    @SneakyThrows
    public static void main(String[] args) {
        Main.main(args);
    }
}
