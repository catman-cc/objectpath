package cc.catman.object.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 字符串工具类
 * @author jpanda
 * @since 0.0.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringHelper {

    public static String unquote(String str) {
        if (Objects.isNull(str)){
            return null;
        }
        if (str.trim().isEmpty()){
            return "";
        }
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        if (str.startsWith("'") && str.endsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
}
