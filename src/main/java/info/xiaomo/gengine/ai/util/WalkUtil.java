package info.xiaomo.gengine.ai.util;

import java.util.List;
import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.IMove;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.obj.AbstractMonster;
import info.xiaomo.gengine.map.util.GeomUtil;

public class WalkUtil {

	/**
	 * 只移动
	 */
	public final static int MOVE = 1;

	/**
	 * 移动并攻击
	 */
	public final static int MOVE_TO_ATTACK = 2;

	/**
	 * 去下个点
	 *
	 * @param map
	 * @param monster
	 * @param targetPoint
	 * @param step
	 * @param action
	 * @return
	 */
	public static MapPoint getPathNext(AbstractGameMap map, AbstractMonster monster, MapPoint targetPoint, int step, int action) {
		boolean needFind = false;
		if (targetPoint != null) {
			List<MapPoint> pathList = monster.getPathList();
			if (pathList == null) {// 当前没有路径，重新寻路
				needFind = true;
			} else if (pathList.isEmpty()) {
				needFind = true; // 当前没有路径， 重新寻路
			} else {
				if (monster.getPathTargetPoint() != targetPoint) {
					needFind = true;
				} else {
					MapPoint nextp = nextPoint(pathList, step, monster, action);
					if (nextp == null || !nextp.canWalk(monster, false)) {// 下个路劲不可走，重新寻路
						needFind = true;
					} else {
						return nextp;
					}
				}
			}
		}

		if (needFind) {

			List<MapPoint> pathList = map.getPathFinder().route(monster.getPoint(), targetPoint, monster, 10);
			monster.setPathList(pathList);
			monster.setPathTargetPoint(targetPoint);
			if (pathList != null && !pathList.isEmpty()) {
				return nextPoint(pathList, step, monster, action);
			} else {
				return null;
			}
		}

		return null;
	}


	/**
	 * 下个点
	 *
	 * @param pathList
	 * @param step
	 * @param monster
	 * @param action
	 * @return
	 */
	private static MapPoint nextPoint(List<MapPoint> pathList, int step, AbstractMonster monster, int action) {
		MapPoint nextp = null;
		int xdis = 0;
		int ydis = 0;
		for (int i = 0; i < step && !pathList.isEmpty(); i++) {
			if (action == MOVE_TO_ATTACK && pathList.size() == 1) {
				// 追击玩家的话，那么只能追击到倒数第二格子，这样就不用和玩家重合
				break;
			}
			MapPoint currentp = pathList.remove(0);
			if (nextp != null) {
				int curxdis = currentp.x - nextp.x;
				int curydis = currentp.y - nextp.y;
				if (xdis != 0 && ydis != 0) {
					if (curxdis != xdis || curydis != ydis) {
						break;
					}
				}
				xdis = curxdis;
				ydis = curydis;
			}
			if (!currentp.canStand(monster)) {
				break;
			}
			nextp = currentp;
		}
		return nextp;
	}

	/**
	 * 沿着某个方向一定(AI调用,不允许玩家调用)
	 *
	 * @param map
	 * @param monster
	 * @param tPoint
	 */
	public static void moveByDir(AbstractGameMap map, AbstractMonster monster, MapPoint tPoint, IMove move) {

		MapPoint mPoint = monster.getPoint();
		if (mPoint == null) {
			return;
		}
		MapPoint nextPoint;
		Dir dir = GeomUtil.getDir(mPoint, tPoint); // 回家的方向

		if (dir == Dir.NONE) {
			return;
		}

		nextPoint = GeomUtil.nextDirPoint(mPoint, dir);
		if (nextPoint != null) {
			if (!nextPoint.canStand(monster)) {
				nextPoint = null;
			} else {
				if (monster.getMoveStep() == 2) {
					MapPoint tempPoint = GeomUtil.nextDirPoint(nextPoint, dir);
					// 有些怪物可以走两步
					if (tempPoint.canStand(monster)) {
						nextPoint = tempPoint;
					}
				}
			}
		}

		if (nextPoint == null) { // 朝着回家的方向两侧走（左侧）
			Dir left = dir.left();
			nextPoint = GeomUtil.nextDirPoint(mPoint, left);
			if (nextPoint != null) {
				if (!nextPoint.canStand(monster)) {
					nextPoint = null;
				}
			}
		}
		if (nextPoint == null) { // 朝着回家的方向两侧走（右侧）
			Dir right = dir.right();
			nextPoint = GeomUtil.nextDirPoint(mPoint, right);
			if (nextPoint != null) {
				if (!nextPoint.canStand(monster)) {
					nextPoint = null;
				}
			}
		}

		if (nextPoint != null) {
			move.performerMove(map, monster, nextPoint);
		}

	}

}
