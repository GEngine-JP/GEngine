package info.xiaomo.core.base.concurrent.command;

/**
 * @author xiaomo
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
