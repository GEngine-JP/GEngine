package info.xiaomo.gengine.network;

import info.xiaomo.gengine.concurrent.command.IQueueDriverCommand;

/** @author xiaomo */
public interface IProcessor {

    /**
     * process
     *
     * @param handler handler
     */
    void process(IQueueDriverCommand handler);
}
