package info.xiaomo.gengine.script;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 心跳脚本，最低按秒循环
 *
 *
 *  2017-03-30
 */
public interface ITimerEventScript extends IScript {

	/**
	 * 每秒执行
	 *
	 * @param localTime
	 */
	default void secondHandler(LocalTime localTime) {

	}

	default void minuteHandler(LocalTime localTime) {

	}

	default void hourHandler(LocalTime localTime) {

	}

	default void dayHandler(LocalDateTime localDateTime) {

	}
}
