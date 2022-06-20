package game.jbt.impl;

import game.jbt.Status;
import game.jbt.SeqBNode;

/**
 * 顺序执行，快速成功
 */
public class FastSuccessSeq extends SeqBNode {

    @Override
    public Status run() {
        for (int i = 0; i < childs.size(); i++) {
            Status status = runChild(i);
            if (!Status.Failure.equals(status)) {
                return status;
            }
        }
        return Status.Failure;
    }
}
