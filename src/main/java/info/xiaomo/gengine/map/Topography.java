package info.xiaomo.gengine.map;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.util.GeomUtil;
import info.xiaomo.gengine.utils.MapUtil;
import lombok.Data;

/**
 * 地形
 */
@Data
public class Topography {

	/**
	 * 格子的宽度
	 */
	public static final int GRID_WIDTH = 48;

	/**
	 * 格子的高度
	 */
	public static final int GRID_HEIGHT = 32;

	private final int pixelWidth;

	private final int pixelHeight;

	private int width;

	private int height;

	/**
	 * 出生点
	 */
	private final List<MapPoint> bornPointList = new ArrayList<>();


	/**
	 * 所有的点(Map)
	 */
	private final Map<Integer, MapPoint> pointMap = new HashMap<>();

	/**
	 * 可行走点
	 */
	private final List<MapPoint> walkList = new ArrayList<>();

	/**
	 * 矿点
	 */
	private Map<Integer, MapPoint> digAblePointMap = new HashMap<>();

	/**
	 * 所有的点(Array)
	 */
	private MapPoint[][] pointArray;

	public Topography(String binaryFile, int pixelWidth, int pixelHeight) {
		this.pixelHeight = pixelHeight;
		this.pixelWidth = pixelWidth;
		byte[] bytes = MapUtil.readMapBytes(binaryFile);
		if (bytes == null) {
			throw new RuntimeException("地图地形信息初始化失败,请检查地形文件[" + binaryFile + "]");
		}

		initGrid();

		readGridAttribute(bytes);

		createEightTree();

	}


	private void initGrid() {

		this.width = pixelWidth / GRID_WIDTH;
		this.height = pixelHeight / GRID_HEIGHT;
		this.width = pixelWidth % GRID_WIDTH == 0 ? this.width : this.width + 1;
		this.height = pixelHeight % GRID_HEIGHT == 0 ? this.height : this.height + 1;
		this.pointArray = new MapPoint[this.width][this.height];

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				MapPoint point = new MapPoint(x, y);
				this.pointArray[x][y] = point;
				pointMap.put(point.getId(), point);
			}
		}
	}

	/**
	 * 处理坐标的属性（障碍物，安全区等）
	 *
	 * @param bytes
	 */
	private void readGridAttribute(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, bytes.length);

		//4个bit代表一个格子[安全区、阻挡、矿点、遮挡(客户端)]
		byte b = buffer.get();
		int bitIndex = 8;
		int count = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int safe;
				int block;
				int digAble;
				if (bitIndex == 8) {
					block = b >> 7 & 0b00000001;
					safe = b >> 5 & 0b00000001;
					digAble = b >> 4 & 0b00000001;
					bitIndex = 4;
				} else {
					block = b >> 3 & 0b00000001;
					safe = b >> 1 & 0b00000001;
					digAble = b & 0b00000001;
					//读取下一个字节
					bitIndex = 8;

					if (!(x == width - 1 && y == height - 1)) {
						b = buffer.get();
					}

				}

				MapPoint grid = this.pointArray[x][y];
				grid.setBlock(block == 1);
				grid.setSafe(safe == 1);
				grid.setDigAble(digAble == 1);
				count++;
			}

		}

		//读取出生点
		int bornLength = buffer.getShort();
		for (int i = 0; i < bornLength; i++) {
			int x = buffer.getShort();
			int y = buffer.getShort();

			MapPoint grid = this.pointArray[x][y];
			bornPointList.add(grid);
		}


		//所有可行走点存下来
		for (MapPoint[] outer : pointArray) {
			for (MapPoint p : outer) {
				if (!p.isBlock()) {
					walkList.add(p);
				}
			}
		}


	}

	private void createEightTree() {
		// 构建八叉树
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				MapPoint p = this.pointArray[x][y];
				// 遍历该格子周围的格子，进行周围是否能走的判断
				MapPoint[] nears = new MapPoint[8];
				for (int index = 0; index < 16; index += 2) {
					int tx = x + GeomUtil.EIGHT_DIR_OFFSET[index];
					int ty = y + GeomUtil.EIGHT_DIR_OFFSET[index + 1];
					if (tx < 0 || ty < 0 || tx >= width || ty >= height)
						continue;
					MapPoint tp = this.pointArray[tx][ty];
					Dir dir = GeomUtil.getDir(x, y, tx, ty);
					nears[dir.getIndex()] = tp;
				}
				p.setNears(nears);
			}
		}
	}

	public MapPoint getPoint(int x, int y) {
		if (x < 0 || y < 0 || x >= pointArray.length || y >= pointArray[0].length) {
			return null;
		}
		return pointArray[x][y];
	}

	public MapPoint getPoint(int id) {
		return pointMap.get(id);
	}


}