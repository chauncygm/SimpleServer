package game.net.msg;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import game.utils.ProtoBufUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息映射管理器
 * <p>
 * 使用静态初始化的方式，利用空间换取安全性(可见性)和性能,
 * <p>
 * 要在应用启动的最开始的时候加载该类
 */
public class MessageMappingHolder {
    private final static Logger logger = LogManager.getLogger(MessageMappingHolder.class);
    private static final Map<Integer, Parser<?>> messageId2ParserMap = new HashMap<>(2048);
    private static final Map<Class<?>, Integer> messageClass2IdMap = new HashMap<>(2048);

    /**
     * 批量注册消息
     */
    public static void registerMsgDict(Map<Integer, Message> map) {
        for (Message message : map.values()) {
            if (!registerMsgDict(message)) {
                logger.error("注册协议失败！");
                System.exit(0);
            }
        }
        logger.info("注册协议成功！");
    }

    /**
     * 单个注册消息
     */
    private static boolean registerMsgDict(Message message) {
        if (messageId2ParserMap.containsKey(message.getMessageId())) {
            logger.error("message repeated, info=" + message.toString());
            return false;
        }

        try {
            Class<? extends MessageLite> messageClass = ProtoBufUtils.findMessageClass(message.getJavaPackageName(),
                    message.getJavaOuterClassName(), message.getMessageName());
            Parser<? extends MessageLite> parser = ProtoBufUtils.findMessageParser(messageClass);
            messageClass2IdMap.put(messageClass, message.getMessageId());
            messageId2ParserMap.put(message.getMessageId(), parser);
        } catch (ReflectiveOperationException e) {
            logger.error("can't find class or parser, messageInfo=" + message.toString() + " error:", e);
            return false;
        }
        return true;
    }

    /**
     * 根据消息id 获取对应的parser
     *
     * @param messageId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Parser<T> getParser(int messageId) {
        return (Parser<T>) messageId2ParserMap.get(messageId);
    }

    /**
     * 获取对象对应的messageId
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T extends MessageLite> int getMessageId(T message) {
        @SuppressWarnings("unchecked")
        Class<T> messageLiteClass = (Class<T>) message.getClass();
        return getMessageId(messageLiteClass);
    }

    /**
     * 获取消息类对应的messageId
     *
     * @param messageClass
     * @param <T>
     * @return
     */
    public static <T extends MessageLite> int getMessageId(Class<T> messageClass) {
        return messageClass2IdMap.getOrDefault(messageClass, 0);
    }

}
