package info.xiaomo.gengine.map.obj;

import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.gengine.utils.TraceUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象地图对象
 *
 * @author Administrator
 */

@Data
@Slf4j
public abstract class MapObject implements IMapObject {

	/**
	 * ID
	 */
	private long id;

	/**
	 * 是否可视
	 */
	private boolean visible = true;

	/**
	 * 配置ID
	 */
	private int configId;

	/**
	 * 地图ID
	 */
	private int mapId;

	/**
	 * 地图分线ID
	 */
	private int line;

	/**
	 * 所在坐标点
	 */
	protected MapPoint point;

	/**
	 * 名字
	 */
	protected String name;

	protected int dir;

	public void setMapId(int mapId) {
		if (getType() == MapObjectType.Player) {
			log.error("{}设置地图id：{},源地图id：{}", this.getName(), mapId, this.getMapId());
		}
		this.mapId = mapId;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public MapPoint getPoint() {
		return point;
	}

	public void setPoint(MapPoint point) {
		if (point != null) {
			if (this.point != null) {
				Dir dir = GeomUtil.getDir(this.point, point);
				if (dir != Dir.NONE) {
					setDir(dir.getIndex());
				}
			}
		}

		this.point = point;

		if (this.getType() == MapObjectType.Player && this.point == null) {
			log.error("设置{}坐标为空,调用栈:{}", this.getName(), TraceUtils.getStackTrace());
		}
	}


	@Override
	public int getType() {
		return 0;
	}

	@Override
	public boolean penetrate(IMapObject obj) {
		return false;
	}

	@Override
	public boolean overlying(IMapObject obj) {
		return false;
	}

	@Override
	public boolean isEnemy(IMapObject obj) {
		return false;
	}

	@Override
	public boolean isFriend(IMapObject obj) {
		return false;
	}


}
