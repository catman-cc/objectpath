package cc.catman.object.core;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任意集合中存放的实体
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
@Accessors(chain = true,fluent = true)
public class Entity {
    /**
     * 索引
     */
    private Integer index;
    /**
     * 键
     */
    private Object key;
    /**
     * 值
     */
    private Object value;

    /**
     * 获取索引或键
     * @return 索引或键
     */
    public Object getIndexOrKey() {
        return index != null ? index : key;
    }

}
