package info.xiaomo.gengine.ai.nav.polygon;

import info.xiaomo.gengine.ai.pfa.Connection;
import info.xiaomo.gengine.ai.pfa.DefaultGraphPath;
import info.xiaomo.gengine.math.Vector3;

/**
 * 多边形路径点
 * <p>
 * <p>
 * 2018年2月20日
 */
public class PolygonGraphPath extends DefaultGraphPath<Connection<Polygon>> {
	public Vector3 start;
	public Vector3 end;
	public Polygon startPolygon;

	public Polygon getEndPolygon() {
		return (getCount() > 0) ? get(getCount() - 1).getToNode() : startPolygon;
	}

}
