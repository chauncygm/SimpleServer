package struct;

/**
 * @author  gongshengjun
 * @date    2021/3/13 15:08
 */
public enum BaseType {
    /**
     * 整数类型，int
     */
    INT("int", "Integer"),
    /**
     * 整数类型，long
     */
    LONG("long", "Long"),
    /**
     * 小数类型, float
     */
    FLOAT("float", "Float");

    /**
     * 基本类型配置值
     */
    String value;

    String wrapValue;

    BaseType(String value, String wrapValue) {
        this.value = value;
        this.wrapValue = wrapValue;
    }

    public String getValue() {
        return value;
    }

    public String getBaseValue() {
        return value;
    }

    public String getWrapValue() {
        return wrapValue;
    }
}
