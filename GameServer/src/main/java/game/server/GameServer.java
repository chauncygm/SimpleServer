package game.server;

import game.net.ChannelInitializerImpl;
import game.net.NettyServer;
import game.net.NodeDefine;
import game.net.msg.MessageMappingHolder;
import game.script.ScriptManager;
import game.thread.CmdManager;
import msg.MsgDefine;

/**
 * @author  gongshengjun
 * @date    2020/12/24 17:01
 */
public class GameServer {

    private NettyServer nettyServer;

    public void start() {
        //初始化脚本管理器
        ScriptManager.init();
        //注册协议处理器
        MessageMappingHolder.registerMsgDict(MsgDefine.allMsg());
        //控制台命令处理线程
        CmdManager.getInstance().start();

        ChannelInitializerImpl initializer = new ChannelInitializerImpl(NodeDefine.GAME_SERVER);
        nettyServer = new NettyServer("NETTY_SERVER", 2000, initializer);
        nettyServer.bind();
    }
}
