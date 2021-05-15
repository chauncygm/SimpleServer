package game.script;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Objects;

/**
 * @author  gongshengjun
 * @date    2020/11/12 14:49
 */
public class ScriptClassLoader extends ClassLoader {

    private static final Logger logger = LogManager.getLogger(ScriptClassLoader.class);

    /**
     * 脚本字节码加载器
     * 为了实现重新加载功能, 此处需打破JVM类加载器双亲委托模型,
     * 否则始终由AppClassLoader加载将无法支持重新加载功能
     *
     * @param classFilePath 路径
     * @param className     类全名
     */
    public Class<?> loadScriptClass(String classFilePath, String className) throws IOException {
        int index = className.lastIndexOf('.');
        if (index < 0) {
            return null;
        }

        String name = className.substring(index + 1);
        String dir = classFilePath + "/" + className.substring(0, index).replace('.', '/') + File.separator;

        File mainF = new File(dir + name + ".class");
        if (!mainF.exists()) {
            return null;
        }

        File dirF = new File(dir);
        if (!dirF.exists()) {
            return null;
        }

        for (File f : Objects.requireNonNull(dirF.listFiles())) {
            if (!f.exists() || !f.isFile() || !f.canRead()) {
                continue;
            }

            String fName = f.getName();
            int in = fName.indexOf('$');
            if (in < 0) {
                continue;
            }
            String prefix = fName.substring(0, in);
            if (!prefix.equals(name)) {
                continue;
            }

            String suffix = fName.substring(in);
            int i = suffix.indexOf('.');
            String cn = className + suffix.substring(0, i);
            byte[] bytes = loadClassData(f);
            this.defineClass(cn, bytes, 0, bytes.length);
            logger.info("热加载:" + name + " 脚本发现内部类(已加载): " + fName);
        }

        // 加载主class
        byte[] bytes = loadClassData(mainF);
        return this.defineClass(className, bytes, 0, bytes.length);
    }

    /**
     * 读取字节码
     */
    private byte[] loadClassData(File file) throws IOException {
        int readCount;
        try (FileInputStream in = new FileInputStream(file);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            while ((readCount = in.read()) != -1) {
                buffer.write(readCount);
            }
            return buffer.toByteArray();
        }
    }
}
