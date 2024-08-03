package cc.catman.object.core.util;

import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ReflectionHelperTest {

    @Test
    public void isAssignableFrom() {
        assertTrue(ReflectionHelper.isAssignableFrom(Map.class, Map.class));
        assertTrue(ReflectionHelper.isAssignableFrom(Object.class,Map.class));
        assertTrue(ReflectionHelper.isAssignableFrom(Map.class,LinkedHashMap.class));
        assertTrue(ReflectionHelper.isAssignableFrom(List.class, ArrayList.class));
    }

    @Test
    public void findSubstringMethod(){
        Object res = ReflectionHelper.invokeMethod("hello world", "substring", Arrays.asList(0, 5));
        assertEquals("hello", res);
    }

    @Test
    public void invokeSameNameMethod(){
        A a = new A();
        Assert.assertEquals("hello", ReflectionHelper.invokeMethod(a,"hello", Collections.emptyList()));
        Assert.assertEquals("hello,world", ReflectionHelper.invokeMethod(a, "hello", Collections.singletonList("world")));
        Assert.assertEquals("hello,world age: 18", ReflectionHelper.invokeMethod(a, "hello", Arrays.asList("world", 18)));

        boolean hasException=false;
        try {
            Assert.assertEquals("hello,world age: 18", ReflectionHelper.invokeMethod(a, "hello", Arrays.asList("world", new Date())));
        } catch (PropertyAccessorRuntimeException e) {
            hasException=true;
        }
        Assert.assertTrue(hasException);
        hasException=false;
        B b = new B();
        try {
            Assert.assertEquals("hello,world age: 18", ReflectionHelper.invokeMethod(b, "hello", Arrays.asList("world", 18)));
        } catch (PropertyAccessorRuntimeException e) {
            hasException=true;
        }
        Assert.assertTrue(hasException);
    }

    public static class A{
        public String hello(){
            return "hello";
        }
        public String hello(String name){
            return "hello," + name;
        }
        public String hello(String name, Number age){
            return "hello," + name + " age: " + age;
        }
    }

    public static class B extends A{

        public String hello(String name,Object obj){
            return "hello," + name + " age: " + obj;
        }
    }
}