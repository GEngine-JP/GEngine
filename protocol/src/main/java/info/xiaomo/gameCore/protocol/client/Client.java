package info.xiaomo.gameCore.protocol.client;

import com.google.protobuf.MessageOrBuilder;
import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 基于netty和当前消息结构的一个客户端连接器
 *
 * @author xiaomo
 * 2017年7月15日 下午10:25:01
 */
@Data
public class Client {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    protected volatile int state = 0;
    protected String host;
    protected int port;
    protected EventLoopGroup group;
    protected Bootstrap bootstrap;
    protected Channel channel;
    protected ClientBuilder builder;

    public Client(ClientBuilder builder) {
        this.builder = builder;
        this.host = builder.getHost();
        this.port = builder.getPort();
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
                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMessagePool()));
                pip.addLast("NettyMessageEncoder", new MessageEncoder(builder.getMessagePool()));
                pip.addLast("NettyMessageExecutor", new MessageExecutor(builder.getConsumer(), builder.getNetworkEventListener()));
            }
        });
    }

    public void start() {
        try {
            ChannelFuture future = this.bootstrap.connect(this.host, this.port).sync();
            channel = future.channel();
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


    public boolean sendMsg(MessageOrBuilder message) {
        Channel channel = getChannel();
        if (channel != null && channel.isOpen()) {
            channel.writeAndFlush(message);
            return true;
        }
        return false;
    }

}
