package info.xiaomo.gengine.map.util;


import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.constant.MapConst;
import info.xiaomo.gengine.map.constant.MapConst.Dir;

public class GeomUtil {

	private static int[][] POINT_ROUND_OFFSET = new int[20][];

	/**
	 * 自己周围八方向的偏移量
	 */
	public static final int[] EIGHT_DIR_OFFSET = new int[]{-1, -1, 0, -1, 1, -1, 1, 0, 1, 1, 0, 1, -1, 1, -1, 0};

	public static int getPointId(int x, int y) {
		return (x << 16) | y;
	}

	/**
	 * 获取某个区域对于中心区域坐标的偏移（正方形）
	 *
	 * @param range 最远距离中心点的格子数, 0：当前点，1：周围9个点，2：周围25个点
	 * @return
	 */
	public static int[] getPointRoundOffset(int range) {

		if (range >= POINT_ROUND_OFFSET.length) { //数组超长，自动进行扩展
			int[][] offset = new int[range][];
			System.arraycopy(POINT_ROUND_OFFSET, 0, offset, 0, POINT_ROUND_OFFSET.length);
			POINT_ROUND_OFFSET = offset;
		}

		int[] ret = POINT_ROUND_OFFSET[range];
		if (ret != null) {
			return ret;
		}

		// 0 = 2 * (0 * 2 + 1) * (0 * 2 + 1) = 2; 0 = 1
		// 1 = 2 * (1 * 2 + 1) * (1 * 2 + 1) = 18; 1 = 9
		// 2 = 2 * (2 * 2 + 1) * (2 * 2 + 1) = 50 2 = 25
		// 3 = 2 * (3 * 2 + 1) * (3 * 2 + 1) = 98 3 = 49
		ret = new int[2 * (range * 2 + 1) * (range * 2 + 1)];
		int i = 0;
		// 1 = 0  到  0
		// 2 = -1 到  1
		// 3 = -2 到   2
		for (int col = 0 - range; col <= range; col++) {
			for (int row = 0 - range; row <= range; row++) {
				ret[i++] = col;
				ret[i++] = row;
			}
		}

		POINT_ROUND_OFFSET[range] = ret;
		return ret;
	}

	public static MapConst.Dir getDir(MapPoint fromPoint, MapPoint toPoint) {
		return getDir(fromPoint.getX(), fromPoint.getY(), toPoint.getX(), toPoint.getY());
	}

	public static Dir getDir(int fx, int fy, int tx, int ty) {
		int colDiff = tx - fx;
		int rowDiff = ty - fy;
		if (colDiff > 0) {
			if (rowDiff > 0) {
				if (rowDiff == colDiff) {
					return Dir.RIGHT_BOTTOM;
				} else if (rowDiff > colDiff) {
					return Dir.BOTTOM;
				} else {
					return Dir.RIGHT;
				}
			} else if (rowDiff < 0) {
				if (rowDiff + colDiff == 0) {
					return Dir.RIGHT_TOP;
				} else if (rowDiff + colDiff > 0) {
					return Dir.RIGHT;
				} else {
					return Dir.TOP;
				}
			} else {
				return Dir.RIGHT;
			}
		} else if (colDiff == 0) {
			if (ty > fy) {
				return Dir.BOTTOM;
			} else if (ty == fy) {
				// 两个点在同一个位置上
				return Dir.NONE;
			} else {
				return Dir.TOP;
			}
		} else {
			if (rowDiff > 0) {
				if (rowDiff + colDiff == 0) {
					return Dir.LEFT_BOTTOM;
				} else if (rowDiff + colDiff > 0) {
					return Dir.BOTTOM;
				} else {
					return Dir.LEFT;
				}
			} else if (rowDiff < 0) {
				if (rowDiff == colDiff) {
					return Dir.LEFT_TOP;
				} else if (rowDiff > colDiff) {
					return Dir.LEFT;
				} else {
					return Dir.TOP;
				}
			} else {
				return Dir.LEFT;
			}
		}
	}

	public static final int distance(MapPoint a, MapPoint b) {
		if (a == null || b == null)
			return 0;
		return distance(a.getX(), a.getY(), b.getX(), b.getY());
	}

	public static final int distance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static MapPoint nextDirPoint(MapPoint point, Dir dir) {

		if (point == null) {
			return null;
		}
		if (dir == Dir.NONE) {
			return null;
		}
		MapPoint[] nears = point.getNears();
		MapPoint ret = nears[dir.getIndex()];
		if (ret == null) {
			return null;
		}

		if (ret.isBlock()) {
			return null;
		}

		return ret;
	}

	public static MapPoint nextDirPoint(MapPoint point, int dirIndex) {

		if (point == null) {
			return null;
		}
		if (dirIndex == Dir.NONE.getIndex()) {
			return null;
		}
		MapPoint[] nears = point.getNears();
		MapPoint ret = nears[dirIndex];
		if (ret == null) {
			return null;
		}

		if (ret.isBlock()) {
			return null;
		}

		return ret;
	}

	public static int[] countDirectionAddition(Dir dir) {
		int[] add = new int[2];
		switch (dir) {
			case TOP:
				add[1] = -1;
				break;
			case RIGHT_TOP:
				add[1] = -1;
				add[0] = 1;
				break;
			case RIGHT:
				add[0] = 1;
				break;
			case RIGHT_BOTTOM:
				add[1] = 1;
				add[0] = 1;
				break;
			case BOTTOM:
				add[1] = 1;
				break;
			case LEFT_BOTTOM:
				add[1] = 1;
				add[0] = -1;
				break;
			case LEFT:
				add[0] = -1;
				break;
			case LEFT_TOP:
				add[1] = -1;
				add[0] = -1;
				break;
			case NONE:
				add[1] = 0;
				add[0] = 0;
				break;
		}

		return add;
	}

}
