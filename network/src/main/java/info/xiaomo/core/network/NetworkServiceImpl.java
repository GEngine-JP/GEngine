package info.xiaomo.core.network;

import info.xiaomo.core.network.handler.MessageDecoder;
import info.xiaomo.core.network.handler.MessageEncoder;
import info.xiaomo.core.network.handler.MessageExecutor;
import info.xiaomo.core.network.handler.WebSocketDecoder;
import info.xiaomo.core.network.handler.WebSocketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.SocketHandler;

/**
 * @author xiaomo
 */
public class NetworkServiceImpl implements IService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServiceImpl.class);

    private int port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    private ServiceState state;

    NetworkServiceImpl(final NetworkServiceBuilder builder) {
        int bossLoopGroupCount = builder.getBossLoopGroupCount();
        int workerLoopGroupCount = builder.getWorkerLoopGroupCount();
        this.port = builder.getPort();
        final SslContext sslCtx;

        bossGroup = new NioEventLoopGroup(bossLoopGroupCount);
        workerGroup = new NioEventLoopGroup(workerLoopGroupCount);

        if (builder.isSsl()) {
            try {
                File keyCertChainFile = new File(builder.getSslKeyCertChainFile());
                File keyFile = new File(builder.getSslKeyFile());
                SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(keyCertChainFile, keyFile);
                sslCtx = sslContextBuilder.build();
            } catch (SSLException var4) {
                throw new RuntimeException("sslCtx create failed.", var4);
            }
        } else {
            sslCtx = null;
        }
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 128 * 1024);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 128 * 1024);

        bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        if (builder.isWebSocket()) {
            bootstrap.childHandler(new WebSocketHandler(builder, sslCtx));
        } else {
            bootstrap.childHandler(new SocketHandler(builder));
        }
    }


    /**
     * web socket的handler
     */
    class WebSocketHandler extends ChannelInitializer {
        private NetworkServiceBuilder builder;
        private SslContext sslCtx;

        WebSocketHandler(NetworkServiceBuilder builder, SslContext sslCtx) {
            this.builder = builder;
            this.sslCtx = sslCtx;
        }

        @Override
        protected void initChannel(Channel ch) {
            //添加web socket相关内容
            ChannelPipeline pip = ch.pipeline();
            if (sslCtx != null) {
                pip.addLast("sslHandler", sslCtx.newHandler(ch.alloc()));
            }
            pip.addLast(new HttpServerCodec());
            pip.addLast(new HttpObjectAggregator(65536));
            pip.addLast(new WebSocketServerProtocolHandler("/"));
            pip.addLast(new WebSocketDecoder());
            pip.addLast(new WebSocketEncoder());
            pip.addLast(new MessageDecoder(builder.getImessageandhandler()));
            pip.addLast(new MessageExecutor(builder.getConsumer(), builder.getListener()));
            for (ChannelHandler handler : builder.getExtraHandlers()) {
                pip.addLast(handler);
            }
        }
    }


    /**
     *  socket的handler
     */
    class SocketHandler extends ChannelInitializer {
        private NetworkServiceBuilder builder;

        SocketHandler(NetworkServiceBuilder builder) {
            this.builder = builder;
        }

        @Override
        protected void initChannel(Channel ch) {
            ChannelPipeline pip = ch.pipeline();
            int maxLength = 1048576;
            int lengthFieldLength = 4;
            int ignoreLength = -4;
            int offset = 0;
            pip.addLast(new LengthFieldBasedFrameDecoder(maxLength, offset, lengthFieldLength, ignoreLength, lengthFieldLength));
            pip.addLast(new MessageDecoder(builder.getImessageandhandler()));
            pip.addLast(new LengthFieldPrepender(4, true));
            pip.addLast(new MessageEncoder(builder.getImessageandhandler()));
            pip.addLast(new MessageExecutor(builder.getConsumer(), builder.getListener()));
            for (ChannelHandler handler : builder.getExtraHandlers()) {
                pip.addLast(handler);
            }
        }
    }


    @Override
    public void start() {
        try {
            ChannelFuture f = bootstrap.bind(port);
            f.sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.state = ServiceState.RUNNING;
        LOGGER.info("Server on port:{} is start", port);
    }


    /**
     * 停止網絡服務
     */
    @Override
    public void stop() {
        this.state = ServiceState.STOPPED;
        Future<?> bf = bossGroup.shutdownGracefully();
        Future<?> wf = workerGroup.shutdownGracefully();
        try {
            bf.get(5000, TimeUnit.MILLISECONDS);
            wf.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.info("Netty服务器关闭失败", e);
        }
        LOGGER.info("Netty Server on port:{} is closed", port);
    }


    /**
     * 獲取當前網絡服務狀態
     * @return state
     */
    @Override
    public ServiceState getState() {
        return this.state;
    }

    /**
     * 服務是否開啟
     * @return bool
     */
    @Override
    public boolean isOpened() {
        return state == ServiceState.RUNNING;
    }

    /**
     * 服務是否關閉
     * @return bool
     */
    @Override
    public boolean isClosed() {
        return state == ServiceState.STOPPED;
    }
}
