package game.jbt.impl;

import game.jbt.Status;
import game.jbt.SeqBNode;

/**
 * 顺序执行，所有成功才算成功
 */
public class AllSuccessSeq extends SeqBNode {

    @Override
    public Status run() {
        Status status = runChild(curIndex);
        if (Status.Running.equals(status)) {
            return status;
        }
        if (Status.Failure.equals(status)) {
            return status;
        }
        if (curIndex < childs.size() - 1) {
            curIndex++;
            return Status.Running;
        }
        return status;
    }
}
