package net;

import java.util.regex.Pattern;

/**
 * @author  gongshengjun
 * @date    2020/12/25 17:18
 */
public class ClientT {

    private static final String st = "^(?!.*?([0-9]).*?\\\\1)([0-9]([0-9])+)$";
    private static final String test = "02234";

    public static void main(String[] args) {

        Pattern pattern = Pattern.compile(st);
        boolean flag = pattern.matcher(test).matches();
        System.out.println("匹配结果：" + flag);
    }
}
