package info.xiaomo.core.base.concurrent.command;

/**
 * @author qq
 */
@FunctionalInterface
public interface ICommand extends Runnable {
    /**
     * 执行
     */
    void doAction();

    /**
     * 运行
     */
    @Override
    default void run() {
        doAction();
    }


}
