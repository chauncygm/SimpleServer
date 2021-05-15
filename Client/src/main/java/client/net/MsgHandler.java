package client.net;

import client.struct.Player;
import client.struct.PlayerManager;
import com.google.protobuf.MessageLite;
import game.net.NodeDefine;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  gongshengjun
 * @date    2021/4/23 16:39
 */
public class MsgHandler extends SimpleChannelInboundHandler<MessageLite> {

    private static final Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    private final int nodeType;

    private final long uid;

    public MsgHandler(int nodeType, long uid) {
        super();
        this.nodeType = nodeType;
        this.uid = uid;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageLite msg) {
        logger.info(msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (nodeType != NodeDefine.CLIENT) return;
        Player player = PlayerManager.getInstance().getPlayer(uid);
        if (player == null) {
            logger.info("玩家不存在, uid: {}", uid);
            return;
        }
        player.setCtx(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        logger.info("连接注销：{}", ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("连接异常：{}, {}", ctx, cause.getMessage());
    }
}
