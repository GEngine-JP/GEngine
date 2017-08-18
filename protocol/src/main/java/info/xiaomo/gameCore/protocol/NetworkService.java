package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.base.common.EncryptUtil;
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

public class NetworkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

	private static final int SERVER_OPEN = 1;

	private static final int SERVER_CLOSE = 2;

	private int bossLoopGroupCount;

	private int workerLoopGroupCount;

	private int port;

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private ServerBootstrap bootstrap;

	private int serverState = 0;

	NetworkService(final NetworkServiceBuilder builder) {
		this.bossLoopGroupCount = builder.getBossLoopGroupCount();
		this.workerLoopGroupCount = builder.getWorkerLoopGroupCount();
		this.port = builder.getPort();

		bossGroup = new NioEventLoopGroup(this.bossLoopGroupCount);
		workerGroup = new NioEventLoopGroup(this.workerLoopGroupCount);
		
		EncryptUtil.initEncryptor(builder.getEncryptor());
		
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
				pip.addLast("NettyMessageEncoder", new MessageEncoder());
				pip.addLast("NettyMessageExecutor", new MessageExecutor(builder.getConsumer()));
				for(ChannelHandler handler : builder.getExtraHandlers()) {
					pip.addLast(handler);
				}
			}
		});

	}

	public void open() {
		try {
			bootstrap.bind(port).sync();
		} catch (InterruptedException e) {
			LOGGER.error("Netty服务器绑定端口失败.", e);
			throw new RuntimeException(e);
		}
		System.out.println("Netty server start ok : " + port);
		this.serverState = SERVER_OPEN;
		LOGGER.info(String.format("Server on port:%d is start", port));
	}

	public void close() {
		this.serverState = SERVER_CLOSE;
		Future<?> bf = bossGroup.shutdownGracefully();
		Future<?> wf = workerGroup.shutdownGracefully();
		try {
			bf.get(5000, TimeUnit.MILLISECONDS);
			wf.get(5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			LOGGER.info("Netty服务器关闭失败", e);
		}
		LOGGER.info(String.format("Netty Server on port:%d is closed", port));
	}

	public boolean isOpen() {
		return this.serverState == SERVER_OPEN;
	}

	public boolean isClosed() {
		return this.serverState == SERVER_CLOSE;
	}

}
