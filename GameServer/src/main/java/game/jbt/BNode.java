package game.jbt;

import java.util.ArrayList;

public abstract class BNode {

    /**
     * 拥有者
     */
    private Object owner;

    /**
     * 运行此节点的必要测试条件
     */
    private ConditionBNode guard;

    /**
     * 父节点
     */
    private BNode parent;

    /**
     * 子节点列表
     */
    protected ArrayList<BNode> childs;

    /**
     * 每帧更新节点状态
     */
    protected abstract Status run();

    protected Status checkGuard() {
        return guard.run();
    }

    protected Status runChild(int index) {
        assert childs != null : "run child but it is null";
        if (index < 0 || index >= childs.size()) {
            throw new IllegalArgumentException("index out of bounds : " + index + " >= " + childs.size());
        }
        return childs.get(index).run();
    }

    private boolean isFinish(Status status) {
        return Status.Success == status || Status.Failure == status;
    }

    public BNode addChild(BNode node) {
        if (childs == null) {
            childs = new ArrayList<>(4);
        }
        node.parent = this;
        childs.add(node);
        if (owner != null) {
            node.setOwner(owner);
        }
        return this;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public ConditionBNode getGuard() {
        return guard;
    }

    public void setGuard(ConditionBNode guard) {
        this.guard = guard;
    }

    /**
     * 是否是叶节点/行为节点
     */
    public boolean isLeftNode() {
        return this instanceof LeafBNode;
    }

}
