package game.player;

/**
 * @author  gongshengjun
 * @date    2021/4/26 16:13
 */
public class RoleBaseInfo {

    /**
     * 角色id
     */
    private long uid;

    /**
     * 角色名
     */
    private String name;

    /**
     * 上次登录时间
     */
    private long loginTime;

    /**
     * 下线时间
     */
    private long logoutTime;

    /**
     * 角色删除时间
     */
    private long deleteTime;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(long logoutTime) {
        this.logoutTime = logoutTime;
    }

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }
}
