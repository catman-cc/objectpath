package cc.catman.object.benchmark;

import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderMock;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BenchmarkTest {

    @Test
    @SneakyThrows
    public void  benchmark(){
        List<String> path4read= Arrays.asList(
                "$.items[0].price",
                "$.items[?(@.price >10)]"
        );
        final List<Object> orders = IntStream
                .range(0, 500000)
                .mapToObj(i -> OrderMock.mockOrder())
                .collect(Collectors.toList());

        path4read.forEach(r->{
            System.out.println("===================[ "+ r + " ]=============");
            List<Benchmark> benchmarks= Arrays.asList(
                    new ObjectPathReadBenchmark(r),
                    new ObjectPathCachedReadBenchmark(r),
                    new FastJsonReadBenchmark(r),
                    new FastJsonCachedReadBenchmark(r),
                    new JsonPathReadBenchmark(r),
                    new CachedJsonPathReadBenchmark(r)
            );
            benchmarks.forEach(b->{
                StopAndWatch saw = b.exec(orders);
                System.out.println(saw);
            });
            System.out.println("===================[ "+ r + " END ]=========");
            System.out.println();
            System.out.println();

        });
    }
}
