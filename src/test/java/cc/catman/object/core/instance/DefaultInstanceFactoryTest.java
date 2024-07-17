package cc.catman.object.core.instance;

import org.junit.Test;


public class DefaultInstanceFactoryTest {

    @Test
    @SuppressWarnings("java:S2699")
    public void create() {
        DefaultInstanceFactory factory = DefaultInstanceFactory.defaultInstance();

        factory.create(byte.class);
        factory.create(Byte.class);

        factory.create(short.class);
        factory.create(Short.class);

        factory.create(int.class);
        factory.create(Integer.class);

        factory.create(long.class);
        factory.create(Long.class);

        factory.create(double.class);
        factory.create(Double.class);

        factory.create(float.class);
        factory.create(Float.class);

        factory.create(boolean.class);
        factory.create(Boolean.class);

        factory.create(char.class);
        factory.create(Character.class);
    }

    @Test
    @SuppressWarnings("java:S2699")
    public void createArray(){
        DefaultInstanceFactory factory = DefaultInstanceFactory.defaultInstance();
        factory.create(byte[].class);
        factory.create(short[].class);
        factory.create(int[].class);
        factory.create(long[].class);
        factory.create(double[].class);
        factory.create(float[].class);
        factory.create(boolean[].class);
        factory.create(char[].class);

        factory.create(Byte[].class);
        factory.create(Short[].class);
        factory.create(Integer[].class);
        factory.create(Long[].class);
        factory.create(Double[].class);
        factory.create(Float[].class);
        factory.create(Boolean[].class);
        factory.create(Character[].class);
        factory.create(String[].class);
    }
}