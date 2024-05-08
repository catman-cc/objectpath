package cc.catman.object.core.classifier.str;

import cc.catman.object.core.classifier.EObjectClassification;
import cc.catman.object.core.classifier.AbstractClassifierObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 字符串对象
 * @author jpanda
 * @since 0.0.1
 */
@Slf4j
public class StringClassifierObject extends AbstractClassifierObject {

    public StringClassifierObject(Object raw ) {
        super(raw, EObjectClassification.STRING);
    }

    @Override
    public String toText() {
        return raw.toString();
    }

    @Override
    public Number toNumber() {
        try {
            return Double.parseDouble(raw.toString());
        } catch (NumberFormatException e) {
            log.trace("转换数字失败", e);
            return  super.toNumber();
        }
    }
}
