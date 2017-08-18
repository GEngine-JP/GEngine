package info.xiaomo.gameCore.base.concurrent.queue;


import info.xiaomo.gameCore.base.concurrent.executor.QueueMonitor;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全的命令队列 任务执行队列，对ArrayDeque的包装. 该队列有一个是否所有队列的任务都执行完毕的标志，用于向队列中添加任务的时候判断是否需要启动任务
 * 
 * @author Administrator
 * @param <V>
 */
public class LockedCommandQueue<V> implements ICommandQueue<V>{

	// 读写锁
	private Lock lock = new ReentrantLock();

	// 命令队列
	private final Queue<V> queue;

	// 队列中的任务是否执行完毕
	private boolean processingCompleted = true;

	/**
	 * 创建一个空队列
	 */
	public LockedCommandQueue() {
		queue = new ArrayDeque<V>();
	}

	/**
	 * 创建一个空的队列，并用指定的大小初始化该队列
	 * @param numElements
	 */
	public LockedCommandQueue(int numElements) {
		queue = new ArrayDeque<V>(numElements);
	}

	/**
	 * 下一执行命令
	 * 
	 * @return
	 */
	public V poll() {
		try {
			this.lock.lock();
			return this.queue.poll();
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * 增加执行指令
	 * 
	 * @return
	 */
	public boolean offer(V value) {
		try {
			this.lock.lock();
			return this.queue.offer(value);
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * 清理
	 */
	public void clear() {
		try {
			this.lock.lock();
			this.queue.clear();
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * 获取指令数量
	 * 
	 * @return
	 */
	public int size() {
		return this.queue.size();
	}

	public boolean isProcessingCompleted() {
		return this.processingCompleted;
	}

	public void setProcessingCompleted(boolean processingCompleted) {
		this.processingCompleted = processingCompleted;
	}


	@Override
	public QueueMonitor getMonitor() {
		return null;
	}

	@Override
	public void setMonitor(QueueMonitor monitor) {

	}
}
