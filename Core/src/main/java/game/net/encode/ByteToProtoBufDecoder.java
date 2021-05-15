package game.net.encode;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import game.net.msg.MessageMappingHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * ProtoBuf解码解码器
 *
 * pipeline.addLast(new {@link LengthFieldBasedFrameDecoder}(8192, 0, 4, 0, 4));
 * pipeline.addLast(new {@link ByteToProtoBufDecoder}());
 */
public class ByteToProtoBufDecoder extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //消息的前4个字节已被跳过，所以这里已经没有了长度字段，只剩下内容部分
        //编码时使用了一个int标记protoBuf协议的类，那在解码时需要先取出该标记
        int messageId = byteBuf.readInt();

        //通过索引获得该协议对应的解析器(客户端与服务器需要保持索引的一致性)
        Parser<?> parser = MessageMappingHolder.getParser(messageId);
        if (parser == null) {
            //自己决定如何处理协议无法解析的情况
            throw new IllegalArgumentException("illegal messageId " + messageId);
        }

        try (ByteBufInputStream bufInputStream = new ByteBufInputStream(byteBuf)) {
            MessageLite messageLite = (MessageLite) parser.parseFrom(bufInputStream);
            channelHandlerContext.fireChannelRead(messageLite);
        }
    }
}
