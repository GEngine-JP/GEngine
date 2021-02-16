package info.xiaomo.gengine.ai.nav.triangle;

import info.xiaomo.gengine.ai.pfa.Connection;
import info.xiaomo.gengine.ai.pfa.DefaultGraphPath;
import info.xiaomo.gengine.common.math.Vector3;

/**
 * 路径点
 *
 * @author jsjolund
 */
public class TriangleGraphPath extends DefaultGraphPath<Connection<Triangle>> {
	/**
	 * The start point when generating a point path for this triangle path
	 */
	public Vector3 start;
	/**
	 * The end point when generating a point path for this triangle path
	 */
	public Vector3 end;
	/**
	 * If the triangle path is empty, the point path will span this triangle
	 */
	public Triangle startTri;

	/**
	 * @return Last triangle in the path.
	 */
	public Triangle getEndTriangle() {
		return (getCount() > 0) ? get(getCount() - 1).getToNode() : startTri;
	}
}
