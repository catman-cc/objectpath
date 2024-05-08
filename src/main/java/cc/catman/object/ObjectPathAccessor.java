package cc.catman.object;

public interface ObjectPathAccessor {

    default Object eval(Object object){
        return eval(object, Object.class);
    }

    <T> T eval(Object object, Class<T> clazz);
}
