package client;

import client.cmd.CmdManager;
import game.net.msg.MessageMappingHolder;
import msg.MsgDefine;

/**
 * @author  gongshengjun
 * @date    2021/4/25 11:20
 */
public class Client {

    public static void main(String[] args) {
        MessageMappingHolder.registerMsgDict(MsgDefine.allMsg());
        CmdManager.getInstance().start();
    }
}
