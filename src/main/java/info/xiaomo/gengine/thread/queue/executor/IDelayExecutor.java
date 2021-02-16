package info.xiaomo.gengine.thread.queue.executor;


import info.xiaomo.gengine.thread.queue.action.DelayGameAction;
import info.xiaomo.gengine.thread.queue.action.GameAction;

/**
 * 带延迟执行的线程执行器接口
 *
 * @author laofan
 */
public interface IDelayExecutor extends IExecutor<GameAction> {

	/**
	 * 执行延迟/定时 action
	 *
	 * @param delayAction
	 */
	void executeDelayAction(DelayGameAction delayAction);
}
