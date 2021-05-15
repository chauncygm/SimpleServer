import config.Config;
import task.ExcelReader;
import task.FtlParser;
import lombok.extern.slf4j.Slf4j;
import struct.ExcelInfo;

import java.io.File;
import java.util.*;

/**
 * @author  gongshengjun
 * @date    2021/3/20 11:13
 */

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception{
        File dir = new File(Config.excelPath);
        if (!dir.isDirectory() || !dir.canRead()) {
            log.error("excel目录错误：{}", Config.excelPath);
            return;
        }
        List<ExcelInfo> list = new ArrayList<>();
        File[] files = Objects.requireNonNull(dir.listFiles());
        log.info("============ 开始扫描指定目录：{} ==============", dir.getAbsolutePath());

        long t1 = System.currentTimeMillis();
        ExcelReader.getInstance().load(files, list);
        long t2 = System.currentTimeMillis();
        log.info("============ 加载配置文件完成,共加载{}个文件，共耗时：{}ms ============", list.size(), t2 - t1);


        HashMap<String, Object> root = new HashMap<>();
        root.put("date", new Date());
        for (ExcelInfo info : list) {
            String classPath = Config.exportPath + File.separator
                    + FtlParser.CLASS_PACKAGE.replace(".", "/") + File.separator
                    + "Cfg" + info.getClassName() + ".java";
            String loadClassPath = Config.exportPath + File.separator
                    + FtlParser.CLASS_PACKAGE.replace(".", "/") + File.separator
                    + "Cfg" + info.getClassName() + "Load.java";
            root.put("info", info);
            boolean result1 = FtlParser.getInstance().writeFile(classPath, "class", root);
            log.info("写入{}文件结果: {}", "Cfg" + info.getClassName() + ".java", result1);
            boolean result2 = FtlParser.getInstance().writeFile(loadClassPath, "load", root);
            log.info("写入{}文件结果: {}", "Cfg" + info.getClassName() + "Load.java", result2);
        }
    }
}
