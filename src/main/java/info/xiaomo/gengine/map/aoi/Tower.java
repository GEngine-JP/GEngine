package info.xiaomo.gengine.map.aoi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.map.obj.IMapObject;
import info.xiaomo.gengine.map.obj.MapObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 灯塔
 *
 * @author Administrator
 */
public class Tower {

	private static final Logger LOGGER = LoggerFactory.getLogger(Tower.class);

	/**
	 * 所有对象的集合
	 */
	private final Map<Long, IMapObject> objectMap = new HashMap<>();

	/**
	 * 观察者(只有玩家一种类型)
	 */
	private final Map<Long, IMapObject> watchers = new HashMap<>();

	/**
	 * 该灯塔类的所有类型的玩家
	 */
	private final Map<Integer, Map<Long, IMapObject>> typeOfObjectMap = new HashMap<>();

	/**
	 * 灯塔中游戏对象的数量
	 */
	private long size = 0;

	private int x = 0;

	private int y = 0;

	/**
	 * 向灯塔中添加一个游戏对象
	 *
	 * @param obj
	 * @return
	 */
	public boolean addObject(IMapObject obj) {
		if (obj == null) {
			return false;
		}
		objectMap.put(obj.getId(), obj);
		Map<Long, IMapObject> map = this.typeOfObjectMap.computeIfAbsent(obj.getType(), k -> new HashMap<>());
		if (map.containsKey(obj.getId())) {
			return false;
		}
		map.put(obj.getId(), obj);
		this.size++;
		return true;
	}

	/**
	 * 添加一个观察者到灯塔中
	 *
	 * @param obj
	 */
	public void addWatcher(IMapObject obj) {
		if (obj == null) {
			return;
		}
		this.watchers.put(obj.getId(), obj);
	}

	/**
	 * 从灯塔中移除一个观察者
	 *
	 * @param obj
	 */
	public void removeWatcher(IMapObject obj) {
		if (obj != null) {
			IMapObject ret = this.watchers.remove(obj.getId());
			if (ret == null) {
				LOGGER.error("移除观察者失败：" + obj.getName() + "[" + this.x + "," + this.y + "]");
			} else {
				LOGGER.debug("移除观察者成功：" + obj.getName() + "[" + this.x + "," + this.y + "]");
			}
		}
	}

	/**
	 * 获取所有观察者
	 *
	 * @return
	 */
	public Map<Long, IMapObject> getWatchers() {
		return this.watchers;
	}

	/**
	 * 从灯塔中移除一个对象
	 *
	 * @param obj
	 */
	public void removeObject(IMapObject obj) {
		boolean isPlayer = obj.getType() == MapObjectType.Player;
		obj = objectMap.remove(obj.getId());
		if (obj != null) {
			if (this.typeOfObjectMap.containsKey(obj.getType())) {
				this.typeOfObjectMap.get(obj.getType()).remove(obj.getId());
			}
			this.size--;
			if (isPlayer)
				LOGGER.info("{}移除灯塔：[{}-{}]", obj.getName(), this.x, this.y);
		} else {
			if (isPlayer)
				LOGGER.info("{}移除灯塔失败，灯塔里没有这个对象：[{}-{}]", obj.getName(), this.x, this.y);
		}
	}

	/**
	 * 获取指定类型的ID列表
	 *
	 * @param type
	 * @return
	 */
	public Map<Long, IMapObject> getObjectByType(int type) {
		if (this.typeOfObjectMap.containsKey(type)) {
			return this.typeOfObjectMap.get(type);
		}
		return Collections.emptyMap();
	}

	/**
	 * 获取所有游戏对象
	 *
	 * @return
	 */
	public Map<Long, IMapObject> getAllObject() {
		return objectMap;
	}

	public void clear() {
		this.objectMap.clear();
		this.watchers.clear();
		this.typeOfObjectMap.clear();
	}

	public long size() {
		return size;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
