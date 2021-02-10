package info.xiaomo.core.thread;

import java.util.concurrent.Executor;

/**
 * 自定义线程池
 *
 * 
 * @date 2017-04-21
 */
public interface ExecutorPool extends Executor {

	/**
	 * 关闭线程
	 */
	void stop();


}
