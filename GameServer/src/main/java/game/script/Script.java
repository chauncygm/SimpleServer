package game.script;

import script.common.cmd.CmdScript;

import java.util.Objects;

/**
 * @author gongshengjun
 * @date 2021/4/21 15:32
 */
public class Script {

    public static IScript getScript(int scriptId) {
        return Objects.requireNonNull(ScriptManager.logic(scriptId));
    }

    public static CmdScript cmdScript() {
        return (CmdScript) getScript(ScriptDefine.SCRIPT_CMD);
    }


}
