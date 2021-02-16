package info.xiaomo.gengine.thread.queue.action;

import info.xiaomo.gengine.thread.queue.IActionQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * action抽象类
 *
 * @author laofan
 */
public abstract class GameAction implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameAction.class);

	/**
	 * 队列
	 */
	private final IActionQueue<GameAction, DelayGameAction> queue;
	/**
	 * 创建时间
	 */
	protected long createTime;

	public GameAction(IActionQueue<GameAction, DelayGameAction> queue) {
		this.queue = queue;
		createTime = System.currentTimeMillis();
	}

	public IActionQueue<GameAction, DelayGameAction> getActionQueue() {
		return queue;
	}

	public void run() {
		if (queue != null) {
			long start = System.currentTimeMillis();
			try {
				execute();
				long end = System.currentTimeMillis();
				long interval = end - start;
				long leftTime = end - createTime;
				if (interval >= 1000) {
					LOGGER.warn("execute action : " + toString() + ", interval : " + interval + ", leftTime : " + leftTime + ", size : " + queue.getActionQueue().size());
				}
			} catch (Exception e) {
				LOGGER.error("run action execute exception. action : " + toString(), e);
			} finally {
				queue.dequeue(this);
			}
		}
	}

	/**
	 * 执行体
	 */
	public abstract void execute();
}
