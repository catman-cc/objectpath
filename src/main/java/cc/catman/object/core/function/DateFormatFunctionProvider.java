package cc.catman.object.core.function;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
        this.args().add(Arrays.asList(
                FunctionArgDesc.builder()
                        .type(Date.class)
                        .desc("日期")
                        .build(),
                format

        ));
        this.args().add(Arrays.asList(
                FunctionArgDesc.builder()
                        .type(Long.class)
                        .desc("时间戳")
                        .build(),
                format
        ));
        this.args().add(Arrays.asList(
                FunctionArgDesc.builder()
                        .type(String.class)
                        .desc("日期字符串")
                        .build(),
                format
        ));
    }

    @Override
    protected Object doApply(List<Object> params) {
        if (params.size() != 2) {
            log.error("参数数量不匹配");
            return null;
        }
        Object date = params.get(0);
        Object format = params.get(1);
        if (date instanceof java.util.Date) {
            return new SimpleDateFormat(format.toString()).format((Date) date);
        } else if (date instanceof Long) {
            return new SimpleDateFormat(format.toString()).format(new Date((Long) date));
        } else if (date instanceof String) {
            Long time = null;
            try {
                time = Long.parseLong((String) date);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            return new SimpleDateFormat(format.toString()).format(new Date(time));
        }
        return null;
    }

}
