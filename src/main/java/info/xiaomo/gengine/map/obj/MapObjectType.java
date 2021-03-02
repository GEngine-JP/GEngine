package info.xiaomo.gengine.map.obj;

/**
 * 地图对象类型
 * @author zhangli
 * 2017年6月6日 下午9:54:33
 */
public interface MapObjectType {

	/**
	 * 玩家
	 */
	int Player = 1;

	/**
	 * 怪物
	 */
	int Monster = 2;

	/**
	 * 宠物
	 */
	int Pet = 3;

	/**
	 * 英雄
	 */
	int HERO = 4;

	/**
	 * npc
	 */
	int NPC = 5;

	/**
	 * 道具
	 */
	int ITEM = 7;

	/**
	 * buffer
	 */
	int BUFFER = 8;

	/**
	 * 事件
	 */
	int EVENT = 9;

}
