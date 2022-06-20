package game.jbt;

/**
 * 条件节点
 */
public abstract class ConditionBNode extends BNode {

    /**
     * 测试条件
     */
    protected abstract boolean checkCondition();

    @Override
    protected Status run() {
        return checkCondition() ? Status.Success : Status.Failure;
    }
}
