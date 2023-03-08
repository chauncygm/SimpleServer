package game.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AutoScanModule extends AbstractModule {

    private final Set<Class<?>> bindClasses = new HashSet<>();

    private final String[] acceptPackages;

    private final String[] rejectClasses;

    private final boolean autoLogger;

    public AutoScanModule(String[] acceptPackages, String[] rejectClasses, boolean autoLogger) {
        this.acceptPackages = acceptPackages;
        this.rejectClasses = rejectClasses;
        this.autoLogger = autoLogger;
    }

    @Override
    public void configure() {
        // 自动注入logger
        if (autoLogger) {
            bindListener(Matchers.any(), new LoggingListener());
        }

        ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .acceptPackages(acceptPackages)
                .rejectClasses(rejectClasses)
                .scan();

        // 遍历所有接口类型
        for (ClassInfo ifs : scanResult.getAllInterfaces()) {
            ClassInfoList impl = scanResult.getClassesImplementing(ifs.getName());
            if (Objects.nonNull(impl)) {
                Class<?> ic = ifs.loadClass();

                //多个实现通过名字绑定
                if (impl.size() > 1) {
                    for (ClassInfo im : impl) {
                        Class<?> implClass = im.loadClass();
                        if (isSingleton(implClass)) {
                            String simpleName = im.getSimpleName();
                            String name = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
                            bindNamedSingleInterface(ic, name, implClass);
                        }
                    }

                } else {
                    for (ClassInfo im : impl) {
                        Class<?> implClass = im.loadClass();
                        if (isProvider(implClass)) {
                            bindProvider(ic, implClass);
                        }
                        if (isSingleton(implClass)) {
                            bindSingleInterface(ic, implClass);
                        }
                    }
                }
            }
        }

        // 扫描所有类
        ClassInfoList standardClasses = scanResult.getAllStandardClasses();
        for (ClassInfo ci : standardClasses) {
            Class<?> implClass = ci.loadClass();
            if (!bindClasses.contains(implClass) && shouldBindSingleton(implClass)) {
                bindSingleton(implClass);
            }
        }
        bindClasses.clear();
        ScanResult.closeAll();
    }

    private boolean shouldBindSingleton(Class<?> implClass) {
        int modifiers = implClass.getModifiers();
        return isSingleton(implClass) && !Modifier.isAbstract(modifiers) && !implClass.isEnum();
    }

    private void bindSingleton(Class<?> implClass) {
        bindClasses.add(implClass);
        bind(implClass).in(Scopes.SINGLETON);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void bindSingleInterface(Class<?> ic, Class<?> implClass) {
        bindClasses.add(implClass);
        bind((Class) ic).to(implClass).in(Scopes.SINGLETON);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void bindNamedSingleInterface(Class<?> ic, String name, Class<?> implClass) {
        bindClasses.add(implClass);
        bind((Class) ic).annotatedWith(Names.named(name)).to(implClass).in(Scopes.SINGLETON);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> void bindProvider(Class<?> ic, Class<?> provider) {
        bindClasses.add(provider);
        Type type = ic.getGenericInterfaces()[0];
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class target = (Class) parameterizedType.getActualTypeArguments()[0];
        bind(target).toProvider(provider).in(Scopes.SINGLETON);
    }

    private boolean isSingleton(Class<?> implClass) {
        return Objects.nonNull(implClass) && implClass.isAnnotationPresent(Singleton.class);
    }

    private boolean isProvider(Class<?> implClass) {
        return isSingleton(implClass) && Provider.class.isAssignableFrom(implClass);
    }
}
