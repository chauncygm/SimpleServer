package game.player;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author  gongshengjun
 * @date    2021/4/21 15:45
 */
public class Player {

    private long uid;

    private RoleBaseInfo baseInfo;

    private AccountInfo accountInfo;

    private ChannelHandlerContext ctx;

    private int lv;

    public Player(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public RoleBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(RoleBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    @Override
    public String toString() {
        return "Player{" +
                "uid=" + uid +
                ", baseInfo=" + baseInfo +
                ", accountInfo=" + accountInfo +
                ", lv=" + lv +
                '}';
    }
}
