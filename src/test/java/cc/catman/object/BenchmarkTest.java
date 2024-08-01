package cc.catman.object;

import cc.catman.OP;
import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderMock;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
@SuppressWarnings("java:S1481")
public class BenchmarkTest extends BaseTest {
    @Test
    @Ignore("take long time")
    @SuppressWarnings("java:S2699")
    public void simpleBenchMark() {
        int count = 100000;
        List<Order> orders = IntStream.range(0, count).mapToObj(i -> OrderMock.mockOrder()).collect(Collectors.toList());

        long baseStart = System.currentTimeMillis();
        orders.forEach(order -> order.getItems().stream().filter(oi -> (oi.getPrice() - oi.getCostPrice()) * oi.getQuantity() > 2).count());

        long baseTime = System.currentTimeMillis() - baseStart;
        System.out.println("base cost:" + baseTime + "ms");

        long fullStart = System.currentTimeMillis();
        orders.forEach(order -> {
            ObjectPathAccessor as = objectPath.parse("$.items[?((@price-@costPrice) * @quantity >2 )].count()");
            as.eval(order);
        });
        long fullTime = System.currentTimeMillis() - fullStart;
        System.out.println("full cost:" + fullTime + "ms");
        System.out.println("full cost is :" + (fullTime * 1.0 / baseTime) + " times of base");

        long cacheStart = System.currentTimeMillis();
        ObjectPathAccessor as = objectPath.parse("$.items[?((@price-@costPrice) * @quantity >2 )].count()");
        orders.forEach(as::eval);
        long cacheTime = System.currentTimeMillis() - cacheStart;
        System.out.println("cache cost:" + cacheTime + "ms");
        System.out.println("cache cost is: " + (cacheTime * 1.0 / baseTime) + " times of base");
    }

    @Test
    @Ignore("take long time")
    @SneakyThrows
    @SuppressWarnings({"java:S2699", "java:S1854", "java:S1481"})
    public void reflectBenchmark() {
        List<Order> orders = IntStream.range(0, 100000).mapToObj(i -> OrderMock.mockOrder()).collect(Collectors.toList());

        long start = System.currentTimeMillis();
        for (Order order : orders) {
            String ignored = order.getId();
        }
        long time = System.currentTimeMillis() - start;

        long reflectStart = System.currentTimeMillis();
        Method method = Order.class.getMethod("getId");
        method.setAccessible(true);
        for (Order order : orders) {
            Object ignored = method.invoke(order);
        }
        long reflectTime = System.currentTimeMillis() - reflectStart;

        System.out.println("base cost:" + time + "ms");
        System.out.println("reflect cost:" + reflectTime + "ms");
        System.out.println("reflect cost is :" + (reflectTime * 1.0 / time) + " times of base");

    }

    @Test
    @SneakyThrows
    public void fastjson() {
        List<Order> orders = IntStream.range(0, 1000000).mapToObj(i -> OrderMock.mockOrder()).collect(Collectors.toList());
        StopAndWatch bsw = StopAndWatch.start("basic serialization");
        orders.forEach(order -> {
            String ignored = JSON.toJSONString(order);
        });
        System.out.println(bsw.stop().toString());

        StopAndWatch rsw = StopAndWatch.start("[fast json] read from object");
        orders.forEach(order -> {
            Object ignored = JSONPath.of("$.items[?(@.price >10)]").eval(order);
        });
        System.out.println(rsw.stop().toString());

        StopAndWatch ssw = StopAndWatch.start("[fast json] set from object");
        orders.forEach(order -> {
            Object ignored = JSONPath.set(order, "$.items[0].price", 100);
            Assert.assertEquals(100, order.getItems().get(0).getPrice());
        });
        System.out.println(ssw.stop());

        StopAndWatch cssw = StopAndWatch.start("[objet-path] set for object with catman");
        orders.forEach(order -> OP.parse("$.items[0].price").setValue(order, 100));
        System.out.println(cssw.stop());

        StopAndWatch ccssw = StopAndWatch.start("[cached][object-path] set for object with catman");
        ObjectPathAccessor cached = OP.parse("$.items[0].price");
        orders.forEach(order -> cached.setValue(order, 100));
        System.out.println(ccssw.stop());

        StopAndWatch ccossw = StopAndWatch.start("[cached][object-path] read for object with catman");
        ObjectPathAccessor cached4read = OP.parse("$.items[?(@.price >10)]");
        orders.forEach(order -> {
            Object ignored = cached4read.eval(order);
        });
        System.out.println(ccossw.stop());

        StopAndWatch jpsw = StopAndWatch.start("[json-pah with serializer] read from json");
        orders.forEach(order -> {
            Object object = JsonPath.parse(JSON.toJSONString(order)).read("$.items[*].price");
        });
        System.out.println(jpsw.stop());

        List<String> orderStrList = orders.stream().map(JSON::toJSONString).collect(Collectors.toList());
        StopAndWatch jppssw = StopAndWatch.start("[json-path pre-serializer] read from json");
        orderStrList.forEach(order->{
            Object ignored = JsonPath.parse(order).read("$.items[?(@.price >10)]");
        });
        System.out.println(jppssw.stop());


        List<Object> preParedOrderDocuments=orderStrList
                .stream()
                .map(j->Configuration.defaultConfiguration().jsonProvider().parse(j))
                .collect(Collectors.toList());

        StopAndWatch jppdsw = StopAndWatch.start("[json-path pre-document] read from json");

        preParedOrderDocuments.forEach(order-> JsonPath.read(order,"$.items[*].price"));
        System.out.println(jppdsw.stop());
    }

    @Data
    public static class StopAndWatch {
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
}
