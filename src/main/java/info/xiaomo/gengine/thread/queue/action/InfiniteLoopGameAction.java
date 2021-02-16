package info.xiaomo.gengine.thread.queue.action;


import info.xiaomo.gengine.thread.queue.IActionQueue;

/**
 * 无限循环
 *
 * @author HW-fanjaiwei
 */
public abstract class InfiniteLoopGameAction extends DelayGameAction {


	public InfiniteLoopGameAction(IActionQueue<GameAction, DelayGameAction> queue, int delay) {
		super(queue, delay);
	}

	@Override
	public void execute() {
		try {
			loopExecute();
		} catch (Exception e) {
			throw e;
		} finally {
			execTime = System.currentTimeMillis() + delay;
			getActionQueue().enDelayQueue(this);
		}
	}

	/**
	 * 循环执行接口
	 */
	public abstract void loopExecute();

}
