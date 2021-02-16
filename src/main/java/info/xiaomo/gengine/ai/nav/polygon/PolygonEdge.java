package info.xiaomo.gengine.ai.nav.polygon;

import com.alibaba.fastjson.JSON;
import info.xiaomo.gengine.ai.pfa.Connection;
import info.xiaomo.gengine.common.math.Vector3;

/**
 * 多边形共享边
 *
 *
 */
public class PolygonEdge implements Connection<Polygon> {

	/**
	 * 右顶点
	 */
	public Vector3 rightVertex;
	public Vector3 leftVertex;

	/**
	 * 源三多边形
	 */
	public Polygon fromNode;
	/**
	 * 指向的多边形
	 */
	public Polygon toNode;
	private float cost;

	public PolygonEdge(Polygon fromNode, Polygon toNode, Vector3 rightVertex, Vector3 leftVertex) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.rightVertex = rightVertex;
		this.leftVertex = leftVertex;
	}

	@Override
	public float getCost() {
		if (cost == 0) {
			cost = fromNode.center.dst(toNode.center);
		}
		return cost;
	}

	@Override
	public Polygon getFromNode() {
		return fromNode;
	}

	@Override
	public Polygon getToNode() {
		return toNode;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


}
