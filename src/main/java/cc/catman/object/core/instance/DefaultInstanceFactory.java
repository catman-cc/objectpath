package cc.catman.object.core.instance;

import cc.catman.object.core.exception.ObjectCreatorRuntimeException;
import cc.catman.object.core.instance.creator.*;
import cc.catman.object.core.instance.creator.array.*;
import cc.catman.object.core.instance.creator.primary.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认实例工厂
 * @author jpanda
 * @since 0.0.3
 */
@Slf4j
public class DefaultInstanceFactory implements InstanceFactory{

    protected List<InstanceCreator> creators;


    public DefaultInstanceFactory() {
        this(new ArrayList<>());
    }

    public DefaultInstanceFactory(List<InstanceCreator> creators) {
        this.creators = creators;
    }

    public static DefaultInstanceFactory defaultInstance(){
        DefaultInstanceFactory factory = new DefaultInstanceFactory();

        // 添加基础类型实例化工具
        factory.add(new ByteInstanceCreator())
                .add(new ShortInstanceCreator())
                .add(new IntInstanceCreator())
                .add(new LongInstanceCreator())
                .add(new DoubleInstanceCreator())
                .add(new FloatInstanceCreator())
                .add(new BooleanInstanceCreator())
                .add(new CharInstanceCreator())
                .add(new StringInstanceCreator())
        ;

        // 添加数组实例化工具
        factory.add(new ByteArrayInstanceCreator())
                .add(new ShortArrayInstanceCreator())
                .add(new IntArrayInstanceCreator())
                .add(new LongArrayInstanceCreator())
                .add(new DoubleArrayInstanceCreator())
                .add(new FloatArrayInstanceCreator())
                .add(new BooleanArrayInstanceCreator())
                .add(new CharArrayInstanceCreator())
                .add(new StringArrayInstanceCreator())
                .add(new ObjectArrayInstanceCreator())
        ;

        // 添加集合实例化工具
        factory.add(new ListInstanceCreator())
                .add(new MapInstanceCreator())
                .add(new SetInstanceCreator())
                .add(new CollectionInstanceCreator())
                .add(new IterableInstanceCreator())
        ;

        // 添加默认对象实例化工具
        factory.add(new ObjectInstanceCreator());

        return factory;
    }

    /**
     * 创建实例对象
     *
     * @param clazz 实例对象类型
     * @param args  实例对象构造参数
     * @return 实例对象
     */
    @Override
    public <T> T create(Class<T> clazz, Object... args) {
        if (clazz==null){
            return null;
        }
        int maxScore=-1;
        InstanceCreator candidate=null;
        for (InstanceCreator creator : creators) {
            int score = creator.score(clazz, args);

            if (log.isTraceEnabled()){
                log.trace("{} score: {},with class:{},args:{}", creator.getClass().getSimpleName(), score,clazz,args);
            }

            if (score > maxScore){
                maxScore = score;
                candidate = creator;
            }
        }
        if (candidate!= null){
            if (log.isTraceEnabled()){
                log.trace("using {} to create instance of '{}'", candidate.getClass().getSimpleName(), clazz.getSimpleName());
            }
            return candidate.create(clazz, args);
        }
        throw new ObjectCreatorRuntimeException(clazz, args);
    }

    public DefaultInstanceFactory add(InstanceCreator creator){
        creators.add(creator);
        return this;
    }

    public DefaultInstanceFactory remove(InstanceCreator creator){
        creators.remove(creator);
        return this;
    }
}
