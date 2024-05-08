package cc.catman.object;

import cc.catman.object.core.json.JsonCoder;
import com.google.gson.Gson;

public class GsonCoder implements JsonCoder{
    @Override
    public String encode(Object object) {
        return new Gson().toJson(object);
    }

    @Override
    public <T> T decode(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
}
