package cc.catman.object.core.classifier.object;

import cc.catman.object.core.classifier.EObjectClassification;
import cc.catman.object.core.classifier.AbstractClassifierObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 标准对象
 * @author jpanda
 * @since 0.0.1
 */
@Slf4j
public class StandardObjectClassifierObject extends AbstractClassifierObject {

    public StandardObjectClassifierObject(Object raw) {
        super(raw, EObjectClassification.STANDARD_OBJECT);
    }

    @Override
    @SuppressWarnings("java:S3011")
    public List<?> toList() {
        Class<?> clazz = this.raw.getClass();
        Field[] fs = clazz.getDeclaredFields();
        List<Object> vs=new ArrayList<>();
        for (Field f : fs) {
            f.setAccessible(true);
            try {
                vs.add(f.get(this.raw));
            } catch (IllegalAccessException e) {
                log.error("获取字段值失败",e);
            }
        }
        return vs;
    }

    @Override
    @SuppressWarnings("java:S3011")
    public Map<?, ?> toMap() {
        Class<?> clazz = this.raw.getClass();
        Field[] fs = clazz.getDeclaredFields();
        Map<String,Object> fm=new LinkedHashMap<>();
        for (Field f : fs) {
            f.setAccessible(true);
            try {
                fm.put(f.getName(),f.get(this.raw));
            } catch (IllegalAccessException e) {
                log.error("获取字段值失败",e);
            }
        }
        return fm;
    }

    @Override
    public Object toObject() {
        return this.getRaw();
    }
}
