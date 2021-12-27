package script.common;

import client.cmd.ICmdScript;
import client.manager.ClientManager;
import client.script.ScriptDefine;
import client.struct.Player;
import client.struct.PlayerManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import msg.login.LoginMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author  gongshengjun
 * @date    2021/4/26 14:11
 */
public class ClientCmdScript implements ICmdScript {

    private static final Logger logger = LoggerFactory.getLogger(ClientCmdScript.class);

    @Override
    public int getId() {
        return ScriptDefine.SCRIPT_CMD;
    }

    @Override
    public Object call(Object... objs) {
        String methodStr = (String) objs[0];
        if (methodStr == null || methodStr.trim().isEmpty()) {
            return false;
        }
        if (!methodStr.startsWith("&") || methodStr.length() == 1) {
            logger.error("错误的命令格式：{}", methodStr);
            return false;
        }

        String[] strings = methodStr.split(" ");
        String method = strings[0].substring(1);

        String[] params = null;
        if (strings.length > 1) {
            params = new String[strings.length - 1];
            System.arraycopy(strings, 1, params, 0, params.length);
        }
        MethodType methodType = MethodType.methodType(String.class);
        if (params != null) {
            methodType = MethodType.methodType(String.class, String[].class);
        }

        String result;
        try {
            MethodHandle cmdHandle = MethodHandles.lookup().findVirtual(getClass(), method, methodType);
            if (params == null) {
                result = cmdHandle.invoke(this).toString();
            } else {
                result = cmdHandle.invoke(this, params).toString();
            }
            if (result != null && !result.isEmpty()) {
                logger.info("执行命令【{}】完成，结果：{}", method, result);
            }
            return true;
        } catch (NoSuchMethodException e) {
            logger.error("{}", method + methodType);
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
        return false;
    }

    private String test() {
        return "client test";
    }

    private AtomicInteger atomicInteger = new AtomicInteger();

    private String connect() {
        while (true) {
            Player player = new Player(atomicInteger.incrementAndGet(), ClientManager.SERVER_IP, ClientManager.SERVER_PORT);
            PlayerManager.getInstance().cachePlayer(player);
            boolean result = player.connect();
            logger.info("玩家[{}]连接: {}", player.getId(), result);
            if (atomicInteger.get() > 1000) {
                break;
            }
        }
        return "ok";
    }

    private String addplayer() {
        Player player = new Player(atomicInteger.incrementAndGet(), ClientManager.SERVER_IP, ClientManager.SERVER_PORT);
        PlayerManager.getInstance().cachePlayer(player);
        boolean result = player.connect();
        logger.info("玩家[{}]连接: {}", player.getId(), result);
        return "";
    }

    private String status() {
        ConcurrentHashMap<Long, Player> playerMap = PlayerManager.getInstance().getPlayerMap();
        logger.info("客户端连接数:" + playerMap.size());
        for (Map.Entry<Long, Player> entry : playerMap.entrySet()) {
            Channel channel = entry.getValue().getCtx().channel();
            logger.info("玩家ID: {}, 连接状态,open: {}, active: {}", entry.getKey(), channel.isOpen(), channel.isActive());
        }
        return "ok";
    }

    private String sendmsg() {
        Player player = PlayerManager.getInstance().getPlayer(1);
        logger.info("{}", player);
        LoginMessage.ReqLogin.Builder builder = LoginMessage.ReqLogin.newBuilder();
        builder.setAccount("abc");
        builder.setRoleId(111);
        ChannelHandlerContext ctx = player.getCtx();
        if (ctx.channel().isActive()) {
            player.getCtx().channel().writeAndFlush(builder.build());
        }
        return "";
    }
}
