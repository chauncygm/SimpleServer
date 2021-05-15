package struct;

/**
 * @author  gongshengjun
 * @date    2021/3/13 15:08
 */
public enum WrapType {

    /**
     * 无
     */
    NONE("^(int|float|long|string)$", "%s"),
    /**
     * 数组类型
     */
    ARRAY("^(int|float|long|string)\\[\\]$","%sReadArray"),
    /**
     * 二维数组类型
     */
    ARRAY_2("^(int|float|long|string)\\[\\]\\[\\]$", "%sReadArrayEs"),
    /**
     * 键值类型
     */
    MAP("^map\\[int,(int|float|long|string)\\]$", "Map<Integer, %s>"),
    /**
     * 键值数组类型
     */
    MAP_LIST("^map\\[int,(int|float|long|string)\\[\\]\\]$", "Map<Integer, %sReadArray>");

    /**
     * 类型正则匹配规则
     */
    private final String regex;

    /**
     * 类型格式
     */
    private final String value;

    WrapType(String regex, String format) {
        this.regex = regex;
        this.value = format;
    }

    public String getRegex() {
        return regex;
    }

    public String getValue() {
        return value;
    }
}
