package game.jbt;

/**
 * 节点运行状态
 */
public enum Status {

    /**
     * 运行中
     */
    Running,

    /**
     * 成功
     */
    Success,

    /**
     * 失败
     */
    Failure;

    public static Status inverse(Status status) {
        if (status == Failure) {
            return Success;
        }
        if (status == Success) {
            return Failure;
        }
        return status;
    }

}
