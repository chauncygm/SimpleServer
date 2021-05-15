package config;

import lombok.extern.slf4j.Slf4j;
import utils.RegexUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author  gongshengjun
 * @date    2021/3/5 15:50
 */
@Slf4j
public class Config {

    public static final String REGEX = "^(?!\\d*?(\\d)\\d*?\\1)[0-5]{4,6}$";
    public static String ftlPath = System.getProperty("user.dir") + "/src/main/resources/ftl/";
    public static String excelPath;
    public static String columnMarkConfig;
    public static String exportFlag;
    public static int maxColumnExplainRow = 0;
    public static String exportPath;


    static {
        try {
            Properties properties = new Properties();
            properties.load(Config.class.getResourceAsStream("/config.properties"));

            excelPath = properties.get("excel_path").toString();
            columnMarkConfig = properties.get("column_mark_config").toString();
            exportFlag = properties.get("export_flag").toString();
            exportPath = properties.get("export_path").toString();
            if (!RegexUtils.match(REGEX, columnMarkConfig)) {
                log.error("配置错误：column_mark_config");
                System.exit(1);
            }
            for (char r :columnMarkConfig.toCharArray()) {
                maxColumnExplainRow = Math.max(maxColumnExplainRow, Integer.parseInt(r + ""));
            }
            log.info("读取配置参数:" +
                        "\nftl_path:" + ftlPath +
                        "\nexcel_path:" + excelPath +
                        "\ncolumn_mark_config:" + columnMarkConfig +
                        "\nexport_flag:" + exportFlag +
                        "\nexport_path:" + exportPath +
                        "\nmaxColumnExplainRow:" + maxColumnExplainRow);
        } catch (IOException e) {
            log.error("加载配置文件失败\n" + e);
            System.exit(1);
        }

    }



}
