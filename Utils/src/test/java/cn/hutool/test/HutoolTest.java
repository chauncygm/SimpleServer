package cn.hutool.test;

import cn.hutool.Hutool;
import cn.hutool.system.SystemUtil;
import org.junit.Test;

/**
 * @author by user
 * @description TODO
 * @date 2022/7/8 10:16
 */
public class HutoolTest {

    @Test
    public void testSystemUtil() {
//        Hutool.printAllUtils();
        SystemUtil.dumpSystemInfo();
    }

    @Test
    public void testSystemProperty() {
        String property = System.getProperty("java.class.path");
        System.out.println(property);
    }
}
