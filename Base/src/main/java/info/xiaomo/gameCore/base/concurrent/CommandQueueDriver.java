package info.xiaomo.gameCore.base.concurrent;

import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;
import info.xiaomo.gameCore.base.concurrent.queue.UnlockedCommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务队列驱动器
 */
public class CommandQueueDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandQueueDriver.class);

    /**
     * 队列最大数量
     */
    private int maxQueueSize;

    /**
     * 驱动名称
     */
    private String name;

    /**
     * 队列ID
     */
    private long queueId;

    /**
     * 任务队列
     */
    private ICommandQueue<IQueueCommand> queue;

    /**
     * 任务执行器
     */
    private CommandQueueExecutor executor;

    public CommandQueueDriver(CommandQueueExecutor executor, String name, long queueId, int maxQueueSize) {
        this(executor, name, queueId, maxQueueSize, new UnlockedCommandQueue(name));
    }

    public CommandQueueDriver(CommandQueueExecutor executor, String name, long queueId, int maxQueueSize, ICommandQueue queue) {
        this.executor = executor;
        this.name = name;
        this.queueId = queueId;
        this.maxQueueSize = maxQueueSize;
        this.queue = queue;
    }

    /**
     * 提交一个队列命令
     *
     * @param command
     */
    public boolean submit(IQueueCommand command) {
        if (command.getQueueId() > 0 && command.getQueueId() != this.queueId) {
            // LOGGER.error("场景驱动[" + this.name + "]-" + queueId +
            // "执行到不属于本场景的命令,命令所在场景：" + command.getQueueId());
            return false;
        }

        if (queue.size() > 200) {
            // LOGGER.error("场景驱动[" + this.name + "]-" + queueId + "队列长度超过200<"
            // + queue.size());
        }

        boolean result;
        synchronized (queue) {
            // 队列中的元素已经超过允许的最大个数时，就将改队列清空，丢弃多有的指令
            if (this.maxQueueSize > 0 && queue.size() > this.maxQueueSize) {
                // LOGGER.error("场景驱动[" + this.name + "]-" + queueId + "抛弃指令!" +
                // Thread.currentThread().getName());
                queue.clear();
            }

            result = queue.offer(command);
            if (result) {
                // 设置command的queue属性
                command.setCommandQueue(queue);
                if (!queue.isRunning()) {
                    queue.setRunning(true);
                    executor.execute(queue.poll());
                }
            } else {
                LOGGER.error("队列添加任务失败");
            }
        }
        return result;
    }

    /**
     * 提交一个命令
     *
     * @param command
     * @return
     */
    public boolean submit(ICommand command) {
        return submit(new AbstractCommand() {
            @Override
            public void action() {
                command.action();
            }
        });
    }

    /**
     * 获取任务队列个数
     *
     * @return
     */
    public int getQueueSize() {
        return queue.size();
    }

    /**
     * 获取最大任务个数
     *
     * @return
     */
    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
