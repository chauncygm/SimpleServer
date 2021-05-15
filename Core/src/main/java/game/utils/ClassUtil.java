package game.utils;

import game.script.JavaSourceFromString;
import game.script.ScriptClassLoader;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author  gongshengjun
 * @date    2020/11/11 16:31
 */
public class ClassUtil {

    /**
     * @param name          文件名
     * @param code          文件数据
     * @param classpath     javac classpath
     * @param classFilePath javac编译输出路径，内部ClassLoader查找脚本class文件路径
     */
    public static Class<?> javaCodeToObject(String name, String code, String classpath, String classFilePath) throws Exception {

        Class<?> c = null;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException ignored) {

        }
        boolean reload = (c != null);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        ScriptClassLoader loader = new ScriptClassLoader();
        boolean success;
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            JavaFileObject jFile = new JavaSourceFromString(name, code);
            List<JavaFileObject> jFiles = new ArrayList<>();
            jFiles.add(jFile);
            List<String> options = new ArrayList<>();
            options.add("-encoding");
            options.add("UTF-8");
            options.add("-classpath");
            options.add(classpath);
            options.add("-d");
            // javac编译结果输出到classFilePath目录中
            options.add(classFilePath);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jFiles);
            success = task.call();
        }

        if (!success) {
            return null;
        }

        if (!reload) {
            return Class.forName(name);
        }
        return loader.loadScriptClass(classFilePath, name);
    }

    /**
     * 从包package中获取所有父类型的子类Class
     *
     * @param packageName   包名
     * @param parentClass   父类型
     */
    public static <T> Set<Class<T>> getSubClasses(String packageName, Class<T> parentClass) throws IOException, ClassNotFoundException {

        Set<Class<T>> classes = new LinkedHashSet<>();
        String packageDirName = packageName.replace('.', '/');

        //获取包目录下所有的文件资源
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if (protocol == null) {
                continue;
            }
            switch (protocol) {
                case "file":
                    //获取包的物理路径，以文件的方式扫描整个包下的文件 并添加到集合中
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, classes, parentClass);
                    break;
                case "jar":
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        //如果是以/开头的，获取后面的字符串
                        if (name.charAt(0) == '/') {
                            name = name.substring(1);
                        }
                        //如果前半部分和定义的包名相同
                        if (name.startsWith(packageDirName)) {

                            //如果以"/"结尾 是一个包，获取包名 把"/"替换成"."
                            int idx = name.lastIndexOf('/');
                            if (idx != -1) {
                                packageName = name.substring(0, idx).replace('/', '.');
                            }

                            //如果是一个.class文件 而且不是目录
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                //去掉后面的".class" 获取真正的类名
                                String className = name.substring(packageName.length() + 1, name.length() - 6);
                                Class<T> loadClass = getClassByName(packageName + '.' + className, parentClass);
                                if (loadClass != null) {
                                    classes.add(loadClass);
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName   当前包名
     * @param packagePath   当前包路径
     * @param classes       保存加载类的集合
     * @param parentClass   父类型
     */
    public static <T> void findAndAddClassesInPackageByFile(String packageName, String packagePath, Set<Class<T>> classes, Class<T> parentClass) throws ClassNotFoundException {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 获取目录下所有的包目录和以.class结尾的文件(编译好的java类文件)
        File[] dirFiles = dir.listFiles(file -> file.isDirectory() || (file.getName().endsWith(".class")));
        for (File file : Objects.requireNonNull(dirFiles)) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                String filePackageName = packageName + "." + file.getName();
                findAndAddClassesInPackageByFile(filePackageName, file.getAbsolutePath(), classes, parentClass);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                /*
                 * 这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                 * classes.add(Class.forName(packageName + '.' + className));
                 */
                Class<T> loadClass = getClassByName(packageName + '.' + className, parentClass);
                if (loadClass != null) {
                    classes.add(loadClass);
                }
            }
        }
    }

    private static <T> Class<T> getClassByName(String fullClassName, Class<T> parentClass) throws ClassNotFoundException {
        Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
        if (parentClass.isAssignableFrom(loadClass) && !parentClass.equals(loadClass)) {
            //noinspection unchecked
            return (Class<T>) loadClass;
        }
        return null;
    }

}
