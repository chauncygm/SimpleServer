package game.jbt.impl;

import game.jbt.BNode;
import game.jbt.DecorateBNode;
import game.jbt.Status;

/**
 * 无限重复节点
 */
public class Repeat extends DecorateBNode {

    protected long runCount;

    public Repeat(BNode realNode) {
        super(realNode);
    }

    @Override
    protected Status runNode() {
        runCount++;
        return super.runNode();
    }

    @Override
    protected Status run() {
        runNode();
        return Status.Running;
    }
}
