package game.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  gongshengjun
 * @date    2020/12/24 15:41
 */
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String name;
    private final int port;
    private boolean listen;

    private final int sendBuff;
    private final int recvBuff;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private final ChannelInitializer<SocketChannel> channel;
    private ChannelFuture future;


    public NettyServer(String name, int port, ChannelInitializer<SocketChannel> channel) {
        this(name, port, channel, 1024 * 1024, 1024 * 1024);
    }

    public NettyServer(String name, int port, ChannelInitializer<SocketChannel> channel, int sendBuff, int recvBuff) {
        this.name = name;
        this.port = port;
        this.channel = channel;
        this.sendBuff = sendBuff;
        this.recvBuff = recvBuff;
    }

    public void bind() {
        if (listen) {
            return;
        }

        logger.info(name + " start listen port = " + port);
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workGroup);
        server.channel(NioServerSocketChannel.class);
        server.childHandler(channel);
        server.childOption(ChannelOption.SO_KEEPALIVE, true);
        server.childOption(ChannelOption.TCP_NODELAY, true);
        server.childOption(ChannelOption.SO_REUSEADDR, true);
        server.childOption(ChannelOption.SO_SNDBUF, sendBuff);
        server.childOption(ChannelOption.SO_RCVBUF, recvBuff);
        future = server.bind(port);
        future.addListener((f) -> {
           if (f.isSuccess()) {
               listen = true;
           }
             else {
               listen = false;
               logger.info("{} 监听端口[{}]失败", name, port);
               System.exit(-1);
           }
        });
    }


    public void syncClose() {
        if (listen) {
            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (listen) {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
