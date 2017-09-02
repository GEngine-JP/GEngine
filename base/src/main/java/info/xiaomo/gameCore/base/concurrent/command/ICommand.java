package info.xiaomo.gameCore.base.concurrent.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface ICommand extends Runnable {
    Logger LOGGER = LoggerFactory.getLogger(ICommand.class);

    void doAction();

    @Override
    default void run() {
        doAction();
    }


}
