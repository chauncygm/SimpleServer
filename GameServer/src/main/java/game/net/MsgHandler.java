package game.net;

import com.google.protobuf.MessageLite;
import game.net.msg.MessageMappingHolder;
import game.player.Player;
import game.player.PlayerManager;
import game.script.IHandlerScript;
import game.script.ScriptManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author  gongshengjun
 * @date    2021/4/23 16:39
 */
public class MsgHandler extends SimpleChannelInboundHandler<MessageLite> {

    private static final Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private final int nodeType;

    public MsgHandler(int nodeType) {
        super();
        this.nodeType = nodeType;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageLite msg) {
        int messageId = MessageMappingHolder.getMessageId(msg);
        IHandlerScript handlerScript = ScriptManager.proto(messageId);
        if (handlerScript == null) {
            logger.error("未找到协议对应的处理脚本: {}, {}", msg.getClass(), messageId);
            return;
        }
        try {
            handlerScript.doAction(ctx, msg);
        } catch (Exception e) {
            logger.error("协议处理失败", e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("新连接激活: {}", ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        logger.info("新连接注册：{}", ctx);
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
