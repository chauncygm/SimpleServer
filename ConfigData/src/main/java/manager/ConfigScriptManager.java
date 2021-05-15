package manager;

import game.utils.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author  gongshengjun
 * @date    2021/4/14 20:36
 */
public class ConfigScriptManager {

    public static final Logger logger = LoggerFactory.getLogger(ConfigScriptManager.class);

    private String javaFilePath;        //java文件存放路径
    private String packageName;         //java文件所在的包名
    private String classpath;           //javac所需的classpath
    private String classFilePath;       //javac输出class路径

    /**
     * 脚本更新时间
     */
    public HashMap<String, Long> updateTimeMap = new HashMap<>();

    public ConfigScriptManager() {
        packageName = "cfg";
        javaFilePath = "config";
        classFilePath = "bin";
        File file = new File(classFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) this.getClass().getClassLoader()).getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        sb.append(classFilePath);
        this.classpath = sb.toString();
    }

    public void init() throws Exception {
        long start = System.currentTimeMillis();
        Set<Class<IConfigScript>> scriptList = ClassUtil.getSubClasses("cfg", IConfigScript.class);
        for (Class<IConfigScript> cls : scriptList) {
            IConfigScript script = cls.newInstance();
            script.load();
        }
        long end = System.currentTimeMillis();
        logger.info("加载配置表数据完成,耗时{}ms.", end - start);

        boolean debug = "true".equals(System.getProperty("ideDebug"));
        if (debug) {
            String userDir = System.getProperty("user.dir");
            String configPath = userDir + "/src/main/java/com/cfg";
            ConfigScriptManager.getInstance().timerCheck(configPath);
        }
    }

    public boolean reloadConfig(String name) {
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir + File.separator + javaFilePath + File.separator + name + ".java" );
        if (!file.isFile() || !file.canRead()) {
            logger.error("不存在可用的配置表文件：{}", name + ".java");
            return false;
        }
        try {
            return reloadConfigScript(file);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

    private void timerCheck(String dirPath) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    File dir = new File(dirPath);
                    if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) {
                        return;
                    }
                    File[] files = dir.listFiles();
                    if (files == null) {
                        return;
                    }
                    for (File f : files) {
                        if (!f.isFile() || !f.canRead() || !f.getName().endsWith("Load.java")) {
                            continue;
                        }
                        Long lastTime = updateTimeMap.putIfAbsent(f.getName(), f.lastModified());
                        if (lastTime == null || lastTime == f.lastModified()) {
                            continue;
                        }
                        reloadConfigScript(f);
                    }
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                }

            }
        }, 1000, 1000);
    }

    private boolean reloadConfigScript(File f) throws Exception {
        Class<?> clazz;
        String className = f.getName().substring(0, f.getName().length() - 5);
        String fullClassName = packageName + "." + className;

        try (InputStream inStream = new FileInputStream(f)) {
            byte[] bytes = new byte[(int) f.length()];
            inStream.read(bytes);
            clazz = ClassUtil.javaCodeToObject(fullClassName, new String(bytes, StandardCharsets.UTF_8), classpath, classFilePath);
        }
        if (clazz == null) {
            logger.error("加载配置表: [{}], 读取class失败, reload失败!", className);
            return false;
        }
        if (!IConfigScript.class.isAssignableFrom(clazz)) {
            return false;
        }
        logger.info("开始加载配置表: [{}]", className);
        Object instance = clazz.newInstance();
        ((IConfigScript)instance).load();
        updateTimeMap.put(f.getName(), f.lastModified());
        logger.info("加载配置表:[{}], reload成功!", className);
        return true;
    }

    private enum Singleton {
        /**
         * 用枚举来实现单例
         */
        INSTANCE;
        ConfigScriptManager processor;

        Singleton() {
            this.processor = new ConfigScriptManager();
        }

        ConfigScriptManager getProcessor() {
            return processor;
        }
    }

    public static ConfigScriptManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

}
