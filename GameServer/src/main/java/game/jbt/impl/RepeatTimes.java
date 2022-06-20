package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.Status;

/**
 * 重复指定次数
 */
public class RepeatTimes extends Repeat {

    private final int needCount;

    public RepeatTimes(BNode bNode, int remainCount) {
        super(bNode);
        this.needCount = remainCount;
    }

    @Override
    public Status run() {
        if (runCount < needCount) {
            return super.runNode();
        }
        return Status.Success;
    }
}
