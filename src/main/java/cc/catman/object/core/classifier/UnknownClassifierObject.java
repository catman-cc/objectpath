package cc.catman.object.core.classifier;

/**
 * 未知对象
 * @author jpanda
 * @since 0.0.1
 */
public class UnknownClassifierObject extends AbstractClassifierObject {

    public static UnknownClassifierObject create(Object raw) {
        return new UnknownClassifierObject(raw);
    }
    public UnknownClassifierObject(Object raw) {
        super(raw, EObjectClassification.UNKNOWN);
    }
}
