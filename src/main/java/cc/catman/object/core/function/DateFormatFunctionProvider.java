package cc.catman.object.core.function;

import cc.catman.object.core.ObjectPathParserContext;
import cc.catman.object.core.exception.FunctionRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * 日期格式化函数提供者,用于格式化日期
 * 支持的参数有: Date,Timestamp,Long,String
 * @author jpanda
 * @since 0.0.1
 */
@Slf4j
public class DateFormatFunctionProvider extends AbstractFunctionProvider{

    public DateFormatFunctionProvider(String name) {
        super(name);
        FunctionArgDesc format = FunctionArgDesc.builder()
                .type(String.class)
                .desc("格式")
                .build();
        this.args().add(Collections.singletonList(
                format

        ));
    }

    @Override
    protected Object doApply(ObjectPathParserContext context,List<Object> params) {
        if (params.size() != 1) {
            log.error("参数数量不匹配");
            return null;
        }
        Object date = context.currentValue().read();
        Object format = params.get(0);
        if (date instanceof java.util.Date) {
            return new SimpleDateFormat(format.toString()).format((Date) date);
        } else if (date instanceof Long) {
            return new SimpleDateFormat(format.toString()).format(new Date((Long) date));
        } else if (date instanceof String) {
            long time;
            try {
                time = Long.parseLong((String) date);
            } catch (NumberFormatException e) {
                throw new FunctionRuntimeException(e, this.name(), params);
            }
            return new SimpleDateFormat(format.toString()).format(new Date(time));
        }
        return null;
    }

}
