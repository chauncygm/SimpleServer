package game.jbt.impl;

import game.jbt.SeqBNode;
import game.jbt.Status;

/**
 * 顺序执行所有，有一个成功即成功
 */
public class OneSuccessSeq extends SeqBNode {

    @Override
    public Status run() {
        Status result = Status.Failure;
        for (int i = curIndex; i < childs.size(); i++) {
            curIndex = i;
            Status status = runChild(i);
            if (Status.Running.equals(status)) {
                return status;
            }
            if (Status.Success.equals(status)) {
                result = Status.Success;
            }
        }
        return result;
    }
}
