package info.xiaomo.gameCore.base.concurrent.command;


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
public abstract class AbstractCommand implements IQueueDriverCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);

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
