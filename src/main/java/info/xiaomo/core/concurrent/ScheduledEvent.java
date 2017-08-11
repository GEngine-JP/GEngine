package info.xiaomo.core.concurrent;

/**
 * 定时事件
 *
 * @author 张力
 * @date 2015-3-11 上午5:47:44
 */
public abstract class ScheduledEvent extends AbstractCommand {
    private long end;
    private int loop;
    private long initialDelay;
    private long step;

    protected ScheduledEvent() {
        this(1, 0, 0);
    }

    protected ScheduledEvent(int loop, int step) {
        this(loop, 0, step);
    }

    protected ScheduledEvent(int loop, int initialDelay, int step) {
        init(loop, initialDelay, step);
    }

    public void init(int loop, int initialDelay, int step) {
        this.loop = loop;
        this.initialDelay = initialDelay;
        this.step = step;
        this.end = System.nanoTime() + 1000000L * this.initialDelay;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
        this.end += 1000000L * step; //换算成纳秒
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public long remain() {
        return this.end - System.nanoTime();
    }
}