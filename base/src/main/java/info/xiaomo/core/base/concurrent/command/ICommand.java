package info.xiaomo.core.base.concurrent.command;

@FunctionalInterface
public interface ICommand extends Runnable {
    void doAction();

    @Override
    default void run() {
        doAction();
    }


}
