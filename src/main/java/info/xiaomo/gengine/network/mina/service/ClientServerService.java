package info.xiaomo.gengine.network.mina.service;

import java.util.Map;
import info.xiaomo.gengine.network.mina.TcpServer;
import info.xiaomo.gengine.network.mina.code.ClientProtocolCodecFactory;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.handler.ClientProtocolHandler;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.thread.timer.event.ServerHeartTimer;
import org.apache.mina.core.filterchain.IoFilter;

/**
 * 游戏前端消息接收 服务
 *
 *
 * @version $Id: $Id
 * 2017年6月29日 下午2:15:38
 */
public class ClientServerService extends GameService<MinaServerConfig> {
	protected final TcpServer tcpServer;
	protected final MinaServerConfig minaServerConfig;
	protected final ClientProtocolHandler clientProtocolHandler;


	/**
	 * 不创建默认IO线程池
	 */
	public ClientServerService(MinaServerConfig minaServerConfig) {
		this(null, minaServerConfig);
	}

	/**
	 * 使用默认消息处理器
	 */
	public ClientServerService(ThreadPoolExecutorConfig threadExecutorConfig, MinaServerConfig minaServerConfig) {
		this(threadExecutorConfig, minaServerConfig, new ClientProtocolHandler(8));
	}

	/**
	 * <p>Constructor for ClientServerService.</p>
	 *
	 * @param threadExecutorConfig   线程池配置
	 * @param minaServerConfig      服务器配置
	 * @param clientProtocolHandler 消息处理器
	 */
	public ClientServerService(ThreadPoolExecutorConfig threadExecutorConfig, MinaServerConfig minaServerConfig,
	                           ClientProtocolHandler clientProtocolHandler) {
		super(threadExecutorConfig);
		this.minaServerConfig = minaServerConfig;
		this.clientProtocolHandler = clientProtocolHandler;
		tcpServer = new TcpServer(minaServerConfig, clientProtocolHandler, new ClientProtocolCodecFactory());
	}

	/**
	 * <p>Constructor for ClientServerService.</p>
	 *
	 * @param threadExecutorConfig   线程池配置
	 * @param minaServerConfig      服务器配置
	 * @param clientProtocolHandler 消息处理器
	 * @param filters               a {@link Map} object.
	 */
	public ClientServerService(ThreadPoolExecutorConfig threadExecutorConfig, MinaServerConfig minaServerConfig,
	                           ClientProtocolHandler clientProtocolHandler, Map<String, IoFilter> filters) {
		super(threadExecutorConfig);
		this.minaServerConfig = minaServerConfig;
		this.clientProtocolHandler = clientProtocolHandler;
		tcpServer = new TcpServer(minaServerConfig, clientProtocolHandler, new ClientProtocolCodecFactory(), filters);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void running() {
		clientProtocolHandler.setService(this);
		tcpServer.run();
		// 添加定时器 ,如果心跳配置为0，则没有定时器
		ServerThread syncThread = getExecutor(ThreadType.SYNC);
		if (syncThread != null) {
			syncThread.addTimerEvent(new ServerHeartTimer());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onShutdown() {
		super.onShutdown();
		tcpServer.stop();

	}

	/**
	 * <p>Getter for the field <code>minaServerConfig</code>.</p>
	 *
	 */
	public MinaServerConfig getMinaServerConfig() {
		return minaServerConfig;
	}

	/**
	 * <p>Getter for the field <code>tcpServer</code>.</p>
	 *
	 */
	public TcpServer getTcpServer() {
		return tcpServer;
	}


}
