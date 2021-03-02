package info.xiaomo.gengine.map;

import java.util.*;
import info.xiaomo.gengine.map.obj.IMapObject;
import info.xiaomo.gengine.map.util.GeomUtil;

/** 
 * 路径寻找器
 * @author zhangli
 * 2017年6月6日 下午9:53:54   
 */
public class PathFinder {

	private OpenPointBinaryHeap openList;
	private Set<MapPoint> closeSet;
	private Set<MapPoint> openSet;

	public PathFinder(int width, int height) {
		openList = new OpenPointBinaryHeap(width * height);
		closeSet = new HashSet<>();
		openSet = new HashSet<>();
	}
	
	public MapPoint simpleRout(MapPoint start, MapPoint end, IMapObject obj, int step) {
		
		
		int dir = GeomUtil.getDir(start, end).getIndex();

		MapPoint next = start.getNears()[dir];
		if(next != null || next.canWalk(obj, false)){
			
		}
		
		return null;
		
	}
	
	public List<MapPoint> route(MapPoint start, MapPoint end, IMapObject obj) {
		return route(start, end, obj, 10000000);
	}
	

	public List<MapPoint> route(MapPoint start, MapPoint end, IMapObject obj, int limit) {
		
			openList.clear();
			closeSet.clear();
			openSet.clear();

			if (start.isBlock()) {
				return Collections.emptyList();
			}
			
			start.clearRouting();
			openList.push(start);

		MapPoint currentPoint = null;
			boolean limited = false;
			
			do {
				if (openList.isEmpty()) {
					// 还未找到终点，并且列表为空，表示没有能通过的路径
					return Collections.emptyList();
				}

				currentPoint = openList.pop();
				
				if(currentPoint.getG() > limit * 10) {
					//超过深度限制
					limited = true;
					break;
				}
				
				closeSet.add(currentPoint);
				MapPoint[] rounds = currentPoint.getNears();
				
				for (int index = 0; index < rounds.length; index++) {
					MapPoint pos = rounds[index];
					if (pos == null) {
						continue;
					}
					
					if (pos != end && !canReach(currentPoint, pos, obj)) {
						// 不可到达该点
						continue;
					}
					if (closeSet.contains(pos)) {
						// 已关闭
						continue;
					}

					if (!openSet.contains(pos)) {
						// 未开启
						pos.setP(currentPoint);

						pos.setG(getG(currentPoint, index));// 由于每个格子都有一个八叉树，所以，可以根据，八叉树索引来确定方向
						pos.setH(getH(pos, end) * 10);
						pos.setF(pos.getG() + pos.getH());
						openList.push(pos);
						openSet.add(pos);
						if (pos == end) {
							// 终点被放进关闭列表 A*搜索完毕
							break;
						}
					} else {
						// 节点 已经开启，检查该开启的节点，采用当前节点作为父节点的路径产生的F值是否会比之前更小，如果小，则更新父节点
						int oldF = pos.getF();
						int newG = getG(currentPoint, index);
						int newF = newG + pos.getH();
						if (newF < oldF) {
							// 更新父节点、更新G值（H值永远不变）
							pos.setP(currentPoint);
							pos.setG(newG);
							pos.setF(newF);
							openList.update(pos);// 更新二叉堆
						}
					}
				}
			} while (!closeSet.contains(end));
			LinkedList<MapPoint> ret = new LinkedList<>();
		MapPoint last = end;
			if(limited){
				last = currentPoint;
			}
			
			while (last.getP() != null && last != start) {
				ret.addFirst(last);
				last = last.getP();
			}
			return ret;
	}

	/**
	 * 判断from到to是否可以到达，from和to是相邻的两个点
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	private static boolean canReach(MapPoint from, MapPoint to, IMapObject obj) {

		// 检查场景中碰撞
		if (to.isBlock()) {
			return false;
		}

		// 判断穿透规则
        return obj == null || to.canWalk(obj, false);
    }

	/**
	 * 获得某个坐标的G值，从起始点走到该节点的代价，走一个直线节点10的代价，走一个对角线（直线）14的代价
	 * 
	 * @param parent
	 * @param index
	 * @return
	 */
	private static int getG(MapPoint parent, int index) {
		int parentG = parent.getG();
		// 方向是奇数表示斜线，偶数表示直线
		if (index % 2 == 0) {
			return parentG + 10;
		} else {
			return parentG + 14;
		}

	}

	/**
	 * 获得某个坐标的H值，采用曼哈顿距离，从该节点到终点的代价
	 * 
	 * @param pos
	 * @return
	 */
	private static int getH(MapPoint pos, MapPoint end) {
		return Math.abs(pos.getX() - end.getX()) + Math.abs(pos.getY() - end.getY());
	}
}
