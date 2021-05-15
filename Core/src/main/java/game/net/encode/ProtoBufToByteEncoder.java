package game.net.encode;

import com.google.protobuf.MessageLite;
import game.net.msg.MessageMappingHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * ProtoBuf编码器
 *
 * 编码方式(这里没有使用{@link LengthFieldPrepender}是为了提高可读性)
 * pipeline.addLast("ProtoBufToByteEncoder", new {@link ProtoBufToByteEncoder}());
 */
public class ProtoBufToByteEncoder extends MessageToByteEncoder<MessageLite> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageLite messageLite, ByteBuf byteBuf) throws Exception {
        //先获取消息对应的枚举编号
        int messageId = MessageMappingHolder.getMessageId(messageLite);

        //protoLength表示有效内容的长度(不包括自身), 4是messageId的长度
        int protoLength = 4 + messageLite.getSerializedSize();
        byteBuf.writeInt(protoLength);
        byteBuf.writeInt(messageId);
        try (ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(byteBuf)) {
            messageLite.writeTo(byteBufOutputStream);
        }
    }
}
