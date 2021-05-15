package game.script;

/**
 * @author  gongshengjun
 * @date    2020/11/12 14:49
 */
public interface IScript {

    /**
     * 获取脚本id
     */
    int getId();

    /**
     * 调用脚本
     */
    Object call(Object... objs);

    default void reloadSuccessCallBack() {}

    default void loadNewSuccessCallBack() {}
}
