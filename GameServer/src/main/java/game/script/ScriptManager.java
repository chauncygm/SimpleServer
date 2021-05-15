package game.script;

/**
 * @author  gongshengjun
 * @date    2021/4/20 9:57
 */
public enum ScriptManager {

    LOGIC("script/common", "bin", "script.common"),
    HANDLER("script/handler", "bin", "script.handler");

    private ScriptLoad loader;

    ScriptManager(String javaFilePath, String classFilePath, String packageName) {
        this.loader = new ScriptLoad(javaFilePath, classFilePath, packageName);
    }

    public static void init() {}

    public ScriptLoad getLoader() {
        return loader;
    }

    public static IScript logic(int scriptId) {
        return LOGIC.getLoader().getScript(scriptId);
    }

    public static IHandlerScript proto(int scriptId) {
        IScript script = HANDLER.getLoader().getScript(scriptId);
        if (script instanceof IHandlerScript) {
            return (IHandlerScript) script;
        }
        return null;
    }
}
