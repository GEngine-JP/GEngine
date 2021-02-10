package info.xiaomo.core.network.mina.service;

import info.xiaomo.core.network.mina.HttpServer;
import info.xiaomo.core.network.mina.config.MinaServerConfig;
import info.xiaomo.core.network.mina.handler.HttpServerIoHandler;
import info.xiaomo.core.server.Service;
import info.xiaomo.core.thread.ThreadPoolExecutorConfig;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏服http服务器
 *
 * @author JiangZhiYong
 * @version $Id: $Id
 * <p>
 * 2017年7月24日 上午11:28:28
 */
@SuppressWarnings("MultipleTopLevelClassesInFile")
public class GameHttpSevice extends Service<MinaServerConfig> {

	private static final Logger log = LoggerFactory.getLogger(GameHttpSevice.class);

	private final HttpServer httpServer;
	private final MinaServerConfig minaServerConfig;

	/**
	 * <p>Constructor for GameHttpSevice.</p>
	 */
	public GameHttpSevice(ThreadPoolExecutorConfig threadExcutorConfig, MinaServerConfig minaServerConfig) {
		super(threadExcutorConfig);
		this.minaServerConfig = minaServerConfig;
		httpServer = new HttpServer(minaServerConfig, new GameHttpServerHandler(this));
	}

	/**
	 * <p>Constructor for GameHttpService.</p>
	 */
	public GameHttpSevice(MinaServerConfig minaServerConfig) {
		super(null);
		this.minaServerConfig = minaServerConfig;
		httpServer = new HttpServer(minaServerConfig, new GameHttpServerHandler(this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void running() {
		log.debug(" run ... ");
		httpServer.run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onShutdown() {
		super.onShutdown();
		log.debug(" stop ... ");
		httpServer.stop();
	}

	@Override
	public String toString() {
		return minaServerConfig.getName();
	}

	/**
	 * <p>Getter for the field <code>minaServerConfig</code>.</p>
	 */
	public MinaServerConfig getMinaServerConfig() {
		return minaServerConfig;
	}

}

/**
 * 消息处理器
 *
 * @author JiangZhiYong
 * @date 2017-03-31
 * QQ:359135103
 */
class GameHttpServerHandler extends HttpServerIoHandler {

	//private static final Logger log = LoggerFactory.getLogger(ClusterHttpServerHandler.class);

	private final Service<MinaServerConfig> service;

	public GameHttpServerHandler(Service<MinaServerConfig> service) {
		this.service = service;
	}

	protected Service<MinaServerConfig> getService() {
		return service;
	}

	@Override
	public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {

	}
}
