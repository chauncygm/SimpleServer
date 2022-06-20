package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.DecorateBNode;
import game.jbt.Status;

/**
 * 装饰节点，总是成功
 */
public class AlwaysSuccess extends DecorateBNode {

    public AlwaysSuccess(BNode realNode) {
        super(realNode);
    }

    @Override
    protected Status run() {
        super.runNode();
        return Status.Success;
    }
}
