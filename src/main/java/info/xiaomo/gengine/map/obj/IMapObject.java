package info.xiaomo.gengine.map.obj;

import info.xiaomo.gengine.map.MapPoint;

/**
 * 地图中的对象
 *
 * @author zhangli
 * 2017年6月6日 下午9:54:10
 */
public interface IMapObject {

	long getId();

	void setId(long id);

	int getConfigId();

	void setConfigId(int configId);

	int getType();

	int getMapId();

	void setMapId(int mapId);

	int getLine();

	void setLine(int line);

	boolean isVisible();

	void setVisible(boolean visible);

	MapPoint getPoint();

	void setPoint(MapPoint point);

	String getName();

	void setName(String name);

	boolean penetrate(IMapObject obj);

	boolean overlying(IMapObject obj);

	boolean isEnemy(IMapObject obj);

	boolean isFriend(IMapObject obj);

}
