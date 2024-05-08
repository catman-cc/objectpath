package cc.catman.object.core.function;

import lombok.Builder;
import lombok.Data;
/**
 * 函数返回值描述
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
public class FunctionResultDesc {
    /**
     * 返回值类型
     */
    private String type;
    /**
     * 返回值描述
     */
    private String desc;
}
