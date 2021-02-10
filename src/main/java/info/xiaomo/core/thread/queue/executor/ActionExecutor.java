package info.xiaomo.core.thread.queue.executor;


import info.xiaomo.core.thread.queue.action.DelayCheckThread;
import info.xiaomo.core.thread.queue.action.DelayGameAction;
import info.xiaomo.core.thread.queue.action.GameAction;

/**
 * 执行action队列的线程池<br>
 * 延迟执行的action，先放置到delay action队列中，延迟时间后再加入执行队列
 */
public class ActionExecutor extends HandlerExecutor<GameAction> implements IDelayExecutor {

	/**
	 * 延迟/定时 检测线程
	 */
	private final DelayCheckThread delayCheckThread;

	public ActionExecutor(int corePoolSize, int maxPoolSize, int keepAliveTime, int cacheSize, String prefix) {
		super(corePoolSize, maxPoolSize, keepAliveTime, cacheSize, prefix);
		delayCheckThread = new DelayCheckThread(prefix);
		delayCheckThread.start();
	}


	/**
	 * 执行延迟/定时 action
	 *
	 * @param delayAction
	 */
	public void executeDelayAction(DelayGameAction delayAction) {
		delayCheckThread.addDelayAction(delayAction);
	}

}
