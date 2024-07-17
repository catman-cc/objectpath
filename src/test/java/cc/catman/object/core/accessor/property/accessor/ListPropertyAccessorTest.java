package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ListPropertyAccessorTest {

    @Test
    public void test() {
        ObjectPathConfiguration configuration = ObjectPathConfiguration.create();
        ListPropertyAccessor accessor = new ListPropertyAccessor(configuration,0);
        List<String> list =new ArrayList<>( Arrays.asList("0", "1", "2", "3"));
        assertEquals("0", accessor.get(list));
        accessor.set(list, "4");
        assertEquals("4", accessor.get(list));

        ListPropertyAccessor outOfBoundAccessor = new ListPropertyAccessor(configuration,10);
        try {
            outOfBoundAccessor.get(list);
            // 2024年07月17日 移除下标越界异常
        }catch (IndexOutOfBoundsException e){
            // expected
        }
        outOfBoundAccessor.set(list, "10");
        assertEquals("10", outOfBoundAccessor.get(list));
    }

}