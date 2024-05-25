package cc.catman.object.core.function;

import cc.catman.object.ObjectPathConfiguration;

import java.util.*;

/**
 * 函数管理器
 * @author jpanda
 * @since 0.0.1
 */
public class DefaultFunctionManager implements FunctionManager{
    /**
     * 函数提供者
     */
    private final Map<String,List<FunctionProvider>> providers;

    private ObjectPathConfiguration configuration;

    public DefaultFunctionManager() {
        this(new HashMap<>());
    }

    public DefaultFunctionManager(Map<String, List<FunctionProvider>> providers) {
        this.providers = providers;
    }

    @Override
    public void register(FunctionProvider provider) {
        Optional.ofNullable(this.configuration).ifPresent(c->c.inject(provider));
        List<FunctionProvider> functionProviders = providers.computeIfAbsent(provider.name(), k -> new ArrayList<>());
        functionProviders.add(provider);
    }

    @Override
    public List<FunctionProvider> getProvider(String name) {
        return providers.getOrDefault(name, Collections.emptyList());
    }

    @Override
    public FunctionProvider getProvider(String name, List<Object> params) {
        List<FunctionProvider> ps = this.getProvider(name);
        for (FunctionProvider p : ps) {
            if (p.isMatch(name, params)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean contains(String name) {
        return providers.containsKey(name);
    }

    @Override
    public List<FunctionProvider> providers() {
        return providers.values().stream().reduce((a, b) -> {
            a.addAll(b);
            return a;
        }).orElse(Collections.emptyList());
    }

    @Override
    public List<String> names() {
        return new ArrayList<>(providers.keySet());
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
        this.providers.forEach((k,v)-> Optional.ofNullable(v)
                        .ifPresent(ps-> ps
                                .forEach(p->this.configuration.inject(v))
                        )
                );
    }
}
