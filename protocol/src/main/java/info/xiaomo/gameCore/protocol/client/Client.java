package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import info.xiaomo.gameCore.protocol.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 基于netty和当前消息结构的一个客户端连接器
 *
 * @author xiaomo
 * 2017年7月15日 下午10:25:01
 */
public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private volatile int state = 0;
    private String host;
    private int port;
    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public Client(String host, int port, final MessageDecoder decoder, final MessageEncoder encoder, final MessageExecutor executor) {
        this.host = host;
        this.port = port;
        this.group = new NioEventLoopGroup();

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.group);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, Boolean.TRUE);
        this.bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        this.bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline pip = channel.pipeline();
                pip.addLast("NettyMessageDecoder", new NettyMessageDecoder(decoder, 0, 2, 0, 4));
                pip.addLast("NettyMessageEncoder", new NettyMessageEncoder(encoder, 2));
                pip.addLast("NettyMessageExecutor", new NettyMessageExecutor(executor));
            }
        });
    }

    public void start() {
        try {
            this.bootstrap.connect(this.host, this.port).sync();
        } catch (InterruptedException e) {
            LOGGER.error("连接服务器【{}:{}】失败", this.host, this.port);
            throw new RuntimeException(e);
        }
        this.state = 1;
    }

    public boolean isOpen() {
        return this.state == 1;
    }

    public void close() {
        this.state = 2;
        Future<?> future = this.group.shutdownGracefully();
        try {
            future.await(5000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.info("Netty客户端关闭失败", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Netty客户端已经关闭【{}:{}】", this.host, this.port);
    }

    public boolean isClosed() {
        return this.state == 2;
    }

}
