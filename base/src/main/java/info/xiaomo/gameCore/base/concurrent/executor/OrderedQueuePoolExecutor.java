package info.xiaomo.gameCore.base.concurrent.executor;

import info.xiaomo.gameCore.base.concurrent.AbstractCommand;
import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;
import info.xiaomo.gameCore.base.concurrent.queue.LockedCommandQueue;
import info.xiaomo.gameCore.base.concurrent.queue.OrderedQueuePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 对ThreadPoolExecutor的封装，该Executor中执行的任务都是基于添加时间有序的。
 *
 * @author Administrator
 */
public class OrderedQueuePoolExecutor extends ThreadPoolExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderedQueuePoolExecutor.class);
    /**
     * 队列池
     */
    private final OrderedQueuePool<Long, AbstractCommand> pool;
    //执行器名称
    private String name;
    //最小线程数，并且最大线程数是最小线程数的2倍
    private int corePoolSize;
    //队列中最大的元素个数
    private int maxQueueSize;

    /**
     * 创建一个Executor
     *
     * @param name 名称
     * @param corePoolSize 最小线程数
     * @param maxQueueSize 队列中最大的元素个数
     */
    public OrderedQueuePoolExecutor(final String name, int corePoolSize, int maxQueueSize) {
    	
        super(corePoolSize, 2 * corePoolSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        	int count = 0;
        	@Override
			public Thread newThread(Runnable r) {
        		count += 1;
				return new Thread(r, name + "-" + count);
			}
		});
        this.pool = new OrderedQueuePool<>();
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maxQueueSize = maxQueueSize;
    }

    /**
     * 使用指定的最小线程数构建一个Executor，该Executor的那么和maxQueueSize都使用默认值. 默认值
     * name:queue-pool maxQueueSize:10000
     *
     * @param corePoolSize
     */
    public OrderedQueuePoolExecutor(int corePoolSize) {
        this("queue-pool", corePoolSize, 10000);
    }

    /**
     * 执行一个任务
     *
     * @param key 该任务的key，通过可以计算使用队列池中的哪个队列
     * @param task 要执行的任务
     * @return
     */
    public boolean addTask(Long key, AbstractCommand task) {
        //计算需要放进的队列的key
        key = key % this.corePoolSize;
        //获取队列
        LockedCommandQueue<AbstractCommand> queue = this.pool.getCommandQueue(key);
        boolean run = false;
        boolean result;
        synchronized (queue) {
            //队列中的元素已经超过允许的最大个数时，就将改队列清空，丢弃多有的指令
            if (this.maxQueueSize > 0 && queue.size() > this.maxQueueSize) {
            	LOGGER.error("队列" + this.name + "(" + key + ")" + "抛弃指令!");
                queue.clear();
            }
            result = queue.offer(task);
            if (result) {
                //将改队列放进task中
                task.setCommandQueue(queue);
                if (queue.isProcessingCompleted()) {
                    //如果该队列中的所有task都已经执行完毕，那么重新启动该队列的执行
                    queue.setProcessingCompleted(false);
                    run = true;
                }
            } else {
            	LOGGER.error("队列添加任务失败");
            }
        }
        if (run) {//启动任务
            execute(queue.poll());
        }
        return result;
    }

    /**
     * 指定的任务执行完毕后，调用该方法
     *
     * @param task 执行的任务
     * @param throwable 异常
     */
    @Override
    protected void afterExecute(Runnable task, Throwable throwable) {
        super.afterExecute(task, throwable);
        AbstractCommand work = (AbstractCommand) task;
       
        ICommandQueue<AbstractCommand> queue = work.getCommandQueue();
        if (queue != null) {
            AbstractCommand afterWork;
            synchronized (queue) {
                afterWork =  queue.poll();
                if (afterWork == null) {
                     //执行完毕后如果队列中没有任务了，那么设置执行完毕标志
                    queue.setProcessingCompleted(true);
                }
                //执行完毕后如果队列中还有任务，那么继续执行下一个
                if (afterWork != null) {
                	execute(afterWork);
                }
            }
        } else {
        	LOGGER.info("执行队列为空");
        }
    }

    /**
     * 获得目前队列中的任务数量
     *
     * @return 队列数量
     */
    public int getTaskCounts() {
        int count = super.getActiveCount();
        for (Map.Entry<Long,LockedCommandQueue<AbstractCommand>> entry : this.pool.getCommandQueue().entrySet()) {
        	LockedCommandQueue<AbstractCommand> tasksQueue =  entry.getValue();
            count += tasksQueue.size();
        }
        return count;
    }
}
