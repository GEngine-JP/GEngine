package info.xiaomo.gameCore.base;

import info.xiaomo.gameCore.base.concurrent.AbstractCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消息处理器
 *
 * @author 张力
 * @date 2014-12-4 上午10:39:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractHandler extends AbstractCommand {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    protected Object message;

    /**
     * 参数,这个参数一般来是用来存放该handler的玩家信息的
     */
    protected Object param;


    /**
     * 具体业务逻辑
     */
    protected abstract void execute();

    /**
     * 执行消息的动作
     */
    public void doAction() {
        execute();
    }

    private long startTime = 0;


}
