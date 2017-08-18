package info.xiaomo.gameCore.base.concurrent.executor;

import info.xiaomo.gameCore.base.concurrent.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 无序的Executor，该类对ThreadPoolExecutor做了简单的封装.
 * 1、增加execute(commnd)方法
 * 2、增加Handler执行超时日志输出的功能
 * @author Administrator
 */
public class NonOrderedQueuePoolExecutor extends ThreadPoolExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NonOrderedQueuePoolExecutor.class);

    /**
     * 构建一个最小和最大线程数都是corePoolSize的Executor
     * @param corePoolSize 最大和最小线程数
     */
    public NonOrderedQueuePoolExecutor(int corePoolSize) {
        super(corePoolSize, corePoolSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 执行一个Handler.
     * 该方法用Handler创建一个Work，然后执行
     * @param command handler 
     */
    public void execute(ICommand command) {
        Work work = new Work(command);
        execute(work);
    }

    /**
     * 执行任务，Work接受一个Handler作为构建参数，在run方法中执行handler.action.
     * Work的run方法中输出超时日志
     */
    private class Work implements Runnable {

        private final ICommand command;

        public Work(ICommand command) {
            this.command = command;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try {
				this.command.doAction();
			} catch (Exception e) {
				e.printStackTrace();
			}
            long end = System.currentTimeMillis();
            if (end - start > 50L) {
            	LOGGER.info("NonOrderedQueuePoolExecutor-->" + this.command.getClass().getSimpleName() + " run:" + (end - start));
            }
        }
    }
}
