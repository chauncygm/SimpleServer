/*
 * @AutoGenerate
 * Python script auto generate, Don't edit it.
 */
package script.handler.login;

import game.script.IHandlerScript;
import io.netty.channel.ChannelHandlerContext;
import msg.MsgDefine;

/**
 * @author  gongshengjun
 * @date 	2021-05-15 17:44:18
 */
public class OnReqCreateRole implements IHandlerScript {

    @Override
    public int getId() {
        return MsgDefine.MSG_LOGIN_ReqCreateRole;
    }

    @Override
    public Object call(Object... objs) {
        return null;
    }

    @Override
    public <Msg> void doAction(ChannelHandlerContext chc, Msg msg) {

    }
}
