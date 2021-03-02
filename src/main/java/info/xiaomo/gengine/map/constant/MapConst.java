package info.xiaomo.gengine.map.constant;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright(©) 2017 by xiaomo.
 */
public interface MapConst {

	/**
	 * 速度
	 */
	interface Speed {
		int WALK = 395;
		int RUN = 455;
		int HORSE = 460;
	}

	/**
	 * 地图id
	 */
	interface MapId {

		/**
		 * 死亡竞技场
		 */
		List<Integer> DEAD_FIGHT = Arrays.asList(4001, 4002, 4003, 4004, 4005, 4006);
	}


	/**
	 * 线
	 */
	interface Line {

		int DEFAULT = 1;

	}

	/**
	 * 方向
	 */
	enum Dir implements MapConst {

		NONE(0, -1) {// 同一个点

			@Override
			public Dir left() {
				return NONE;
			}

			@Override
			public Dir right() {
				return NONE;
			}

			@Override
			public Dir behind() {
				return NONE;
			}

		},
		TOP(0x10000000, 0) {// 上

			@Override
			public Dir left() {
				return LEFT_TOP;
			}

			@Override
			public Dir right() {
				return RIGHT_TOP;
			}

			@Override
			public Dir behind() {
				return BOTTOM;
			}
		},
		RIGHT_TOP(0x01000000, 1) {// 右上

			@Override
			public Dir left() {
				return TOP;
			}

			@Override
			public Dir right() {
				return RIGHT;
			}

			@Override
			public Dir behind() {
				return LEFT_BOTTOM;
			}
		},
		RIGHT(0x00100000, 2) {// 右

			@Override
			public Dir left() {
				return RIGHT_TOP;
			}

			@Override
			public Dir right() {
				return RIGHT_BOTTOM;
			}

			@Override
			public Dir behind() {
				return LEFT;
			}
		},
		RIGHT_BOTTOM(0x00010000, 3) {// 右下

			@Override
			public Dir left() {
				return RIGHT;
			}

			@Override
			public Dir right() {
				return BOTTOM;
			}

			@Override
			public Dir behind() {
				return LEFT_TOP;
			}
		},
		BOTTOM(0x00001000, 4) {// 下

			@Override
			public Dir left() {
				return RIGHT_BOTTOM;
			}

			@Override
			public Dir right() {
				return LEFT_BOTTOM;
			}

			@Override
			public Dir behind() {
				return TOP;
			}
		},
		LEFT_BOTTOM(0x00000100, 5) {// 左下

			@Override
			public Dir left() {
				return BOTTOM;
			}

			@Override
			public Dir right() {
				return LEFT;
			}

			@Override
			public Dir behind() {
				return RIGHT_TOP;
			}
		},
		LEFT(0x00000010, 6) {// 左

			@Override
			public Dir left() {
				return LEFT_BOTTOM;
			}

			@Override
			public Dir right() {
				return LEFT_TOP;
			}

			@Override
			public Dir behind() {
				return RIGHT;
			}
		},
		LEFT_TOP(0x00000001, 7) {// 左上

			@Override
			public Dir left() {
				return LEFT;
			}

			@Override
			public Dir right() {
				return TOP;
			}

			@Override
			public Dir behind() {
				return RIGHT_BOTTOM;
			}
		},
		;
		/**
		 * 方向的值
		 */
		private int value;
		/**
		 * 八叉树数组中的下表索引
		 */
		private int index;

		/**
		 * 左侧方向(逆时针)
		 *
		 * @return
		 */
		public abstract Dir left();

		/**
		 * 右侧方向（顺时针）
		 *
		 * @return
		 */
		public abstract Dir right();

		/**
		 * 相反方向
		 *
		 * @return
		 */
		public abstract Dir behind();

		public static Dir getByIndex(int index) {
			for (Dir dir : values()) {
				if (dir.index == index) {
					return dir;
				}
			}
			return null;
		}

		Dir(int value, int index) {
			this.value = value;
			this.index = index;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
