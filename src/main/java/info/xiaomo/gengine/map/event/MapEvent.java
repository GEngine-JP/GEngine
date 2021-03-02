package info.xiaomo.gengine.map.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件，该事件通过trigger和executor完成相应的功能
 *
 * @author zhangli
 * 2017年5月18日 下午8:35:39
 */
@Data
@AllArgsConstructor
@Slf4j
public class MapEvent implements Comparable<MapEvent> {
	private int id;
	private int priority;

	private long groundEventId;

	private EventTrigger trigger;
	private EventExecutor executor;


	@Override
	public int compareTo(MapEvent other) {
		return Integer.compare(priority, other.priority);
	}

}
