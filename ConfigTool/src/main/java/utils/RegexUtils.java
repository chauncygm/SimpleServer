package utils;

import java.util.regex.Pattern;

/**
 * @author  gongshengjun
 * @date    2021/3/18 13:52
 */
public class RegexUtils {

    private static final Pattern NUMBER_REG = Pattern.compile("^(-)?(0|[1-9]\\d*)$");
    private static final Pattern FLOAT_REG = Pattern.compile("^(-)?(\\d+)(\\.\\d+)?$");

    public static boolean match(String regex, String source) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(source).matches();
    }

    public static boolean isNumber(String source) {
        return NUMBER_REG.matcher(source).matches();
    }

    public static boolean isFloat(String source) {
        return FLOAT_REG.matcher(source).matches();
    }

}
