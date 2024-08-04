package cc.catman.object;

import cc.catman.OP;
import cc.catman.object.cases.Order.Order;
import cc.catman.object.cases.Order.OrderItem;
import cc.catman.object.cases.Order.OrderMock;
import cc.catman.object.core.Features;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultObjectPathAccessorTest {

    @Test
    public void setExprForSimple() {
        Person person = new Person("panda");
        ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
        cfg.getFeatures().put(Features.CONNECT_STRING_USE_PLUS_SIGN,true);

        ObjectPathAccessor accessor = ObjectPath.parse("$.name", cfg);
        accessor.setExpr(person,"1+$.name+1");
        assertEquals("1panda1",person.name);
    }

    @Test
    public void setExprFroObject(){
        Person person = new Person("panda");
        ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
        List<String> list = Arrays.asList("a", "b");
        ObjectPathAccessor accessor = ObjectPath.parse("$[\"like\"]",cfg);
        accessor.setValue(person, list);
        assertEquals(person.like,list);
    }

    @Test
    public void setExpr1(){
        Person person = new Person("panda");
        OP.parse("$['like']").setValue(person,Arrays.asList("a","b"));
        assertEquals(Arrays.asList("a", "b"), person.like);
    }

    @Test
    public void setExpr4Each(){
        Person person=new Person("panda",Arrays.asList("1","2","3","4","5"));
        OP.parse("$['like']").setExpr(person,"@[*].concat('-suffix')");
        assertEquals(Arrays.asList("1-suffix", "2-suffix", "3-suffix", "4-suffix", "5-suffix"), person.like);
    }

    @Test
    public void setExpr2(){
        Person person = new Person("panda");
        OP.parse("$['like'][0]").setValue(person,"a");
        assertNotNull(person.like);
        assertEquals("a", person.like.get(0));
    }

    @Test
    public void setExprWithComputer(){
        Order order= OrderMock.mockOrderWithFixedSize(10);
        OP.parse("$['items'][*]['quantity']").setExpr(order,"@*10");
        for (OrderItem item : order.getItems()) {
            assertEquals(0, item.getQuantity() % 10);
        }
    }

    public static class Person{
        private final String name;

        private List<String> like;

        public Person(String name) {
            this.name = name;
        }

        public Person(String name, List<String> like) {
            this.name = name;
            this.like = like;
        }
    }
}