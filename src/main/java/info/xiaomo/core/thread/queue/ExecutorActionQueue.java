package info.xiaomo.core.thread.queue;

import java.util.Queue;
import info.xiaomo.core.thread.queue.action.DelayGameAction;
import info.xiaomo.core.thread.queue.action.GameAction;
import info.xiaomo.core.thread.queue.executor.IDelayExecutor;


/**
 * 可执行的ACTION队列
 *
 * @author laofan
 */
public class ExecutorActionQueue extends ExecutorHandlerQueue<GameAction> implements IActionQueue<GameAction, DelayGameAction> {


	public ExecutorActionQueue(IDelayExecutor executor, String queueName) {
		super(executor, queueName);

	}


	public void enDelayQueue(DelayGameAction delayAction) {
		((IDelayExecutor) executor).executeDelayAction(delayAction);
	}


	@Override
	public Queue<GameAction> getActionQueue() {
		return getQueue();
	}


}
