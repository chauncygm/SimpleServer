package game.db.beans;

public class Role extends RoleKey {
    private String roleName;

    private Long accountId;

    private String account;

    private Integer lv;

    private Long loginTime;

    private Long logoutTime;

    private Long updateTime;

    private Long deleteTime;

    private Integer createServerId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Long logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getCreateServerId() {
        return createServerId;
    }

    public void setCreateServerId(Integer createServerId) {
        this.createServerId = createServerId;
    }
}