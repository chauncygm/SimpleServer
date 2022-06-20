package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.Status;

/**
 * 重复执行直到失败
 * 默认无最大重试次数
 */
public class RepeatUntilFailure extends Repeat {

    /**
     * 最大重试次数
     */
    private final int maxCount;

    public RepeatUntilFailure(BNode bNode) {
        this(bNode, 0);
    }

    public RepeatUntilFailure(BNode bNode, int remainCount) {
        super(bNode);
        this.maxCount = remainCount;
    }

    @Override
    public Status run() {
        if (maxCount == 0 || runCount < maxCount) {
            Status status = super.runNode();
            if (Status.Failure.equals(status)) {
                return Status.Success;
            }
            return Status.Running;
        }
        return Status.Failure;
    }
}
