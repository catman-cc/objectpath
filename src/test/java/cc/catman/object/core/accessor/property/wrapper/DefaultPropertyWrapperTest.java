package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.accessor.property.accessor.UnfinishedPropertyAccessor;
import cc.catman.object.core.util.ReflectionHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultPropertyWrapperTest {

    @Test
    public void testForSimpleList(){
        PropertyWrapperFactory wf = ObjectPathConfiguration.create().getWrapperFactory();
        List<String> s=new ArrayList<>(Arrays.asList("1","2","3"));
        PropertyWrapper pw = wf.create(s);
        Object read = pw.read();
        assertEquals(s,read);
    }

    @Test public void testForReadListElement(){
        PropertyWrapperFactory wf = ObjectPathConfiguration.create()
                .getWrapperFactory();
        List<String> s=new ArrayList<>(Arrays.asList("1","2","3"));
        PropertyWrapper pw = wf.create(s);

        Object read1 = pw.read(0).read();
        assertEquals("1",read1);

        Class<?> readType1 = pw.readType(0);
        assertEquals(String.class,readType1);

        pw.set(0,"4");
        Object read2 = pw.read(0).read();
        assertEquals("4",read2);

        pw.set(10,"5");
        Object read3 = pw.read(10).read();
        assertEquals("5",read3);
    }

    @Test
    public void testForReadListWithOnlyType(){
        ObjectPathConfiguration configuration = ObjectPathConfiguration.create();
        UnfinishedPropertyAccessor pa = new UnfinishedPropertyAccessor(configuration, null);
        Object o = pa.get(Arrays.asList("1","2","3"));
        assertTrue(ReflectionHelper.isAssignableFrom(List.class, o.getClass()));
    }
}