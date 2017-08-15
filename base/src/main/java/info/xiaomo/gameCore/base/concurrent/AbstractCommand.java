package info.xiaomo.gameCore.base.concurrent;


import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;


/**
 * 用于执行具体业务逻辑的工作类，该类的实例中包括一个CommandQueue
 *
 * @author Administrator
 */
public abstract class AbstractCommand implements IQueueCommand {
    /**
     * 所属的队列
     */
    protected ICommandQueue<IQueueCommand> commandQueue;
    /**
     * 所属的队列ID
     */
    protected int queueId;

    public ICommandQueue<IQueueCommand> getCommandQueue() {
        return commandQueue;
    }

    public void setCommandQueue(ICommandQueue<IQueueCommand> commandQueue) {
        this.commandQueue = commandQueue;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Override
    public Object getParam() {
        return null;
    }

    @Override
    public void setParam(Object param) {
    }
}
