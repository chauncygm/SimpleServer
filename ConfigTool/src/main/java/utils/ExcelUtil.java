package utils;

import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.DecimalFormat;

/**
 * @author by user
 * @date 2022/8/16 17:54
 */
public class ExcelUtil {

    private ExcelUtil() {}

    /**
     * 取得数据表格的字符串值
     *
     * @param cell excel格子
     * @return 字符串内容
     */
    public static String getCellVal(XSSFCell cell) {
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = new DecimalFormat("#.###").format(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                cellValue = cell.getCTCell().getV();
                break;
            default:
                break;
        }
        return cellValue.trim();
    }
}
