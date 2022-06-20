package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.DecorateBNode;
import game.jbt.Status;

/**
 * 装饰节点，总是失败
 */
public class AlwaysFailure extends DecorateBNode {

    public AlwaysFailure(BNode realNode) {
        super(realNode);
    }

    @Override
    protected Status run() {
        super.runNode();
        return Status.Failure;
    }
}
