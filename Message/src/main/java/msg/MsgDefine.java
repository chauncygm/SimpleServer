/*
 * @AutoGenerate
 * Python script auto generate, Don't edit it.
 */
package msg;

import game.net.msg.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  gongshengjun
 * @date 	2021-05-15 17:48:40
 */
public class MsgDefine {
    
    public static final int MSG_BASE_ReqServerInfo = 101101;
	public static final int MSG_BASE_ResServerInfo = 101202;
	public static final int MSG_LOGIN_ReqRegister = 102101;
	public static final int MSG_LOGIN_ResRegister = 102202;
	public static final int MSG_LOGIN_ReqCreateRole = 102103;
	public static final int MSG_LOGIN_ResCreateRole = 102204;
	public static final int MSG_LOGIN_ReqLogin = 102105;
	public static final int MSG_LOGIN_ResLogin = 102206;
	
    public static Map<Integer, Message> allMsg() {
        Map<Integer, Message> map = new HashMap<>(1024);
        map.put(MSG_BASE_ReqServerInfo, new Message(MSG_BASE_ReqServerInfo,  "msg.base", "BaseMessage", "ReqServerInfo"));
		map.put(MSG_BASE_ResServerInfo, new Message(MSG_BASE_ResServerInfo,  "msg.base", "BaseMessage", "ResServerInfo"));
		map.put(MSG_LOGIN_ReqRegister, new Message(MSG_LOGIN_ReqRegister,  "msg.login", "LoginMessage", "ReqRegister"));
		map.put(MSG_LOGIN_ResRegister, new Message(MSG_LOGIN_ResRegister,  "msg.login", "LoginMessage", "ResRegister"));
		map.put(MSG_LOGIN_ReqCreateRole, new Message(MSG_LOGIN_ReqCreateRole,  "msg.login", "LoginMessage", "ReqCreateRole"));
		map.put(MSG_LOGIN_ResCreateRole, new Message(MSG_LOGIN_ResCreateRole,  "msg.login", "LoginMessage", "ResCreateRole"));
		map.put(MSG_LOGIN_ReqLogin, new Message(MSG_LOGIN_ReqLogin,  "msg.login", "LoginMessage", "ReqLogin"));
		map.put(MSG_LOGIN_ResLogin, new Message(MSG_LOGIN_ResLogin,  "msg.login", "LoginMessage", "ResLogin"));
		return map;
    }
}
