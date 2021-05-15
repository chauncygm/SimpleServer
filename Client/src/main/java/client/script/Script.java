package client.script;

import game.script.IScript;
import script.common.ClientCmdScript;

import java.util.Objects;

/**
 * @author  gongshengjun
 * @date    2021/4/21 15:32
 */
public class Script {

    public static IScript getScript(int scriptId) {
        return Objects.requireNonNull(ScriptManager.logic(scriptId));
    }

    public static ClientCmdScript cmdScript() {
        return (ClientCmdScript) getScript(ScriptDefine.SCRIPT_CMD);
    }


}
