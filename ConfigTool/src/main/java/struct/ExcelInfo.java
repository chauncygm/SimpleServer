package struct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author  gongshengjun
 * @date    2021/3/13 15:05
 */
public class ExcelInfo {

    /**
     * 配置表Id
     */
    private int id;

    /**
     * 名字
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 字段列表
     */
    private List<ColumnInfo> columnInfoList = new ArrayList<>(20);

    /**
     * 配置数据
     */
    private List<List<String>> dataInfoList = new ArrayList<>(100);

    /**
     * 导入的类
     */
    private Set<String> importClass = new HashSet<>();

    public ExcelInfo(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }

    public List<List<String>> getDataInfoList() {
        return dataInfoList;
    }

    public void setDataInfoList(List<List<String>> dataInfoList) {
        this.dataInfoList = dataInfoList;
    }

    public Set<String> getImportClass() {
        return importClass;
    }

    public void setImportClass(Set<String> importClass) {
        this.importClass = importClass;
    }

    public String getClassName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
