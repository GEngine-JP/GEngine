package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;
import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public abstract class AbstractHandler<T> implements IQueueDriverCommand {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    protected T message;

    protected Object session;

    protected int queueId;

    /**
     * 过滤器
     */
    protected HandlerFilter filter;

    @Override
    public int getQueueId() {
        return queueId;
    }

    @Override
    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Override
    public ICommandQueue<IQueueDriverCommand> getCommandQueue() {
        return null;
    }

    @Override
    public void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue) {

    }

    @Override
    public Object getParam() {
        return session;
    }

    @Override
    public void setParam(Object session) {
        this.session = session;
    }

    @Override
    public void run() {
        try {
            long time = System.currentTimeMillis();
            if (filter != null && !filter.before(this)) {
                return;
            }
            doAction();
            LOGGER.warn(this.getClass().getSimpleName() + "耗时：" + (System.currentTimeMillis() - time) + "ms");
            if (filter != null) {
                filter.after(this);
            }
        } catch (Throwable e) {
            LOGGER.error("命令执行错误", e);
        }
    }

}
