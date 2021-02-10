package info.xiaomo.core.network.netty;


import info.xiaomo.core.concurrent.command.IQueueDriverCommand;

/**
 * @author xiaomo
 */
public interface IProcessor {

    /**
     * process
     *
     * @param  handler handler
     */
    void process(IQueueDriverCommand handler);

}
