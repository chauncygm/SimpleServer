package game.net;

import game.net.encode.ByteToProtoBufDecoder;
import game.net.encode.ProtoBufToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author  gongshengjun
 * @date    2020/12/24 17:05
 */
public class ChannelInitializerImpl extends ChannelInitializer<SocketChannel> {

    private final int nodeType;

    public ChannelInitializerImpl(int nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
        pipeline.addLast(new ByteToProtoBufDecoder());
        pipeline.addLast(new ProtoBufToByteEncoder());
        pipeline.addLast(new MsgHandler(nodeType));
    }
}
