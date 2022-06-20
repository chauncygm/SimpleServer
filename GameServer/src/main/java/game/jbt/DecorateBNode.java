package game.jbt;

/**
 * 装饰节点
 */
public abstract class DecorateBNode extends BNode {

    protected BNode realNode;

    public DecorateBNode(BNode realNode) {
        this.realNode = realNode;
    }

    protected Status runNode() {
        return realNode.run();
    }

    @Override
    public void setOwner(Object owner) {
        super.setOwner(owner);
        realNode.setOwner(owner);
    }
}
