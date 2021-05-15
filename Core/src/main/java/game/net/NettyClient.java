package game.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author  gongshengjun
 * @date    2020/12/25 16:36
 */
public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private final String name;
    private final String ip;
    private final int port;
    private final Bootstrap client;
    private final NioEventLoopGroup workGroup;
    private final int sendBuff;
    private final int recvBuff;
    private ChannelFuture future;

    public NettyClient(String name, String ip, int port, ChannelInitializer<SocketChannel> initializer) {
        this(name, ip, port, 1024 * 1024, 1024 * 1024, initializer);
    }

    public NettyClient(String name, String ip, int port, int sendBuff, int recvBuff, ChannelInitializer<SocketChannel> initializer) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.sendBuff = sendBuff;
        this.recvBuff = recvBuff;
        this.client = new Bootstrap();
        this.workGroup = new NioEventLoopGroup();
        initialize(initializer);
    }

    private void initialize(ChannelInitializer<SocketChannel> initializer) {
        client.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(initializer)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);
    }

    public boolean connect() {
        future = client.connect(ip, port);
        future.awaitUninterruptibly(30 * 1000);
        return future.isSuccess();
    }

    private void syncClose() {
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workGroup.shutdownGracefully();
    }
}
