package task;

import config.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author  gongshengjun
 * @date    2021/3/5 15:59
 */
@Slf4j
public class FtlParser {

    public static final String CLASS_PACKAGE = "cfg";

    private Configuration config = null;

    private FtlParser() {
        try {
            File file = new File(Config.ftlPath);
            System.out.println(file.getAbsolutePath());
            config = new Configuration(Configuration.VERSION_2_3_31);
            config.setDirectoryForTemplateLoading(file);
            config.setDefaultEncoding("UTF-8");
        } catch (IOException e) {
            log.error("加载模板失败", e);
            e.printStackTrace();
        }
    }

    public boolean writeFile(String filePath, String name, HashMap<String, Object> root) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        Template test = config.getTemplate(name + ".ftl");
        test.process(root, writer);

        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            fos.write(writer.getBuffer().toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (Exception e) {
            log.error(e.toString(), e);
            return false;
        }
        return true;
    }

    public enum Singleton {
        /**
         * 枚举单例
         */
        INSTANCE;
        FtlParser parser;

        Singleton() {
            this.parser = new FtlParser();
        }

        public FtlParser getParser() {
            return parser;
        }
    }

    public static FtlParser getInstance() {
        return FtlParser.Singleton.INSTANCE.getParser();
    }
}
