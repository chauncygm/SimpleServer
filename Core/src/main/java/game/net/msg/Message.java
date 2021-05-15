package game.net.msg;

public class Message {
    /**
     * 消息id，必须唯一
     */
    private int messageId;
    /**
     * java包名
     */
    private String javaPackageName;
    /**
     * java外部类名字
     */
    private String javaOuterClassName;
    /**
     * 消息名(类简单名)
     * {@link Class#getSimpleName()}
     */
    private String messageName;

    public Message(int messageId, String javaPackageName, String javaOuterClassName, String messageName) {
        this.messageId = messageId;
        this.javaPackageName = javaPackageName;
        this.javaOuterClassName = javaOuterClassName;
        this.messageName = messageName;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getJavaPackageName() {
        return javaPackageName;
    }

    public void setJavaPackageName(String javaPackageName) {
        this.javaPackageName = javaPackageName;
    }

    public String getJavaOuterClassName() {
        return javaOuterClassName;
    }

    public void setJavaOuterClassName(String javaOuterClassName) {
        this.javaOuterClassName = javaOuterClassName;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", javaPackageName='" + javaPackageName + '\'' +
                ", javaOuterClassName='" + javaOuterClassName + '\'' +
                ", messageName='" + messageName + '\'' +
                '}';
    }
}
