package game.jbt.impl;

import game.jbt.Status;
import game.jbt.SeqBNode;

/**
 * 顺序执行，总是成功
 */
public class DefaultSeq extends SeqBNode {

    @Override
    public Status run() {
        Status status = runChild(curIndex);
        if (Status.Running.equals(status)) {
            return status;
        }
        if (curIndex < childs.size() - 1) {
            curIndex++;
            return Status.Running;
        }
        return Status.Success;
    }

}
