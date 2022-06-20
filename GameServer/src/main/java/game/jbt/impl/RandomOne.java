package game.jbt.impl;

import cn.hutool.core.util.RandomUtil;
import game.jbt.SelectorBNode;
import game.jbt.Status;

import java.util.Collections;
import java.util.List;

/**
 * 分支选择节点
 */
public class RandomOne extends SelectorBNode {

    @Override
    public List<Integer> chooseStrategy() {
        int choose = RandomUtil.randomInt(childs.size());
        curIndex = choose;
        return Collections.singletonList(choose);
    }

    @Override
    protected Status run() {
        preChoose();
        return runChild(curIndex);
    }
}
