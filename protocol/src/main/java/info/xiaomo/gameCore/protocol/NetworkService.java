package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 网络服务,负责以下几个事情:</br>
 * 1.网络服务提供TCP协议的接受和发送</br>
 * 2.通信协议的编码和解码</br>
 * 3.调用消费则消费消息</br>
 *
 * @author 小莫
 */
public class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    private int port;

    private ServerBootstrap bootstrap;

    /**
     * 状态
     */
    private int state;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private static final byte STATE_STOP = 0;
    private static final byte STATE_START = 1;


    /**
     * 开启一个netty的服务器
     */
    public void start() {
        try {
            ChannelFuture f = bootstrap.bind(port);
            f.sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.state = STATE_START;
        LOGGER.info("Server on port:{} is start", port);
    }

    NetworkService(final NetworkServiceBuilder builder) {
        int bossLoopGroupCount = builder.getBossLoopGroupCount();
        int workerLoopGroupCount = builder.getWorkerLoopGroupCount();
        this.port = builder.getPort();

        bossGroup = new NioEventLoopGroup(bossLoopGroupCount);
        workerGroup = new NioEventLoopGroup(workerLoopGroupCount);

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 128 * 1024);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 128 * 1024);

        bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pip = ch.pipeline();
                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMessagePool()));
                pip.addLast("NettyMessageEncoder", new MessageEncoder(builder.getMessagePool()));
                pip.addLast("NettyMessageExecutor", new MessageExecutor(builder.getConsumer(), builder.getNetworkEventListener()));
                for (ChannelHandler handler : builder.getChannelHandlerList()) {
                    pip.addLast(handler);
                }
            }
        });

    }

    public void stop() {
        this.state = STATE_STOP;
        Future<?> bf = bossGroup.shutdownGracefully();
        Future<?> wf = workerGroup.shutdownGracefully();
        try {
            bf.get(5000, TimeUnit.MILLISECONDS);
            wf.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.info("Netty服务器关闭失败", e);
        }
        LOGGER.info("Netty服务器已经关闭，端口号【{}】", this.port);
    }

    public boolean isRunning() {
        return this.state == STATE_START;
    }

    public boolean isClosed() {
        return this.state == 2;
    }

}
