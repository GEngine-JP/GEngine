package info.xiaomo.gengine.thread;

import java.util.concurrent.Executor;

/**
 * 自定义线程池
 * <p>
 * <p>
 * 2017-04-21
 */
public interface ExecutorPool extends Executor {

	/**
	 * 关闭线程
	 */
	void stop();


}
