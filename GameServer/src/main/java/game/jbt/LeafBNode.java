package game.jbt;

/**
 * 叶子节点，即行为节点
 */
public abstract class LeafBNode extends BNode {

    @Override
    public BNode addChild(BNode node) {
        throw new UnsupportedOperationException("Leaf node can't add child");
    }

}

