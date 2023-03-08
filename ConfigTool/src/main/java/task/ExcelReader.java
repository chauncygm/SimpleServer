package task;

import config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import struct.BaseType;
import struct.ColumnInfo;
import struct.ExcelInfo;
import struct.WrapType;
import utils.RegexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

import static utils.ExcelUtil.getCellVal;

/**
 * @author  gongshengjun
 * @date    2021/3/20 10:37
 */
@Slf4j
public class ExcelReader {

    private static final int COLUMN_INFO = 0;
    private static final int COLUMN_DESC = 1;
    private static final int COLUMN_NAME = 2;
    private static final int COLUMN_TYPE = 3;
    private static final int COLUMN_EXPORT_FLAG = 4;

    private static final HashSet<Integer> ID_SET = new HashSet<>();
    private static final HashSet<String> NAME_SET = new HashSet<>();

    /**
     * 读取excel文件配置信息，转为ExcelInfo对象
     *
     * @param files excel文件列表
     * @param list  存放读取信息的ExcelInfo对象列表
     *              读取失败，直接抛异常
     */
    public void load(File[] files, List<ExcelInfo> list) throws Exception{
        Map<Integer, String> idNames = new HashMap<>(100);
        for (File file : files) {
            if (file == null || file.getName().startsWith("~")) {
                continue;
            }
            if (!file.exists() || !file.canRead()) {
                log.error("can't read file：" + file.getAbsolutePath());
                throw new Exception();
            }
            int id = Integer.parseInt(file.getName().split("_")[0]);
            String name = file.getName().split("_")[1];
            if (idNames.containsKey(id) || idNames.containsValue(name)) {
                log.error("repeated id or name：{}, {}", id, name);
                throw new Exception();
            }
            idNames.put(id, name);

            ExcelInfo info = new ExcelInfo(id, name, file.getAbsolutePath());
            long t1 = System.currentTimeMillis();
            String fileName = info.getId() + "_" + info.getName();
            log.info("start resolve excel：{}", fileName);

            if (info.getName().contains("global")) {
                loadGlobal(info);
            } else {
                readExcel(info);
            }

            ID_SET.clear();
            NAME_SET.clear();
            checkImportClass(info);
            list.add(info);

            long t2 = System.currentTimeMillis();
            log.info("resolve {} finish, spend {}ms", fileName, t2 - t1);
        }
    }

    public void loadGlobal(ExcelInfo info) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(info.getPath())));
        if (workbook.getNumberOfSheets() < 1) {
            throw new Exception("excel not exist：" + info.getPath());
        }
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum() + 1;

        List<String> rowData = new ArrayList<>(rows);
        info.getDataInfoList().add(rowData);

        for (int i = 0; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            String exportFlag = getCellVal(row.getCell(3));
            if (!exportFlag.contains(Config.exportFlag)) {
                continue;
            }

            ColumnInfo columnInfo = new ColumnInfo();
            loadColumnInfo(columnInfo, getCellVal(row.getCell(0)), COLUMN_DESC);
            loadColumnInfo(columnInfo, getCellVal(row.getCell(1)), COLUMN_NAME);
            loadColumnInfo(columnInfo, getCellVal(row.getCell(2)), COLUMN_TYPE);
            info.getColumnInfoList().add(columnInfo);

            loadExcelData(rowData, columnInfo, getCellVal(row.getCell(4)));
        }
    }

    /**
     * 读取excel配置信息到ExcelInfo对象
     * 读取失败直接抛异常
     *
     * @param info  配置表对象
     */
    public void readExcel(ExcelInfo info) throws Exception{

        String fileName = info.getId() + "_" + info.getName();
        XSSFWorkbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(info.getPath())));
        if (workbook.getNumberOfSheets() < 1) {
            throw new Exception("excel not exist：" + info.getPath());
        }
        XSSFSheet sheet = workbook.getSheetAt(0);

        //获取需导出列，并添加字段信息
        int markRowNum = Integer.parseInt(Config.columnMarkConfig.charAt(4) + "");
        List<Integer> exportCellList = getExportList(sheet, markRowNum);
        for(Integer ignored : exportCellList) {
            info.getColumnInfoList().add(new ColumnInfo());
        }

        int rows = sheet.getLastRowNum() + 1;
        int cellSize = exportCellList.size();
        if (rows <= Config.maxColumnExplainRow) {
            throw new Exception("字段信息配置信息不完整");
        }

        //按行列读数据
        for (int curRow = 0; curRow < rows; curRow++) {
            if (curRow == markRowNum) {
                continue;
            }

            XSSFRow row = sheet.getRow(curRow);
            boolean isColumnInfo = curRow <= Config.maxColumnExplainRow;

            //检查重复Id，并添加行数据集
            int type = 0;
            List<String> rowData = null;
            if (!isColumnInfo) {
                String cellVal = getCellVal(row.getCell(0));
                if (cellVal.isEmpty()) {
                    break;
                }
                checkIdRepeat(Integer.parseInt(cellVal));
                rowData = new ArrayList<>(cellSize);
                info.getDataInfoList().add(rowData);
            } else {
                type = Config.columnMarkConfig.indexOf("" + curRow);
            }

            //读取每一行的数据
            for (int j = 0; j < cellSize; j++) {
                int cellNum = exportCellList.get(j);
                String content = getCellVal(row.getCell(cellNum));
                try {
                    ColumnInfo columnInfo = info.getColumnInfoList().get(j);
                    if (isColumnInfo) {
                        //加载字段信息
                        loadColumnInfo(columnInfo, content, type);
                    } else {
                        //加载数据信息
                        loadExcelData(rowData, columnInfo, content);
                    }
                } catch (Exception e) {
                    String cellName = (j + 1) / 26 > 0 ? (char)(64 + (j + 1) / 26) + "" + (char)(65 + j % 26) : "" + (char)(65 + j % 26);
                    log.error(String.format("load %s failed, row: %d, col: %s：%s", fileName, curRow + 1, cellName, "\"" + content + "\""), e);
                    throw new Exception();
                }
            }
        }

    }

    /**
     * 获取表格需要导出的列索引列表
     *
     * @param sheet excel表格
     * @return 需导出的列
     */
    private List<Integer> getExportList(XSSFSheet sheet, int markRowNum) {
        XSSFRow markRow = sheet.getRow(markRowNum);
        List<Integer> exportCellList = new ArrayList<>(markRow.getLastCellNum() + 1);

        int markRows = markRow.getLastCellNum();
        for (int i = 0; i < markRows; i++) {
            String cellVal = getCellVal(markRow.getCell(i));
            if (cellVal.contains(Config.exportFlag)) {
                exportCellList.add(i);
            }
        }
        return exportCellList;
    }

    /**
     * 检查id重复
     */
    private void checkIdRepeat(int id) throws Exception{
        if (ID_SET.contains(id)) {
            throw new Exception("repeated id : " + id);
        }
        ID_SET.add(id);
    }

    /**
     * 检查字段名重复
     */
    private void checkNameRepeat(String name) throws Exception{
        if (NAME_SET.contains(name)) {
            throw new Exception("repeated field : " + name);
        }
        NAME_SET.add(name);
    }


    /**
     * 读取字段相关信息
     *
     * @param columnInfo    字段
     * @param content       内容
     * @param type          行类型
     */
    private void loadColumnInfo(ColumnInfo columnInfo, String content, int type) throws Exception{
        switch (type) {
            case COLUMN_INFO:
            case COLUMN_EXPORT_FLAG:
                break;
            case COLUMN_DESC:
                columnInfo.setDesc(content.trim());
                break;
            case COLUMN_NAME:
                checkNameRepeat(content.trim());
                columnInfo.setName(content.trim());
                break;
            case COLUMN_TYPE:
                parseType(columnInfo, content.trim());
                break;
            default:
                throw new Exception("unknown type : " + type);
        }
    }

    /**
     * 解析字段类型信息
     *
     * @param columnInfo    字段信息
     * @param content       字段类型配置
     */
    private void parseType(ColumnInfo columnInfo, String content) throws Exception{
        for (WrapType type : WrapType.values()) {
            if (RegexUtils.match(type.getRegex(), content)) {
                columnInfo.setWrapType(type);
                break;
            }
        }
        for (BaseType type : BaseType.values()) {
            if (content.contains(type.getValue())) {
                columnInfo.setBaseType(type);
                break;
            }
        }
        if (columnInfo.getWrapType() == null || columnInfo.getBaseType() == null) {
            throw new Exception("resolve field type error, type:" + content);
        }

        String baseTypeStr = columnInfo.getBaseType().getWrapValue();
        if (columnInfo.getWrapType() == WrapType.NONE) {
            baseTypeStr = columnInfo.getBaseType().getBaseValue();
        }
        String formatStr = columnInfo.getWrapType().getValue();
        columnInfo.setType(String.format(formatStr, baseTypeStr));
    }

    /**
     * 读取格子内的数据
     *
     * int  float   long    string  int[]       int[][]         map[int, int]       map[int,int[]]
     * 5    10.0    100     abc     5_10_20     5_6_7|8_9_10    1:20|10:40|20:60    1:2_3_4|2:5_6_7
     *
     * @param rowData       行数据
     * @param columnInfo    字段信息
     * @param content       格子内容
     */
    private void loadExcelData(List<String> rowData, ColumnInfo columnInfo, String content) throws Exception{

        if (columnInfo.getWrapType() == null || columnInfo.getBaseType() == null) {
            throw new Exception();
        }

        boolean empty = content.isEmpty();

        String[] split;
        String[] subStr;
        List<String> data = new ArrayList<>();

        //验证包装类型
        switch (columnInfo.getWrapType()) {
            case NONE:
                data.add(empty ? "0" : content);
                break;
            case ARRAY:
                data.addAll(Arrays.asList(content.split("_")));
                break;
            case ARRAY_2:
                if (!empty) {
                    split = content.split("\\|");
                    Arrays.stream(split).forEach(n -> data.addAll(Arrays.asList(n.split("_"))));
                }
                break;
            case MAP:
            case MAP_LIST:
                if (!empty) {
                    split = content.split("\\|");
                    for (String str : split) {
                        subStr = str.split(":");
                        if (Long.parseLong(subStr[0]) > Integer.MAX_VALUE) {
                            throw new Exception("加载数据格式错误, 类型:" + columnInfo.getWrapType().name());
                        }
                        if (columnInfo.getWrapType() == WrapType.MAP) {
                            data.add(subStr[1]);
                        } else {
                            data.addAll(Arrays.asList(subStr[1].split("_")));
                        }
                    }
                }
                break;
            default: break;
        }

        //验证基本类型数据的配置
        boolean isLong = columnInfo.getBaseType() == BaseType.LONG;
        for(String s : data) {
            if (s.isEmpty()) {
                continue;
            }
            double fNum = Double.parseDouble(s);
            if (isLong && fNum > Integer.MAX_VALUE) {
                throw new Exception("加载数据格式错误, 类型:" + columnInfo.getBaseType().name());
            }
        }
        if (columnInfo.getWrapType() == WrapType.NONE) {
            boolean isDecimals = columnInfo.getBaseType() == BaseType.FLOAT;
            content = content.isEmpty() ? "0" : content;
            content += isDecimals ? "f" : "";
            content += isLong ? "L" : "";
        }
        rowData.add(content);
    }

    /**
     * 检查需要导入的类
     */
    private void checkImportClass(ExcelInfo excelInfo) {
        for (ColumnInfo info : excelInfo.getColumnInfoList()) {
            String s1 = info.getBaseType().getWrapValue(), s2 = "";
            switch (info.getWrapType()) {
                case ARRAY:
                case MAP_LIST: s2 = "ReadArray";break;
                case ARRAY_2: s2 = "ReadArrayEs";break;
                default: break;
            }
            if (!s2.isEmpty()) {
                excelInfo.getImportClass().add(String.format("%s%s", s1, s2));
            }
        }
    }

    private ExcelReader() {}

    public enum Singleton {
        /**
         * 枚举单例
         */
        INSTANCE;
        ExcelReader reader;

        Singleton() {
            this.reader = new ExcelReader();
        }

        public ExcelReader getReader() {
            return reader;
        }
    }

    public static ExcelReader getInstance() {
        return Singleton.INSTANCE.getReader();
    }
}
