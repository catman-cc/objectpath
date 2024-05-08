package cc.catman.object.core.function;

import lombok.Builder;
import lombok.Data;

/**
 * 函数参数描述
 * @author jpanda
 * @since 0.0.1
 */
@Data
@Builder
public class FunctionArgDesc {
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数类型
     */
    private Class<?> type;
    /**
     * 是否必须
     */
    private boolean required;

    /**
     * 参数描述
     */
    private String desc;

}
