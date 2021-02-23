package info.xiaomo.gengine.network;

import info.xiaomo.gengine.concurrent.command.IQueueDriverCommand;
import info.xiaomo.gengine.concurrent.queue.ICommandQueue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaomo
 */
@Data
@Slf4j
public abstract class AbstractHandler<T> implements IQueueDriverCommand {

    protected T message;

    protected ISession session;

    protected int queueId;

    /**
     * 过滤器
     */
    protected IHandlerFilter filter;

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
        this.session = (ISession) session;
    }

    @Override
    public void run() {
        try {
            long time = System.currentTimeMillis();
            if (filter != null && !filter.before(this)) {
                return;
            }
            doAction();
            log.warn(this.getClass().getSimpleName() + "耗时：" + (System.currentTimeMillis() - time) + "ms");
            if (filter != null) {
                filter.after(this);
            }
        } catch (Throwable e) {
            log.error("命令执行错误", e);
        }
    }

}
