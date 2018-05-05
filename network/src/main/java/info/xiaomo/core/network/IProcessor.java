package info.xiaomo.core.network;


import info.xiaomo.core.base.concurrent.command.IQueueDriverCommand;

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
