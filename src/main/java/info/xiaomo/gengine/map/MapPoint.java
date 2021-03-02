package info.xiaomo.gengine.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.obj.IMapObject;
import info.xiaomo.gengine.map.obj.MapObjectType;
import info.xiaomo.gengine.map.util.GeomUtil;
import lombok.Data;

/**
 * 格子(更具体的点)，这个点绝对不允许自己new，只能从地图中获取，如果一定要传递x,y这样的结构，请使用 TwoTuple<Integer,
 * Integer>替代
 *
 * @author 张力
 */
@Data
public class MapPoint {

	private int id;

	public int x;

	public int y;

	// A*寻路相关的属性-总消耗
	private int f;

	// A*寻路相关的属性-h
	private int h;

	// A*寻路相关的属性-g
	private int g;

	// A*寻路相关的属性-父节点
	private MapPoint p;

	// A*寻路结果相关属性-子节点
	private MapPoint s;

	private boolean block;

	/**
	 * 是否安全点
	 */
	private boolean safe;

	/**
	 * 是否可挖掘
	 */
	private boolean digAble;

	private MapPoint[] nears;

	private boolean hasTransmitEvent = false;

	private boolean hasEvent = false;

	/**
	 * 格子中的对象
	 */
	private Map<Long, IMapObject> objectMap = new ConcurrentHashMap<>();

	/**
	 * 格子中的对象对象数量
	 */
	private List<IMapObject> objectList = new ArrayList<>();

	public MapPoint(String xy) {
		int _index = xy.indexOf('_');
		this.x = Integer.parseInt(xy.substring(0, _index));
		this.y = Integer.parseInt(xy.substring(_index + 1));
		this.id = GeomUtil.getPointId(x, y);
	}

	public MapPoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.id = GeomUtil.getPointId(x, y);
	}

	public MapPoint(int id) {
		this.x = id >>> 16;
		this.y = id & 0xFFFF;
	}

	/**
	 * 设置格子的相邻点
	 *
	 * @param dir   方向
	 * @param point 点
	 */
	public void putNear(Dir dir, MapPoint point) {

	}

	/**
	 * 是否可以站立在该格子里（包含重叠规则）
	 *
	 * @param obj
	 * @return
	 */
	public boolean canStand(IMapObject obj) {

		if (this.block)
			return false;

		if (this.safe)
			return true;

		for (Entry<Long, IMapObject> entry : objectMap.entrySet()) {
			IMapObject pointObj = entry.getValue();

			if (obj != null) {
				if (!pointObj.overlying(pointObj)) {
					return false;
				}
			}

		}
		return true;
	}


	/**
	 * 是否可以掉落道具（没有NPC和道具即可）
	 *
	 * @return
	 */
	public boolean hasNotNpcAndItem() {
		for (Entry<Long, IMapObject> entry : objectMap.entrySet()) {
			IMapObject pointObj = entry.getValue();
			int type = pointObj.getType();
			if (type == MapObjectType.ITEM || type == MapObjectType.NPC) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否可以路过（包含穿透规则）
	 *
	 * @param me
	 * @param force
	 * @return
	 */
	public boolean canWalk(IMapObject me, boolean force) {
		if (force) {
			return true;
		}

		for (Entry<Long, IMapObject> entry : objectMap.entrySet()) {
			IMapObject target = entry.getValue();

			if (me.getType() == MapObjectType.Player && target.getType() == MapObjectType.Player) {
				if (this.isSafe()) {
					return true;
				}
			}

			if (!me.penetrate(target)) {
				return false;
			}
		}
		return true;
	}

	public void addObject(IMapObject obj) {
		this.objectMap.put(obj.getId(), obj);
		this.objectList.add(obj);

	}

	public void removeObject(IMapObject obj) {
		this.objectMap.remove(obj.getId());
		this.objectList.remove(obj);

	}


	@Override
	public String toString() {
		return "MapPoint[" + this.x + "," + this.y + "]";
	}

	public void clearRouting() {
		p = null;
		s = null;
		f = 0;
		g = 0;
		h = 0;
	}
}
