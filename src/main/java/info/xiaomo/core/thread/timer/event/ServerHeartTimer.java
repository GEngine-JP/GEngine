package info.xiaomo.core.thread.timer.event;

import java.time.LocalTime;
import info.xiaomo.core.script.ITimerEventScript;
import info.xiaomo.core.script.ScriptManager;
import info.xiaomo.core.thread.timer.TimerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器主定时器
 *
 * @author JiangZhiYong
 * @date 2017-04-09
 */
public class ServerHeartTimer extends TimerEvent {

	protected static final Logger log = LoggerFactory.getLogger(ServerHeartTimer.class);
	public static boolean GLOBAL_PROTECT;
	protected int hour = -1;
	protected int min = -1;
	protected int sec = -1;
	protected int date = -1;

	public ServerHeartTimer() {
	}

	@Override
	public void run() {
		LocalTime localTime = LocalTime.now();
		int _sec = localTime.getSecond();
		if (sec != _sec) { // 每秒钟执行
			sec = _sec;
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(ITimerEventScript.class,
					(ITimerEventScript script) -> {
						long start = System.currentTimeMillis();
						script.secondHandler(localTime);
						long executeTime = System.currentTimeMillis() - start;
						if (executeTime > 50) {
							log.warn("定时脚本[{}] 执行{}ms", script.getClass().getName(), executeTime);
						}
					});
		}
		int _min = localTime.getMinute();
		if (min != _min) { // 每分钟执行
			min = _min;
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(ITimerEventScript.class,
					(ITimerEventScript script) -> {
						long start = System.currentTimeMillis();
						script.minuteHandler(localTime);
						long executeTime = System.currentTimeMillis() - start;
						if (executeTime > 50) {
							log.warn("定时脚本[{}] 执行{}ms", script.getClass().getName(), executeTime);
						}
					});
		}
		int _hour = localTime.getHour();
		if (hour != _hour) { // 每小时执行
			hour = _hour;
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(ITimerEventScript.class,
					(ITimerEventScript script) -> {
						long start = System.currentTimeMillis();
						script.hourHandler(localTime);
						long executeTime = System.currentTimeMillis() - start;
						if (executeTime > 50) {
							log.warn("定时脚本[{}] 执行{}ms", script.getClass().getName(), executeTime);
						}
					});
		}
	}
}
