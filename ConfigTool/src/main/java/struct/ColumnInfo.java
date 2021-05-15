package struct;

/**
 * @author gongshengjun
 * @date 2021/3/13 15:08
 */
public class ColumnInfo {

    /**
     * 字段名
     */
    private String name;

    /**
     * 注释说明
     */
    private String desc = "";

    /**
     * 字段类型字符串
     */
    private String type;

    /**
     * 字段基本类型
     */
    private BaseType baseType;

    /**
     * 字段包装类型
     */
    private WrapType wrapType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(BaseType baseType) {
        this.baseType = baseType;
    }

    public WrapType getWrapType() {
        return wrapType;
    }

    public void setWrapType(WrapType wrapType) {
        this.wrapType = wrapType;
    }

}
