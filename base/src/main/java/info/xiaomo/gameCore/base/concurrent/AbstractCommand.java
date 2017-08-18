package info.xiaomo.gameCore.base.concurrent;


import info.xiaomo.gameCore.base.concurrent.executor.QueueMonitor;
import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用于执行具体业务逻辑的工作类，该类的实例中包括一个CommandQueue
 *
 * @author Administrator
 */
@Data
public abstract class AbstractCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);

    private ICommandQueue<AbstractCommand> commandQueue;


    /**
     * 消息所属队列ID（有可能是场景队列，也有可能是其他队列）
     */
    protected int queueId;

    @Override
    public void run() {
        try {

            if (QueueMonitor.open && commandQueue != null) {

                QueueMonitor monitor = commandQueue.getMonitor();
                if (monitor != null) {

                    long time = System.currentTimeMillis();

                    doAction();

                    int total = (int) (System.currentTimeMillis() - time);

                    monitor.monitor(this, time, total);
                } else {
                    doAction();
                }
            } else {
                doAction();
            }

        } catch (Throwable e) {
            LOGGER.error("命令执行错误", e);
        }
    }

}
