package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.ArrayHelper;
import org.junit.Test;


import static org.junit.Assert.*;

public class DefaultPropertyAccessorFactoryTest {

    @Test
    public void testCreate() {
        byte want=1;
        byte set=3;
        short ws=1;
        short ss=3;
        testCreateForAnyArray(new int[]{1,2,3,4,5}, 0,1, 2,3);
        testCreateForAnyArray(new byte[]{1,2,3,4,5}, 0, want, 2,set);
        testCreateForAnyArray(new short[]{1,2,3,4,5}, 0, ws, 2,ss);
        testCreateForAnyArray(new char[]{'1','2','3','4','5'}, 0,'1', 2,'3');
        testCreateForAnyArray(new long[]{1,2,3,4,5}, 0,1L, 2,3L);
        testCreateForAnyArray(new float[]{1,2,3,4,5}, 0,1F, 2,3F);
        testCreateForAnyArray(new double[]{1,2,3,4,5}, 0,1D, 2,3D);

        testCreateForAnyArray(new Integer[]{1,2,3,4,5}, 0,1, 2,3);
        testCreateForAnyArray(new Byte[]{1,2,3,4,5}, 0,want, 2,set);
        testCreateForAnyArray(new Short[]{1,2,3,4,5}, 0,ws, 2,ss);
        testCreateForAnyArray(new Character[]{'1','2','3','4','5'}, 0,'1', 2,'3');
        testCreateForAnyArray(new Long[]{1L,2L,3L,4L,5L}, 0,1L, 2,3L);
        testCreateForAnyArray(new Float[]{1F,2F,3F,4F,5F}, 0,1F, 2,3F);
        testCreateForAnyArray(new Double[]{1D,2D,3D,4D,5D}, 0,1D, 2,3D);
        testCreateForAnyArray(new String[]{"1","2","3","4","5"}, 0,"1", 2,"3");

        testCreateForAnyArray(new Object[]{1,2,3,4,5}, 0,1, 2,3);
    }

    private void testCreateForAnyArray(Object obj
            ,int readIndex,Object wantValue
            ,int setIndex,Object setValue
    ){
        DefaultPropertyAccessorFactory factory=DefaultPropertyAccessorFactory.create();
        factory.injectConfiguration(ObjectPathConfiguration.create());
        PropertyAccessor pa = factory.create(obj.getClass(), readIndex);
        assertEquals(wantValue,pa.get(obj));
        assertEquals(setValue,pa.set(obj,setValue));
        assertEquals(setValue,pa.get(obj));
        assertEquals(setValue, ArrayHelper.get(obj,setIndex));

        PropertyAccessor set= factory.create(obj.getClass(), setIndex);
        assertEquals(setValue,set.set(obj,setValue));
        assertEquals(setValue, ArrayHelper.get(obj,setIndex));
        assertEquals(setValue,pa.get(obj));

        PropertyAccessor max = factory.create(obj.getClass(), Integer.MAX_VALUE);
        boolean found = false;
        try {
            max.set(obj, setValue);
        } catch (PropertyAccessorRuntimeException e) {
            found=true;
        }
        assertTrue(found);
    }
}