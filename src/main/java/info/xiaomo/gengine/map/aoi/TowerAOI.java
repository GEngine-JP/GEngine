package info.xiaomo.gengine.map.aoi;

import java.util.*;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.obj.IMapObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于灯塔的视野管理
 *
 * @author zhangli
 * 2017年6月6日 下午9:58:04
 */
public class TowerAOI {

	private static final Logger LOGGER = LoggerFactory.getLogger(TowerAOI.class);

	public static final int WIDTH = 6;
	public static final int HEIGHT = 5;
	public static final int RANGE_LIMIT = 5;

	public static final int RANGE_DEFAULT = 2;

	/**
	 * 地图宽度
	 */
	private final int width;

	/**
	 * 地图高度
	 */
	private final int height;

	/**
	 * 灯塔的宽度
	 */
	private final int towerWidth;

	/**
	 * 灯塔的高度
	 */
	private final int towerHeight;

	/**
	 * 遍历的时候最大返回限制
	 */
	private final int rangeLimit;

	/**
	 * x最大值
	 */
	private int maxX;

	/**
	 * y最大值
	 */
	private int maxY;

	/**
	 * 灯塔数组
	 */
	private Tower[][] towers;

	/**
	 * 事件监听者
	 */
	private final List<AOIEventListener> listeners = new ArrayList<>();

	/**
	 * 创建一个AOI对象
	 *
	 * @param width       地图宽度
	 * @param height      地图高度
	 * @param towerWidth  每个灯塔的宽度
	 * @param towerHeight 每个灯塔的高度
	 * @param rangeLimit  视野范围最大值
	 */
	public TowerAOI(int width, int height, int towerWidth, int towerHeight, int rangeLimit) {
		this.width = width;
		this.height = height;
		this.towerWidth = towerWidth;
		this.towerHeight = towerHeight;
		this.rangeLimit = rangeLimit;
		init();
	}

	/**
	 * 创建一个AOI对象,灯塔默认宽度为6 灯塔默认高度为5，最大视野范围为5
	 *
	 * @param width  地图宽度
	 * @param height 地图高度
	 */
	public TowerAOI(int width, int height) {
		this.width = width;
		this.height = height;
		this.towerWidth = WIDTH;
		this.towerHeight = HEIGHT;
		this.rangeLimit = RANGE_LIMIT;
		init();
	}

	/**
	 * 初始化地图中的灯塔
	 */
	private void init() {
		this.maxX = this.width / this.towerWidth;
		if (this.width % this.towerWidth != 0) {
			this.maxX++;// 多余的多出一格子
		}
		this.maxY = this.height / this.towerHeight;
		if (this.height % this.towerHeight != 0) {
			this.maxY++;// 多余的多出一格子
		}
		towers = new Tower[maxX + 1][this.maxY + 1];
		for (int i = 0; i <= this.maxX; i++) {
			for (int j = 0; j <= this.maxY; j++) {
				towers[i][j] = new Tower();
				towers[i][j].setX(i);
				towers[i][j].setY(j);
			}
		}
	}

	/**
	 * 获取Pos周围range大小灯塔中所有指定类型的对象ID
	 *
	 * @param pos
	 * @param range
	 * @param type
	 * @return
	 */
	public List<IMapObject> getObjectListByRangeAndType(MapPoint pos, int range, int type) {
		if (range < 0 || range > this.rangeLimit || !checkPos(pos)) {
			return Collections.emptyList();
		}

		List<IMapObject> ret = new ArrayList<>();

		MapPoint tp = transPos(pos);
		int round = 2 * range + 1;
		int num = round * round;
		for (int i = 0; i < num; i++) {
			int x = tp.getX() + i % round - range;
			int y = tp.getY() + i / round - range;
			if (x < 0 || y < 0 || x > this.maxX || y > this.maxY) {
				continue;
			}
			Tower tower = towers[x][y];
			ret.addAll(tower.getObjectByType(type).values());

		}
		return ret;
	}

	public List<IMapObject> getObjectListByRangeAndType(MapPoint pos, int range, List<Integer> types) {
		if (range < 0 || range > this.rangeLimit || !checkPos(pos)) {
			return Collections.emptyList();
		}

		List<IMapObject> ret = new ArrayList<>();

		MapPoint tp = transPos(pos);
		int round = 2 * range + 1;
		int num = round * round;
		for (int i = 0; i < num; i++) {
			int x = tp.getX() + i % round - range;
			int y = tp.getY() + i / round - range;
			if (x < 0 || y < 0 || x > this.maxX || y > this.maxY) {
				continue;
			}
			Tower tower = null;
			try {
				tower = towers[x][y];
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int type : types) {
				ret.addAll(tower.getObjectByType(type).values());
			}
		}
		return ret;
	}

	/**
	 * 通过pos和range获取对象列表
	 *
	 * @param pos
	 * @param range
	 * @return
	 */
	public List<IMapObject> getObjectListByRange(MapPoint pos, int range) {
		if (!this.checkPos(pos) || range < 0) {
			return Collections.emptyList();
		}
		range = Math.min(range, this.rangeLimit);
		List<IMapObject> ret = new ArrayList<>();
		MapPoint tp = transPos(pos);
		PosLimit limit = getPosLimit(tp, range);
		for (int i = limit.getStartX(); i <= limit.getEndX(); i++) {
			for (int j = limit.getStartY(); j <= limit.getEndY(); j++) {
				Tower tower = towers[i][j];
				ret.addAll(tower.getAllObject().values());
			}
		}
		return ret;
	}

	/**
	 * 添加一个对象，将对象放入坐标对应的灯塔里面，向灯塔中的观察者触发一个 新对象添加事件
	 *
	 * @param obj
	 * @param pos
	 * @return
	 */
	public boolean addObject(IMapObject obj, MapPoint pos) {
		if (!this.checkPos(pos)) {
			return false;
		}
		MapPoint tp = this.transPos(pos);
		this.towers[tp.getX()][tp.getY()].addObject(obj);
		//System.out.println("Tian - monster->"+ obj.getName());
		// 触发对象加入事件
		fireAddObjectEvent(obj, this.towers[tp.getX()][tp.getY()].getWatchers());
		return true;

	}

	/**
	 * 删除一个对象，将对象从坐标对应的灯塔里面移除，向灯塔中的观察者触发一个 对象移除事件
	 *
	 * @param obj
	 * @param pos
	 * @return
	 */
	public boolean removeObject(IMapObject obj, MapPoint pos) {
		if (!this.checkPos(pos)) {
			return false;
		}

		MapPoint tp = this.transPos(pos);
		this.towers[tp.getX()][tp.getY()].removeObject(obj);
		// 触发对象删除事件
		fireRemoveObjectEvent(obj, this.towers[tp.getX()][tp.getY()].getWatchers());
		return true;
	}

	/**
	 * 更新一个游戏对象，该方法会触发一个update事件，此事件通知其他观察者，我消失或者进入了你的视野</br>
	 * 注意：该方法的触发的事件不包括自己
	 *
	 * @param obj
	 * @param oldPos
	 * @param newPos
	 * @return
	 */
	public boolean updateObject(IMapObject obj, MapPoint oldPos, MapPoint newPos) {
		if (!this.checkPos(oldPos) || !this.checkPos(newPos)) {
			return false;
		}

		MapPoint oldTp = this.transPos(oldPos);
		MapPoint newTp = this.transPos(newPos);

		if (oldTp.getX() == newTp.getX() && oldTp.getY() == newTp.getY()) {
			return true;
		}

		Tower oldTower = this.towers[oldTp.getX()][oldTp.getY()];
		Tower newTower = this.towers[newTp.getX()][newTp.getY()];

		Map<Long, IMapObject> oldWatchers = oldTower.getWatchers();
		Map<Long, IMapObject> newWatchers = newTower.getWatchers();

		Map<Long, IMapObject> updatePosWatchers = new HashMap<>();

		Map<Long, IMapObject> removeWatchers = new HashMap<>();
		Map<Long, IMapObject> addPosWatchers = new HashMap<>();

		for (Long id : oldWatchers.keySet()) {
			if (!newWatchers.containsKey(id)) {
				// 移除视野
				removeWatchers.put(id, oldWatchers.get(id));
			} else {
				// 更新视野
				updatePosWatchers.put(id, oldWatchers.get(id));

			}
		}
		for (Long id : newWatchers.keySet()) {
			if (!oldWatchers.containsKey(id)) {// 通知加入视野
				addPosWatchers.put(id, newWatchers.get(id));
			}
		}

		//updatePosWatchers.values().forEach((e) -> System.out.print(e.getName()));

		oldTower.removeObject(obj);
		newTower.addObject(obj);
		// 触发对象更新事
		fireUpdateObjectEvent(obj, removeWatchers, updatePosWatchers, addPosWatchers);
		return true;
	}


	public boolean addWatcher(IMapObject obj, MapPoint pos) {
		return addWatcher(obj, pos, RANGE_DEFAULT);
	}

	/**
	 * 添加事件观察者
	 *
	 * @param obj
	 * @param pos
	 * @param range
	 * @return
	 */
	public boolean addWatcher(IMapObject obj, MapPoint pos, int range) {
		if (range < 0 || !this.checkPos(pos)) {
			return false;
		}
		range = Math.min(range, 5);
		MapPoint tp = this.transPos(pos);
		PosLimit limit = getPosLimit(tp, range);
		for (int i = limit.getStartX(); i <= limit.getEndX(); i++) {
			for (int j = limit.getStartY(); j <= limit.getEndY(); j++) {
				Tower tower = towers[i][j];
				tower.addWatcher(obj);
			}
		}

		return true;
	}


	public boolean removeWatcher(IMapObject obj, MapPoint pos) {
		return removeWatcher(obj, pos, RANGE_DEFAULT);
	}

	/**
	 * 移除观察者
	 *
	 * @param obj
	 * @param pos
	 * @param range
	 * @return
	 */
	public boolean removeWatcher(IMapObject obj, MapPoint pos, int range) {
		if (range < 0 || !this.checkPos(pos)) {
			return false;
		}

		range = Math.min(range, 5);
		MapPoint tp = this.transPos(pos);
		PosLimit limit = getPosLimit(tp, range);
		for (int i = limit.getStartX(); i <= limit.getEndX(); i++) {
			for (int j = limit.getStartY(); j <= limit.getEndY(); j++) {
				Tower tower = towers[i][j];
				tower.removeWatcher(obj);
			}
		}
		return true;
	}

	/**
	 * 更新观察者，该方法会触发一个updateWatcher事件，此事件告诉观察者本身，我的视野游戏哪些变化
	 *
	 * @param obj
	 * @param oldPos
	 * @param newPos
	 * @return
	 */
	public boolean updateWatcher(IMapObject obj, MapPoint oldPos, MapPoint newPos) {
		return updateWatcher(obj, oldPos, newPos, RANGE_DEFAULT, RANGE_DEFAULT);
	}

	/**
	 * 更新观察者，该方法会触发一个updateWatcher事件，此事件告诉观察者本身，我的视野游戏哪些变化
	 *
	 * @param obj
	 * @param oldPos
	 * @param newPos
	 * @param oldRange
	 * @param newRange
	 * @return
	 */
	public boolean updateWatcher(IMapObject obj, MapPoint oldPos, MapPoint newPos, int oldRange, int newRange) {
		if (!this.checkPos(oldPos) || !this.checkPos(newPos)) {
			return false;
		}

		MapPoint newTp = this.transPos(newPos);
		MapPoint oldTp = this.transPos(oldPos);
		if (newTp.getX() == oldTp.getX() && newTp.getY() == oldTp.getY()) {
			// System.out.println("在同一个灯塔范围内无需更新");
			return true;
		}

		if (oldRange < 0 || newRange < 0) {
			return false;
		}

		TowerChange tc = getChangeTowers(oldTp, newTp, oldRange, newRange);

		if (tc.getAddTowers().isEmpty() && tc.getRemoveTowers().isEmpty()) {
			// System.out.println("边角的两个灯塔之间没有改变和增加");
			return true;
		}

		List<IMapObject> addObjectList = new ArrayList<>();
		List<IMapObject> removeObjectList = new ArrayList<>();
		for (Tower tower : tc.getAddTowers()) {
			tower.addWatcher(obj);
			addObjectList.addAll(tower.getAllObject().values());
		}

		for (Tower tower : tc.getRemoveTowers()) {
			tower.removeWatcher(obj);
			removeObjectList.addAll(tower.getAllObject().values());
		}
		/*
		 * System.out.println("需要增加的Tower:" + tc.getAddTowers().size());
		 * System.out.println("需要删除的Tower:" + tc.getRemoveTowers().size());
		 * System.out.println("保持不变的Tower:" + tc.getUnChangeTowers().size());
		 */

		// 触发Watcher Update 事件
		fireUpdateWatcherEvent(obj, addObjectList, removeObjectList);
		return true;
	}

	/**
	 * 获取指定类型的观察者
	 *
	 * @param pos
	 * @return
	 */
	public Map<Long, IMapObject> getWatchers(MapPoint pos) {
		if (!this.checkPos(pos)) {
			return Collections.emptyMap();
		}
		MapPoint tp = this.transPos(pos);
		return this.towers[tp.getX()][tp.getY()].getWatchers();
	}

	/**
	 * 检查坐标的正确性
	 *
	 * @param pos
	 * @return
	 */
	private boolean checkPos(MapPoint pos) {
		return pos != null
				&& !(pos.getX() < 0 || pos.getY() < 0 || pos.getX() > this.width || pos.getY() > this.height);
	}

	/**
	 * 将地图坐标转换成灯塔坐标
	 *
	 * @param pos
	 * @return
	 */
	private MapPoint transPos(MapPoint pos) {
		return new MapPoint(pos.getX() / this.towerWidth, pos.getY() / this.towerHeight);
	}

	/**
	 * 获取指定范围的格子限制坐标
	 *
	 * @param pos
	 * @param range
	 * @return
	 */
	private PosLimit getPosLimit(MapPoint pos, int range) {

		int startX;
		int startY;

		int endX;
		int endY;

		if (pos.getX() - range < 0) {
			startX = 0;
			endX = 2 * range - 1;
		} else if (pos.getX() + range > this.maxX) {
			endX = this.maxX;
			startX = this.maxX - 2 * range + 1;
		} else {
			startX = pos.getX() - range;
			endX = pos.getX() + range;
		}

		if (pos.getY() - range < 0) {
			startY = 0;
			endY = range * 2 - 1;
		} else if (pos.getY() + range > this.maxY) {
			endY = this.maxY;
			startY = this.maxY - range * 2 + 1;
		} else {
			startY = pos.getY() - range;
			endY = pos.getY() + range;
		}

		startX = Math.max(startX, 0);
		endX = Math.min(endX, this.maxX);
		startY = Math.max(startY, 0);
		endY = Math.min(endY, this.maxY);
		return new PosLimit(startX, endX, startY, endY);
	}

	/**
	 * 获取两个做个坐标之间变化的灯塔
	 *
	 * @param oldTp
	 * @param newTp
	 * @param oldRange
	 * @param newRange
	 * @return
	 */
	public TowerChange getChangeTowers(MapPoint oldTp, MapPoint newTp, int oldRange, int newRange) {
		PosLimit oldLimit = getPosLimit(oldTp, oldRange);
		PosLimit newLimit = getPosLimit(newTp, newRange);
		List<Tower> removeTowers = new ArrayList<>();
		List<Tower> addTowers = new ArrayList<>();
		List<Tower> unChangeTowers = new ArrayList<>();

		for (int x = oldLimit.getStartX(); x <= oldLimit.getEndX(); x++) {
			for (int y = oldLimit.getStartY(); y <= oldLimit.getEndY(); y++) {
				if (isInRect(x, y, newLimit)) {
					// 不变的Tower
					unChangeTowers.add(towers[x][y]);
				} else {
					// 需要移除的Tower
					removeTowers.add(towers[x][y]);
				}
			}
		}
		// 计算新增的Tower
		for (int x = newLimit.getStartX(); x <= newLimit.getEndX(); x++) {
			for (int y = newLimit.getStartY(); y <= newLimit.getEndY(); y++) {
				if (!isInRect(x, y, oldLimit)) {
					addTowers.add(towers[x][y]);
				}
			}
		}

		int maxRemove = (oldRange * 2 + 1) * (oldRange * 2 + 1);
		int maxAdd = (newRange * 2 + 1) * (newRange * 2 + 1);
		int maxUnChange = (oldRange * 2 + 1) * (oldRange * 2 + 1);
		if (removeTowers.size() > maxRemove || addTowers.size() > maxAdd || unChangeTowers.size() > maxUnChange) {
			LOGGER.error("获取灯塔变更错误,灯塔变化太大");
		}
		return new TowerChange(removeTowers, addTowers, unChangeTowers);

	}

	/**
	 * 是否在矩形之中
	 *
	 * @param x
	 * @param y
	 * @param limit
	 * @return
	 */
	private boolean isInRect(int x, int y, PosLimit limit) {
		return x >= limit.getStartX() && x <= limit.getEndX() && y >= limit.getStartY() && y <= limit.getEndY();
	}

	/**
	 * 触发对象增加事件（发给观察者）
	 *
	 * @param obj
	 * @param watchers
	 */
	private void fireAddObjectEvent(IMapObject obj, Map<Long, IMapObject> watchers) {
		for (AOIEventListener listener : listeners) {
			listener.onAdd(obj, watchers);
		}
	}

	/**
	 * 触发对象移除事件（发给观察者）
	 *
	 * @param obj
	 * @param watchers
	 */
	private void fireRemoveObjectEvent(IMapObject obj, Map<Long, IMapObject> watchers) {
		for (AOIEventListener listener : listeners) {
			listener.onRemove(obj, watchers);
		}
	}

	/**
	 * 触发对象更新事件（发给观察者）
	 *
	 * @param obj
	 * @param oldWatchers
	 * @param updateWatchers
	 * @param newWatchers
	 */
	private void fireUpdateObjectEvent(IMapObject obj, Map<Long, IMapObject> oldWatchers,
	                                   Map<Long, IMapObject> updateWatchers, Map<Long, IMapObject> newWatchers) {
		for (AOIEventListener listener : listeners) {
			listener.onUpdate(obj, oldWatchers, updateWatchers, newWatchers);
		}
	}

	/**
	 * 触发观察者更新事件（发给玩家自己）
	 *
	 * @param obj
	 * @param addObjectList
	 * @param removeObjectList
	 */
	private void fireUpdateWatcherEvent(IMapObject obj, List<IMapObject> addObjectList,
	                                    List<IMapObject> removeObjectList) {
		for (AOIEventListener listener : listeners) {
			listener.onUpdateWatcher(obj, addObjectList, removeObjectList);
		}
	}

	/**
	 * 向AOI模块添加一个观察者
	 *
	 * @param listener
	 */
	public void addListener(AOIEventListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * 清空
	 */
	public void clear() {
		for (Tower[] towerArray : towers) {
			for (Tower tower : towerArray) {
				tower.clear();
			}
		}
	}


}
