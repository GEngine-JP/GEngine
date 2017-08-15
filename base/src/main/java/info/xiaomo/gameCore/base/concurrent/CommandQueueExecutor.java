package info.xiaomo.gameCore.base.concurrent;

import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 队列执行器</br>
 * 该executor执行完毕一个任务的时候，会自动从该任务所属队列中获取下一个任务执行，直到队列为空
 *
 * @author 张力
 * @date 2015-3-11 下午10:51:20
 */
public class CommandQueueExecutor extends ThreadPoolExecutor {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CommandQueueExecutor.class);

    /**
     * 执行器名称
     */
    private String name;

    public CommandQueueExecutor(final String name, int corePoolSize, int maxPoolSize) {

        super(corePoolSize, maxPoolSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new SimpleThreadFactory(name));
        this.name = name;
    }

    /**
     * 指定的任务执行完毕后，调用该方法
     *
     * @param task      执行的任务
     * @param throwable 异常
     */
    @Override
    protected void afterExecute(Runnable task, Throwable throwable) {

        super.afterExecute(task, throwable);
        IQueueCommand work = (IQueueCommand) task;
        ICommandQueue<IQueueCommand> queue = work.getCommandQueue();
        if (queue == null) {
            LOGGER.error("命令执行队列为空");
            return;
        }
        synchronized (queue) {
            IQueueCommand nextCommand = queue.poll();
            if (nextCommand == null) {
                // 执行完毕后如果队列中没有任务了，那么设置运行标记为false
                queue.setRunning(false);

            } else {
                // 执行完毕后如果队列中还有任务，那么继续执行下一个
                execute(nextCommand);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
