package game.jbt.impl;

import game.jbt.Status;
import game.jbt.SeqBNode;

/**
 * 顺序执行，快速失败
 */
public class FastFailureSeq extends SeqBNode {

    @Override
    public Status run() {
        for (int i = curIndex; i < childs.size(); i++) {
            curIndex = i;
            Status status = runChild(i);
            if (!Status.Success.equals(status)) {
                return status;
            }
        }
        return Status.Success;
    }
}
