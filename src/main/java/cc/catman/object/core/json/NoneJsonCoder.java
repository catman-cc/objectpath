package cc.catman.object.core.json;

import lombok.extern.slf4j.Slf4j;


/**
 * 一个不具备任何功能的json编码器
 * @author jpanda
 * @since 0.0.2
 */
@Slf4j
public class NoneJsonCoder extends AbstractJsonCoder{

    @Override
    public String encode(Object object) {
        throw new UnsupportedOperationException("This json coder is not supported.");
    }

    @Override
    public <T> T decode(String json, Class<T> clazz) {
        throw new UnsupportedOperationException("This json coder is not supported.");
    }
}
