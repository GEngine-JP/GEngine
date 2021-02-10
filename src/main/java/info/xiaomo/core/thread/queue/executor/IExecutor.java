package info.xiaomo.core.thread.queue.executor;

/**
 * 执行器接口定义
 *
 * @param <T>
 * @author laofan
 */
public interface IExecutor<T extends Runnable> {

	/**
	 * 执行任务
	 *
	 * @param cmdTask
	 */
	void execute(T cmdTask);

	/**
	 * 停止所有线程
	 */
	void stop();

}
