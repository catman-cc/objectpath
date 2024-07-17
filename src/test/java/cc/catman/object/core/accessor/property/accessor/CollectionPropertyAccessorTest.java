package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class CollectionPropertyAccessorTest {
    @Test
    public void testSetPropertyAccessor(){
        CollectionPropertyAccessor accessor = new CollectionPropertyAccessor(
                ObjectPathConfiguration.create()
        );
        List<? extends Serializable> collection = Arrays.asList("0", 1, 2, "3", 4);

        Assert.assertEquals(collection,accessor.get(collection));
        Assert.assertEquals(collection.get(0),accessor.get(collection,0).read());

        accessor.set(collection,0,"123");
        Assert.assertEquals("123",accessor.get(collection,0).read());
    }

}