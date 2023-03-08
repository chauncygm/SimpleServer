package game.script;

import game.utils.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author  gongshengjun
 * @date    2020/11/12 14:49
 */
public abstract class ScriptAbstractManager {

    private static final Logger logger = LoggerFactory.getLogger(ScriptAbstractManager.class);

    /**
     * 所有脚本缓存
     */
    protected final ConcurrentHashMap<Integer, ScriptBean> scripts = new ConcurrentHashMap<>();

    private String classpath;           // javac classpath
    private String javaFilePath;        // .java文件路径
    private String classFilePath;       // javac编译输出路径，内部ClassLoader查找脚本class文件路径
    private String packageName;         // 初始化package下的脚本
    private boolean hasInitialized;     // 初始化标识

    /**
     * 初始化脚本管理器
     *
     * @param javaFilePath  java脚本文件所在目录
     * @param classFilePath 编译后.class所在根目录
     * @param packageName   编译后.class文件所在的包名
     */
    public synchronized void initialize(String javaFilePath, String classFilePath, String packageName) throws Exception {
        boolean debug = "true".equals(System.getProperty("ideDebug"));
        initialize(javaFilePath, classFilePath, packageName, debug);
    }

    /**
     * 初始化脚本管理器
     *
     * @param javaFilePath  java脚本所在目录
     * @param classFilePath java脚本编译后class所在目录
     * @param packageName   初始化package下的脚本
     * @param isDebug       是否是debug版本，debug版本会启动timer，每秒自动编译最新脚本
     */
    public synchronized void initialize(String javaFilePath, String classFilePath, String packageName, boolean isDebug) throws Exception{
        if (!hasInitialized) {
            this.javaFilePath = javaFilePath;
            this.classFilePath = classFilePath;
            this.packageName = packageName;
            buildClassPath();
            initScript(isDebug);
            hasInitialized = true;
            if (isDebug) {
                timerCheckReload();
            }
        }
    }

    private void buildClassPath() {
        File file = new File(classFilePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                logger.error("创建classFilePath失败!");
            }
        }
        StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) this.getClass().getClassLoader()).getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        sb.append(classFilePath);
        this.classpath = sb.toString().replace("%20", " ");
    }

    /**
     * 初始化packageName下的脚本到map中
     *
     * @param isDebug 是否debug模式
     */
    private void initScript(boolean isDebug) throws Exception {
        Set<Class<IScript>> scriptList = ClassUtil.getSubClasses(packageName, IScript.class);
        for (Class<IScript> cls : scriptList) {
            IScript script = cls.newInstance();
            ScriptBean scriptBean = new ScriptBean();
            scriptBean.setId(script.getId())
                    .setName(script.getClass().getName())
                    .setScript(script)
                    .setJavaFileTimestamp(0)
                    .setClassFileTimestamp(System.currentTimeMillis());
            if (!isDebug) {
                //正式版需使用最新的代码编译成的脚本
                String fullJavaFilePath = javaFilePath + "/" + scriptBean.getName().replace('.', '/') + ".java";
                File f = new File(fullJavaFilePath);
                if (f.isFile() && f.canRead()) {
                    if (!makeScript(scriptBean, f, true)) {
                        System.exit(-1);
                    }
                }
            }
            if (scripts.containsKey(scriptBean.getId())) {
                logger.error("脚本id:{}重复, 脚本名1:{}, 脚本名2:{}", scriptBean.getId(), scriptBean.getName(), scripts.get(scriptBean.getId()).getName());
                System.exit(-1);
            }

            scripts.put(scriptBean.getId(), scriptBean);
            logger.info("加载脚本完成, 脚本id: {}, 脚本名: {}", scriptBean.getId(), scriptBean.getName());
        }
    }

    /**
     * 生成脚本数据（scriptBean已设置脚本名）
     */
    private boolean makeScript(ScriptBean scriptBean, File f, boolean isNeedLog) {
        try {
            if (f.isFile() && f.canRead()) {
                Class<?> clazz;
                try (InputStream inStream = new FileInputStream(f)) {
                    byte[] bytes = new byte[(int) f.length()];
                    inStream.read(bytes);
                    String code = new String(bytes, StandardCharsets.UTF_8);
                    clazz = ClassUtil.javaCodeToObject(scriptBean.getName(), code, classpath, classFilePath);
                }
                if (clazz == null) {
                    return false;
                }

                if (!IScript.class.isAssignableFrom(clazz)) {
                    return false;
                }

                IScript script = (IScript) clazz.newInstance();
                scriptBean.setScript(script)
                        .setJavaFileTimestamp(f.lastModified())
                        .setClassFileTimestamp(System.currentTimeMillis());

                return true;

            }
        } catch (Exception e) {
            if (isNeedLog) {
                logger.error("加载脚本失败, 脚本id: {}, scriptName: {}",scriptBean.getId() ,scriptBean.getName(), e);
            }
        }
        return false;
    }

    /**
     * 启动一个timer，每秒自动编译最新脚本，开发版本时才调用
     */
    private void timerCheckReload() {
        new Timer("ScriptCheckReload-Timer").schedule(new TimerTask() {
            private final HashMap<String, ScriptBean> checkScripts = new HashMap<>();

            @Override
            public void run() {
                try {
                    if (!hasInitialized) {
                        return;
                    }
                    if (checkScripts.isEmpty()) {
                        for (Map.Entry<Integer, ScriptBean> entry : scripts.entrySet()) {
                            checkScripts.put(entry.getValue().getName(), entry.getValue());
                        }
                        return;
                    }
                    String baseDir = "./src/main/java/";
                    File dir = new File(baseDir + packageName.replace(".", "/"));
                    checkScript(dir, packageName);
                } catch (Exception e) {
                    logger.error(e.toString(), e);
                }
            }


            /**
             * 检查脚本
             */
            private void checkScript(File dir, String packageName) {
                for (File f : Objects.requireNonNull(dir.listFiles())) {
                    String name = packageName.isEmpty() ? f.getName() : packageName + "." + f.getName();
                    if (f.isDirectory()) {
                        checkScript(f, name);
                        continue;
                    }
                    if (!f.isFile() && !f.canRead()) {
                        continue;
                    }
                    name = name.replace(".java", "");
                    ScriptBean scriptBean = checkScripts.get(name);
                    if (scriptBean == null) {
                        continue;
                    }
                    if (scriptBean.getJavaFileTimestamp() == 0) {
                        scriptBean.setJavaFileTimestamp(f.lastModified());
                        continue;
                    }
                    if (f.lastModified() == scriptBean.getJavaFileTimestamp()) {
                        continue;
                    }
                    logger.warn("加载脚本: {}，脚本id: {}", f.getPath(), scriptBean.getId());
                    if (makeScript(scriptBean, f, false)) {
                        scripts.put(scriptBean.getId(), scriptBean);
                        logger.info("加载脚本成功， 脚本id: {}, 脚本名: {}", scriptBean.getId(), scriptBean.getName());
                    }
                }
            }
        }, 1000, 1000);
    }

    /**
     * 获取脚本
     *
     * @param scriptId 脚本id
     */
    public IScript getScript(int scriptId) {
        try {
            ScriptBean scriptBean = scripts.get(scriptId);
            if (scriptBean == null) {
                throw new Exception("Cannot find scriptBean, scriptId = " + scriptId);
            }
            IScript script = scriptBean.getScript();
            if (script == null) {
                throw new Exception("Cannot find script, scriptId = " + scriptId);
            }
            return script;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    /**
     * 重新加载已存在的脚本
     *
     * @param scriptId 脚本Id
     * @return  加载脚本是否成功
     */
    public synchronized boolean reload(int scriptId) {
        ScriptBean scriptBean = scripts.get(scriptId);
        if (scriptBean == null) {
            logger.info("脚本刷新失败，脚本不存在，脚本id: {}", scriptId);
            return false;
        }
        if (makeScript(scriptBean)) {
            logger.info("重新加载脚本完成, 脚本id: {}, 脚本名: {}", scriptBean.getId(), scriptBean.getName());
            return true;
        }
        logger.info("脚本id: {}, 脚本名: {}, 脚本刷新失败，生成scriptBean失败", scriptBean.getId(), scriptBean.getName());
        return false;
    }

    /**
     * 加载新脚本
     *
     * @param scriptId  脚本id
     * @param className 脚本名
     * @return 加载脚本是否成功
     */
    public synchronized boolean loadNew(int scriptId, String className) {
        if (scripts.containsKey(scriptId)) {
            logger.info("脚本id:{}, 脚本名:{}, 加载新脚本失败，脚本已存在", scriptId, scripts.get(scriptId).getName());
            return false;
        }
        ScriptBean scriptBean = new ScriptBean();
        scriptBean.setName(className);
        if (makeScript(scriptBean)) {
            scripts.put(scriptId, scriptBean);
            logger.info("加载新的脚本完成, 脚本id: {}, 脚本名: {}", scriptBean.getId(), scriptBean.getName());
            return true;
        }
        logger.info("脚本id: {}, 脚本名: {}, 加载新脚本失败，生成scriptBean失败", scriptId, className);
        return false;
    }

    /**
     * 生成脚本数据（scriptBean已设置脚本名）
     */
    private boolean makeScript(ScriptBean scriptBean) {
        try {
            String fullJavaFilePath = javaFilePath + "/" + scriptBean.getName().replace('.', '/') + ".java";
            File f = new File(fullJavaFilePath);
            return makeScript(scriptBean, f, true);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return false;
    }

}
