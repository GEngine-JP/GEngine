package info.xiaomo.gameCore.base.concurrent.queue;


import info.xiaomo.gameCore.base.concurrent.executor.QueueMonitor;

public interface ICommandQueue<V> {

	/**
	 * 下一执行命令
	 * 
	 * @return
	 */
	V poll();

	/**
	 * 增加执行指令
	 * 
	 * @return
	 */
	boolean offer(V value);

	/**
	 * 清理
	 */
	void clear();

	/**
	 * 获取指令数量
	 * 
	 * @return
	 */
	int size();

	boolean isProcessingCompleted();

	void setProcessingCompleted(boolean processingCompleted);

	QueueMonitor getMonitor();

	void setMonitor(QueueMonitor monitor);



}
