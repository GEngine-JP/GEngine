package info.xiaomo.gengine.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import info.xiaomo.gengine.thread.ExecutorPool;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.thread.ThreadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 抽象服务
 *
 * @param <Conf>
 *
 * @date 2017-03-30
 */
public abstract class GameService<Conf extends BaseServerConfig> implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
	/**
	 * 线程容器
	 */
	private final Map<ThreadType, Executor> serverThreads = new ConcurrentHashMap<>();

	/**
	 * 不创建地图线程组
	 *
	 * @param threadPoolExecutorConfig
	 */
	public GameService(ThreadPoolExecutorConfig threadPoolExecutorConfig) {
		// 初始化
		if (threadPoolExecutorConfig != null) {
			// IO默认线程池 客户端的请求,默认使用其执行
			ThreadPoolExecutor ioHandlerThreadExecutor = threadPoolExecutorConfig.newThreadPoolExecutor();
			serverThreads.put(ThreadType.IO, ioHandlerThreadExecutor);

			//全局sync线程
			ServerThread syncThread = new ServerThread(new ThreadGroup("全局同步线程"),
					"全局同步线程:" + getClass().getSimpleName(), threadPoolExecutorConfig.getHeart(),
					threadPoolExecutorConfig.getCommandSize());
			syncThread.start();
			serverThreads.put(ThreadType.SYNC, syncThread);
		}
	}

	@Override
	public void run() {
		Runtime.getRuntime().addShutdownHook(new Thread(new CloseByExit(this)));
		initThread();
		running();
	}

	/**
	 * 初始化线程
	 */
	protected void initThread() {
	}

	/**
	 * 运行中
	 */
	protected abstract void running();

	/**
	 * 关闭回调
	 */
	protected void onShutdown() {
		serverThreads.values().forEach(executor -> {
			if (executor != null) {
				try {
					if (executor instanceof ServerThread) {
						if (((ServerThread) executor).isAlive()) {
							((ServerThread) executor).stop(true);
						}
					} else if (executor instanceof ThreadPoolExecutor) {
						if (!((ThreadPoolExecutor) executor).isShutdown()) {
							((ThreadPoolExecutor) executor).shutdown();
							while (!((ThreadPoolExecutor) executor).awaitTermination(5, TimeUnit.SECONDS)) {
								LOGGER.error("线程池剩余线程:" + ((ThreadPoolExecutor) executor).getActiveCount());
							}
						}
					} else if (executor instanceof ExecutorPool) {
						((ExecutorPool) executor).stop();
					}
				} catch (Exception e) {
					LOGGER.error("关闭线程", e);
				}

			}
		});
	}

	/**
	 * 关闭
	 *
	 * @param flag
	 */
	public void stop(boolean flag) {
		onShutdown();
	}

	public Map<ThreadType, Executor> getServerThreads() {
		return serverThreads;
	}

	/**
	 * 获得线程
	 *
	 * @param threadType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Executor> T getExecutor(ThreadType threadType) {
		return (T) serverThreads.get(threadType);
	}


	/**
	 * 关服回调
	 *
	 *
	 * @date 2017-03-30
	 */
	private static final class CloseByExit implements Runnable {

		private static final Logger log = LoggerFactory.getLogger(CloseByExit.class);
		@SuppressWarnings("rawtypes")
		private final GameService server;

		@SuppressWarnings("rawtypes")
		private CloseByExit(GameService server) {
			this.server = server;
		}

		@Override
		public void run() {
			server.onShutdown();
			log.warn("服务{}已停止", server.getClass().getName());
		}
	}

}
