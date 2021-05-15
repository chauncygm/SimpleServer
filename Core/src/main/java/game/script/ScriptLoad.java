package game.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  gongshengjun
 * @date    2021/4/20 10:35
 */
public class ScriptLoad extends ScriptAbstractManager{

    private static final Logger logger = LoggerFactory.getLogger(ScriptLoad.class);

    private String javaFilePath;
    private String classFilePath;
    private String packageName;

    public ScriptLoad(String javaFilePath, String classFilePath, String packageName) {
        this.javaFilePath = javaFilePath;
        this.classFilePath = classFilePath;
        this.packageName = packageName;
        try {
            super.initialize(javaFilePath, classFilePath, packageName);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            System.exit(-1);
        }
    }
}
