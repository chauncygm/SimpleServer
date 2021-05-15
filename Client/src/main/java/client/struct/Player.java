package client.struct;

import client.net.ClientInitializerImpl;
import game.net.NettyClient;
import game.net.NodeDefine;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  gongshengjun
 * @date    2021/4/26 11:47
 */
public class Player extends NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    private long id;

    private String ip;

    private int port;

    private ChannelHandlerContext ctx;

    public Player(long id, String ip, int port) {
        super("player_" + id, ip, port, new ClientInitializerImpl(NodeDefine.CLIENT, id));
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", ctx=" + ctx +
                '}';
    }

    @Override
    public boolean connect() {
        boolean success = super.connect();
        if (success) {
            logger.info("player connect server success, id: {}", id);
            PlayerManager.getInstance().getPlayerMap().put(id, this);
            return true;
        }
        logger.error("player connect server failed, id: {}", id);
        return false;
    }
}
