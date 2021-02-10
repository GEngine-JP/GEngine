package info.xiaomo.core.thread.queue;

import java.util.Queue;
import info.xiaomo.core.thread.queue.action.DelayGameAction;
import info.xiaomo.core.thread.queue.action.GameAction;


/**
 * action 队列接口
 *
 * @author laofan
 */
public interface IActionQueue<T extends GameAction, E extends DelayGameAction> {

	/**
	 * 添加延时执行任务
	 *
	 * @param delayAction
	 */
	void enDelayQueue(E delayAction);

	/**
	 * 清空队列
	 */
	void clear();

	/**
	 * 获取队列
	 *
	 * @return
	 */
	Queue<T> getActionQueue();

	/**
	 * 入队
	 *
	 * @param action
	 */
	void enqueue(T action);

	/**
	 * 出队
	 *
	 * @param cmd
	 */
	void dequeue(T action);

}
