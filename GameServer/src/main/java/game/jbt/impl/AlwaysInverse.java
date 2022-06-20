package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.DecorateBNode;
import game.jbt.Status;

/**
 * 装饰节点，总是反转结果
 */
public class AlwaysInverse extends DecorateBNode {

    public AlwaysInverse(BNode realNode) {
        super(realNode);
    }

    @Override
    protected Status run() {
        return Status.inverse(super.runNode());
    }
}
