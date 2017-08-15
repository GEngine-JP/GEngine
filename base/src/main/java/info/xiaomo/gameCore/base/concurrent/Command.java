/**
 * 创建日期:  2017年08月11日 12:02
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.base.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 杨 强
 */
public interface Command extends ICommand, Runnable {
    Logger LOGGER = LoggerFactory.getLogger(Command.class);

    @Override
    default void run() {
        try {
            action();
        } catch (Throwable e) {
            LOGGER.error("命令执行错误", e);
        }
    }
}
