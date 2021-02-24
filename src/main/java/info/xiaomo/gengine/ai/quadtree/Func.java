package info.xiaomo.gengine.ai.quadtree;

import info.xiaomo.gengine.ai.nav.polygon.Polygon;
import info.xiaomo.gengine.ai.quadtree.point.PointQuadTree;
import info.xiaomo.gengine.ai.quadtree.polygon.PolygonGuadTree;

/**
 * 功能函数
 *
 * @param <K>
 * @param <V>
 */
public interface Func<V> {
    public default void call(PointQuadTree<V> quadTree, Node<V> node) {}

    public default void call(PolygonGuadTree quadTree, Node<Polygon> node) {}
}
