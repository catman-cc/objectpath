package cc.catman.object.core.accessor;


import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

public class DefaultClassObjectAccessorTest {

    @Test
    public void testUnSupport() {
        DefaultClassObjectAccessor objectAccessor = new DefaultClassObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(null, null));
    }
    @Test
    public void testGet() {
        TestClass testObject = createTestObject();
        DefaultClassObjectAccessor objectAccessor = new DefaultClassObjectAccessor();
        Assert.assertEquals("a", objectAccessor.get(testObject, "a"));
        Assert.assertEquals(true, objectAccessor.get(testObject, "b"));
        Assert.assertEquals("c", objectAccessor.get(testObject, "c"));
        Assert.assertEquals("d", objectAccessor.get(testObject, "d"));
        Assert.assertEquals("e", objectAccessor.get(testObject, "e"));
        Assert.assertNull(objectAccessor.get(testObject, "f"));
    }

    @Test
    public void testGetNull() {
        DefaultClassObjectAccessor objectAccessor = new DefaultClassObjectAccessor();
        Assert.assertNull(objectAccessor.get(null, "a"));
        Assert.assertNull(objectAccessor.get(null, null));
    }

    @Test
    public void testGetWithNullKey() {
        DefaultClassObjectAccessor objectAccessor = new DefaultClassObjectAccessor();
        Assert.assertNull(objectAccessor.get(createTestObject(), null));
    }

    private static TestClass createTestObject(){
        return new TestClass("a", true, "c", "d","e");
    }
    /**
     * 一个特殊的不符合开发规范的class定义
     */
    private static class TestClass {
        @Getter
        private final String a;
        private final Boolean b;
        private final String c;
        private final String d;
        private final String e2;

        public TestClass(String a, Boolean b, String c, String d,String e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e2 = e;
        }

        public Boolean isB() {
            return b;
        }
        public String c() {
            return c;
        }
        public String e(){
            return e2;
        }
        public String getD2() {
            return d;
        }
    }
}