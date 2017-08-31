package info.xiaomo.gameCore.base.concurrent;

import info.xiaomo.gameCore.base.command.AbstractCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时事件
 *
 * @author 张力
 * @date 2015-3-11 上午5:47:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ScheduledEvent extends AbstractCommand {

    // 定时结束时间
    private long end;

    // 定时剩余时间
    private long remain;

    // 执行次数
    private int loop;

    // 间隔时间
    private long delay;

    /**
     * 计时事件
     *
     * @param end 执行事件
     */
    protected ScheduledEvent(long end) {
        this.end = end;
        this.loop = 1;
    }

    /**
     * 循环事件
     *
     * @param loop  循环次数
     * @param delay 间隔时间
     */
    protected ScheduledEvent(int loop, long delay) {
        this.loop = loop;
        this.delay = delay;
        this.end = (System.currentTimeMillis() + delay);
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}