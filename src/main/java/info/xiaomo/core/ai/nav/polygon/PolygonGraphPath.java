package info.xiaomo.core.ai.nav.polygon;

import info.xiaomo.core.ai.pfa.Connection;
import info.xiaomo.core.ai.pfa.DefaultGraphPath;
import info.xiaomo.core.common.math.Vector3;

/**
 * 多边形路径点
 *
 * 
 * @date 2018年2月20日
 */
public class PolygonGraphPath extends DefaultGraphPath<Connection<Polygon>> {
	public Vector3 start;
	public Vector3 end;
	public Polygon startPolygon;

	public Polygon getEndPolygon() {
		return (getCount() > 0) ? get(getCount() - 1).getToNode() : startPolygon;
	}

}
