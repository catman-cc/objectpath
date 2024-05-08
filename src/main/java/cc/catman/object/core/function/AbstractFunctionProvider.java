package cc.catman.object.core.function;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 抽象函数提供者
 *
 * @author jpanda
 * @since 0.0.1
 */
@Slf4j
public abstract class AbstractFunctionProvider implements FunctionProvider {
    /**
     * 函数名称
     */
    private final String name;
    /**
     * 函数描述
     */
    private final String desc;
    /**
     * 函数参数描述
     */
    private final List<List<FunctionArgDesc>> args;
    /**
     * 函数返回值描述
     */
    private final FunctionResultDesc result;


    public AbstractFunctionProvider(String name) {
        this(name, "", new ArrayList<>());
    }

    public AbstractFunctionProvider(String name, FunctionResultDesc result) {
        this(name, "", new ArrayList<>(), result);
    }

    public AbstractFunctionProvider(String name, List<List<FunctionArgDesc>> args) {
        this(name, "", args);
    }

    public AbstractFunctionProvider(String name, List<List<FunctionArgDesc>> args, FunctionResultDesc result) {
        this(name, "", args, result);
    }

    public AbstractFunctionProvider(String name, String desc) {
        this(name, desc, new ArrayList<>());
    }

    public AbstractFunctionProvider(String name, String desc, List<List<FunctionArgDesc>> args) {
        this(name, desc, args, FunctionResultDesc.builder()
                .type("void")
                .desc("")
                .build());
    }

    public AbstractFunctionProvider(String name, String desc, List<List<FunctionArgDesc>> args, FunctionResultDesc result) {
        this.name = name;
        this.desc = desc;
        this.args = args;
        this.result = result;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    @Override
    public List<List<FunctionArgDesc>> args() {
        return this.args;
    }

    @Override
    public FunctionResultDesc result() {
        return this.result;
    }

    @Override
    public boolean isMatch(String name, List<Object> params) {
        if (name.equals(this.name)) {
            // 从多组参数中找到一个匹配的参数
            if (args.stream().anyMatch(argDescList -> doCheckParams(argDescList, params))) {
                return this.doCheckMatch(params);
            }
        }
        return false;
    }

    protected boolean doCheckParams(List<FunctionArgDesc> args, List<Object> params) {
        if (args.size() != params.size()) {
            return false;
        }
        for (int i = 0; i < args.size(); i++) {
            if (Optional.ofNullable(params.get(i)).isPresent()) {
                if (!args.get(i).getType().isAssignableFrom(params.get(i).getClass())) {
                    return false;
                }
            } else {
                if (args.get(i).isRequired()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Object apply(List<Object> params) {
        return doApply(params);
    }

    protected abstract Object doApply(List<Object> params);

    private boolean doCheckMatch(List<Object> params) {
        return true;
    }
}
