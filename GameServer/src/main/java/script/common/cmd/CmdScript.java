package script.common.cmd;

import game.script.IScript;
import game.script.ScriptDefine;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author gongshengjun
 * @date 2021/5/15 17:46
 */
public class CmdScript implements IScript {

    private static final Logger logger = LoggerFactory.getLogger(CmdScript.class);

    private static final String CMD_PRIFIX = "#";

    @Override
    public int getId() {
        return ScriptDefine.SCRIPT_CMD;
    }

    @Override
    public Object call(Object... objs) {
        String methodStr = (String) objs[0];
        if (Strings.isBlank(methodStr) || !methodStr.startsWith(CMD_PRIFIX) || methodStr.length() == 1) {
            logger.error("错误的命令格式：{}", methodStr);
            return false;
        }

        String[] strings = methodStr.split(" ");
        String method = strings[0].substring(1);

        String[] params = null;
        if (strings.length > 1) {
            params = new String[strings.length - 1];
            System.arraycopy(strings, 1, params, 0, params.length);
        }
        MethodType methodType = MethodType.methodType(String.class);
        if (params != null) {
            methodType = MethodType.methodType(String.class, String[].class);
        }

        String result;
        try {
            MethodHandle cmdHandle = MethodHandles.lookup().findVirtual(getClass(), method, methodType);
            if (params == null) {
                result = cmdHandle.invoke(this).toString();
            } else {
                result = cmdHandle.invoke(this, params).toString();
            }
            if (result != null && !result.isEmpty()) {
                logger.info("执行命令【{}】完成，结果：{}", method, result);
            }
            return true;
        } catch (NoSuchMethodException e) {
            logger.error("{}", method + methodType);
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
        return false;
    }

    private String test() {

        String s = Long.toHexString(0L);
        logger.info(s);
        logger.info("abc");
        return "server test";
    }


}
