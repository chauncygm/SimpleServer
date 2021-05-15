package client.cmd;

import client.script.Script;
import client.script.ScriptDefine;
import game.thread.ConsoleThread;

/**
 * @author  gongshengjun
 * @date    2021/4/26 14:04
 */
public class CmdManager extends ConsoleThread {

    private CmdManager() {}

    enum Singleton {
        /**
         * 枚举实现单例
         */
        INSTANCE;
        CmdManager processor;

        Singleton() {
            this.processor = new CmdManager();
        }

        public CmdManager getProcessor() {
            return processor;
        }
    }

    public static CmdManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    @Override
    public boolean dealOrder(String order) {
        if (order == null || order.isEmpty()) {
            return false;
        }
        return (boolean) Script.getScript(ScriptDefine.SCRIPT_CMD).call(order);
    }
}
