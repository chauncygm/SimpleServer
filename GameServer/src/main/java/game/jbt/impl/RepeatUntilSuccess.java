package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.Status;
import game.jbt.DecorateBNode;

/**
 * 重复执行直到成功
 * 默认无最大重试次数
 */
public class RepeatUntilSuccess extends Repeat {

    /**
     * 最大重试次数
     */
    private final int maxCount;

    public RepeatUntilSuccess(BNode bNode) {
        this(bNode, 0);
    }

    public RepeatUntilSuccess(BNode bNode, int remainCount) {
        super(bNode);
        this.maxCount = remainCount;
    }

    @Override
    public Status run() {
        if (maxCount == 0 || runCount < maxCount) {
            Status status = super.runNode();
            if (Status.Success.equals(status)) {
                return status;
            }
            return Status.Running;
        }
        return Status.Failure;
    }
}
