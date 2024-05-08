package cc.catman.object.core.json;

public interface JsonCoder {
    String encode(Object object);
    <T> T decode(String json, Class<T> clazz);
}
