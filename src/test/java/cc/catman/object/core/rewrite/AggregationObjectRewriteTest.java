package cc.catman.object.core.rewrite;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 重写对象
 * @author jpanda
 * @since 0.0.1
 */
public class AggregationObjectRewriteTest {

    @Test
    public void testSimpleRewrite(){
        AggregationObjectRewrite or=new AggregationObjectRewrite();
        or.add(object -> {
            if (object instanceof Date){
                return RewriteObject.of(object,((Date) object).getTime());
            }
            return RewriteObject.of(object);
        }).add(object -> {
            if (object instanceof Long){
                return RewriteObject.of(object,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((Long) object)));
            }
            return RewriteObject.of(object);
        });

        RewriteObject rewrite = or.rewrite(new Date());
        assert rewrite.getRewrite() instanceof String;
    }
}