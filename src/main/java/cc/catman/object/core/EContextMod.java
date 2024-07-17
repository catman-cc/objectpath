package cc.catman.object.core;

import lombok.Getter;

/**
 * 上下文对应的模式
 * 使用不同的上下文模式,可以有效的切换对象访问的复杂度,从而提高性能
 * 因为在写入模式下,会进行更复杂的包装和验证
 * @author jpanda
 * @since 0.0.3
 */
public enum EContextMod {
    /**
     * 只读模式,不会修改上下文
     */
    READ_ONLY(true),
    /**
     * 写入模式,该模式用于操作及更新原始对象的数据
     */
    WRITE_MOD(false),
    ;

    @Getter
    private final boolean readOnly;

    EContextMod(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
