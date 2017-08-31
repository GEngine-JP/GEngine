package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import info.xiaomo.gameCore.protocol.handler.WSByteToWebSocketFrameHandler;
import info.xiaomo.gameCore.protocol.handler.WSWebSocketFrameToByteHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    private int port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    private int state = 0;

    private static final byte STATE_STOP = 0;
    private static final byte STATE_START = 1;


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
        if (builder.isWebSocket()) {
            bootstrap.childHandler(new WebSocketHandler(builder));
        } else {
            bootstrap.childHandler(new SocketHandler(builder));
        }
    }


    class WebSocketHandler extends ChannelInitializer {
        private NetworkServiceBuilder builder;

        WebSocketHandler(NetworkServiceBuilder builder) {
            this.builder = builder;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            //添加websocket相关内容
            ChannelPipeline pip = ch.pipeline();
            pip.addLast(new HttpServerCodec());
            pip.addLast(new HttpObjectAggregator(65536));
            pip.addLast(new WebSocketServerProtocolHandler("/"));
            pip.addLast(new WSWebSocketFrameToByteHandler());
            pip.addLast(new MessageDecoder(builder.getMessagePool()));
            pip.addLast(new WSByteToWebSocketFrameHandler());
            pip.addLast(new MessageExecutor(builder.getConsumer(), builder.getListener()));
            for (ChannelHandler handler : builder.getExtraHandlers()) {
                pip.addLast(handler);
            }
        }
    }

    class SocketHandler extends ChannelInitializer {
        private NetworkServiceBuilder builder;

        SocketHandler(NetworkServiceBuilder builder) {
            this.builder = builder;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pip = ch.pipeline();
            int maxLength = 1048576;
            int lengthFieldLength = 4;
            int ignoreLength = -4;
            int offset = 0;
            pip.addLast(new LengthFieldBasedFrameDecoder(maxLength, offset, lengthFieldLength, ignoreLength, lengthFieldLength));
            pip.addLast(new MessageDecoder(builder.getMessagePool()));
            pip.addLast(new LengthFieldPrepender(4, true));
            pip.addLast(new MessageEncoder(builder.getMessagePool()));
            pip.addLast(new MessageExecutor(builder.getConsumer(), builder.getListener()));
            for (ChannelHandler handler : builder.getExtraHandlers()) {
                pip.addLast(handler);
            }
        }
    }


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
        LOGGER.info("Netty Server on port:{} is closed", port);
    }

    public boolean isRunning() {
        return this.state == STATE_START;
    }

}
