package info.xiaomo.gengine.map.aoi;

import java.util.List;
import java.util.Map;
import info.xiaomo.gengine.map.obj.IMapObject;

/**
 * 视野接口
 *
 * @author zhangli
 * 2017年6月6日 下午9:57:39
 */
public interface AOIEventListener {

	/**
	 * 视野内添加了对象事件
	 *
	 * @param obj
	 * @param watchers
	 */
	void onAdd(IMapObject obj, Map<Long, IMapObject> watchers);

	/**
	 * 视野中移除了对象事件
	 *
	 * @param obj
	 * @param watchers
	 */
	void onRemove(IMapObject obj, Map<Long, IMapObject> watchers);

	/**
	 * 被观察者移动之后，通知观察者 该被观察者的位置变更
	 *
	 * @param obj
	 * @param oldWatchers
	 * @param updateWatchers
	 * @param newWatchers
	 */
	void onUpdate(IMapObject obj, Map<Long, IMapObject> oldWatchers, Map<Long, IMapObject> updateWatchers,
	              Map<Long, IMapObject> newWatchers);

	/**
	 * 观察者自己移动之后，视野更新事件
	 *
	 * @param obj
	 * @param addObjectList
	 * @param removeObjectList
	 */
	void onUpdateWatcher(IMapObject obj, List<IMapObject> addObjectList, List<IMapObject> removeObjectList);

}
