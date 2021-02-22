package info.xiaomo.gengine.concurrent.command;


import info.xiaomo.gengine.concurrent.queue.ICommandQueue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * 用于执行具体业务逻辑的工作类，该类的实例中包括一个CommandQueue
 *
 * @author Administrator
 */
@Data
@Slf4j
public abstract class AbstractCommand implements IQueueDriverCommand {

    private ICommandQueue<IQueueDriverCommand> commandQueue;

    /**
     * 消息所属队列ID
     */
    protected int queueId;

    @Override
    public Object getParam() {
        return null;
    }

    @Override
    public void setParam(Object param) {

    }


}
