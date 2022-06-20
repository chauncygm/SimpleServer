package game.jbt;

import java.util.List;

/**
 * 分支选择节点
 */
public abstract class SelectorBNode extends BNode {

    /**
     * 选择结果
     */
    protected List<Integer> chooseList;

    /**
     * 当前执行的节点
     */
    protected int curIndex;

    /**
     * 选择策略接口
     */
    protected abstract List<Integer> chooseStrategy();

    protected void preChoose() {
        if (chooseList == null) {
            chooseList = chooseStrategy();
        }
        if (chooseList == null) {
            throw new NullPointerException("chooseList is null");
        }
    }
}
