package cc.catman.object.core.instance;

import cc.catman.object.core.instance.creator.ListInstanceCreator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListInstanceCreatorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testCreate() {
        ListInstanceCreator creator = new ListInstanceCreator();
        List<String> list = creator.create(List.class);
        assertNotNull(list);
        Assert.assertEquals(list.getClass(), ArrayList.class);
    }

}