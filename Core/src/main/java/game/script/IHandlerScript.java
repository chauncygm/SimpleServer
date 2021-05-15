package game.script;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author  gongshengjun
 * @date    2020/11/14 17:00
 */
public interface IHandlerScript extends IScript{

    <Msg> void doAction(ChannelHandlerContext chc, Msg msg);
}
