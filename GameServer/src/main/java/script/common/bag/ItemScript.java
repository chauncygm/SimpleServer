package script.common.bag;

import game.logic.bag.IItemScript;
import game.script.ScriptDefine;

/**
 * @author gongshengjun
 * @date 2021/5/15 17:50
 */
public class ItemScript implements IItemScript {

    @Override
    public int getId() {
        return ScriptDefine.SCRIPT_ITEM;
    }

    @Override
    public Object call(Object... objs) {
        return null;
    }

}
