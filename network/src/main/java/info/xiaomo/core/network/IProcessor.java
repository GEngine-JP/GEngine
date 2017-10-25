package info.xiaomo.core.network;


import info.xiaomo.core.base.concurrent.command.IQueueDriverCommand;

public interface IProcessor {

    /**
     * process
     *
     * @param handler
     */
    void process(IQueueDriverCommand handler);

}
