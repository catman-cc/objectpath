package cc.catman.object.core.script;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * ScriptObjectMirror解析器,用于递归解析ScriptObjectMirror对象为普通java对象
 * @author jpanda
 * @since 2024-04-23
 */
public class ScriptObjectMirrorParser {

    public Object parse(Object obj) {
        if (obj instanceof ScriptObjectMirror){
            return parse((ScriptObjectMirror) obj);
        }
        return obj;
    }

    public Object parse(ScriptObjectMirror mirror) {
        if (mirror.isArray()){
            List<Object> list = new ArrayList<>();
            mirror.values().forEach(o->{
                list.add(parse(o));
            });
            return list;
        }
        if (mirror.isFunction()){
            throw new RuntimeException("script return a function");
        }
        if (mirror.isEmpty()) {
            return null;
        }
        // 此时认为是一个map,将其转换为map
        Map<String,Object> map=new LinkedHashMap<>();
        mirror.forEach((key, value) -> map.put(key, parse(value)));
        return map;
    }
}
