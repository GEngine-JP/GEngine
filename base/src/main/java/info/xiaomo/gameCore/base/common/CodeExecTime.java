/**
 * 创建日期:  2017年08月02日 15:00
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.base.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代码执行时间
 *
 * @author 杨 强
 */
public class CodeExecTime {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeExecTime.class);
    private long threshold;
    private long startTime;
    private long lastStepTime;

    public static CodeExecTime newCodeExecTime() {
        return new CodeExecTime();
    }

    public static CodeExecTime newCodeExecTime(long threshold) {
        return new CodeExecTime(threshold);
    }

    public CodeExecTime() {
        this.threshold = 10;
        this.startTime = System.nanoTime();
        this.lastStepTime = this.startTime;
    }

    public CodeExecTime(long threshold) {
        this.threshold = threshold;
        this.startTime = System.nanoTime();
        this.lastStepTime = this.startTime;
    }

    public boolean total(Logger logger, long threshold, String msg) {
        long currentTime = System.nanoTime();
        try {
            long time = currentTime - this.startTime;
            if (time >= threshold) {
                logger.error(StringUtil.format("total:代码执行超时:{}, 超时 {}>={} ns", msg, time, threshold));
                return true;
            }
        } finally {
            this.lastStepTime = currentTime;
        }

        return false;
    }

    public boolean total(Logger logger, String msg) {
        return this.total(logger, this.threshold, msg);
    }

    public boolean total(int threshold, String msg) {
        return this.total(LOGGER, threshold, msg);
    }

    public boolean total(String msg) {
        return this.total(LOGGER, this.threshold, msg);
    }

    public boolean step(Logger logger, long threshold, String msg) {
        long currentTime = System.nanoTime();
        try {
            long time = currentTime - this.lastStepTime;
            if (time >= threshold) {
                logger.error(StringUtil.format("total:代码执行超时:{}, 超时 {}>={} ns", msg, time, threshold));
                return true;
            }

        } finally {
            this.lastStepTime = currentTime;
        }

        return false;
    }

    public boolean step(Logger logger, String msg) {
        return this.step(logger, this.threshold, msg);
    }

    public boolean step(int threshold, String msg) {
        return this.step(LOGGER, threshold, msg);
    }

    public boolean step(String msg) {
        return this.step(LOGGER, this.threshold, msg);
    }

    public void restStep() {
        this.lastStepTime = System.nanoTime();
    }

    public void restTotal() {
        this.startTime = System.nanoTime();
        this.lastStepTime = this.startTime;
    }
}
