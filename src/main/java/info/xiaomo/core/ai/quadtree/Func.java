package info.xiaomo.core.ai.quadtree;

import info.xiaomo.core.ai.nav.polygon.Polygon;
import info.xiaomo.core.ai.quadtree.point.PointQuadTree;
import info.xiaomo.core.ai.quadtree.polygon.PolygonGuadTree;

/**
 * 功能函数
 *
 * @param <K>
 * @param <V>
 * 
 */
public interface Func<V> {
	public default void call(PointQuadTree<V> quadTree, Node<V> node) {

	}

	public default void call(PolygonGuadTree quadTree, Node<Polygon> node) {

	}
}
