package info.xiaomo.gengine.map.obj;


import java.util.ArrayList;
import java.util.List;
import info.xiaomo.gengine.ai.fsm.FSMMachine;
import info.xiaomo.gengine.map.MapPoint;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 怪物
 *
 * @author zhangli
 * 2017年6月6日 下午9:54:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractMonster extends Performer {


	// ================AI相关属性=======================================
	private FSMMachine<? extends AbstractMonster> machine;

	/**
	 * AI类型 </br>
	 * 1、怪物 怪物的AI类型   @see AiType
	 * 2、宠物 0 保护 1战斗  2跟随 3停止
	 */
	private int aiType = 1;

	/**
	 *
	 */
	private int[] targetChoose = {1, 0};

	/**
	 * AI如果是脚本，就是脚本的名称
	 */
	private String script;

	/**
	 * 距离攻击目标,警戒范围
	 */
	private int toAttackArea = 3;

	/**
	 * 移动间隔
	 */
	private int moveInterval = 500;

	/**
	 * 周围警戒的反应速度
	 */
	private int heart = 3;

	/**
	 * 睡眠时间
	 */
	private int reliveDelay = 30;

	/**
	 * 死亡尸体停留时间
	 */
	private int dieDelay = 3;

	/**
	 * 复活类型
	 * 1. 固定时间
	 * 2. 每天定点复活
	 */
	private int reliveType = 0;

	/**
	 * 出生点
	 */
	private MapPoint birthPoint;

	/**
	 * 最近的可攻击对象
	 */
	private long nearestObjectId;

	/**
	 * 移动速度
	 */
	private int moveSpeed;

	/**
	 * 当前路径
	 */
	private List<MapPoint> pathList = new ArrayList<>();

	/**
	 * 路径目标点
	 */
	private MapPoint pathTargetPoint;

	/**
	 * 移动的步子
	 */
	private int moveStep = 1;

	/**
	 * 最小可攻击范围
	 */
	private int attackArea = 5;

	/**
	 * 是否刚刚初始化
	 */
	private boolean init;

	/**
	 * 固定1点血
	 */
	private boolean fixHurt = false;


	// ================AI相关属性=======================================

	/**
	 * 怪物归属
	 */
	private long owner;

	/**
	 * 怪物类型
	 */
	private int monsterType;

	/**
	 * 是否死亡以后立即从场景中移除
	 */
	private boolean removeAfterDie;

	@Override
	public int getType() {
		return MapObjectType.Monster;
	}

	public FSMMachine<? extends AbstractMonster> getMachine() {
		return machine;
	}

	public void setMachine(FSMMachine<? extends AbstractMonster> machine) {
		this.machine = machine;
	}


	@Override
	public boolean penetrate(IMapObject obj) {
		return obj.getType() == MapObjectType.Player || obj.getType() == MapObjectType.HERO || obj.getType() == MapObjectType.Pet;
	}

	@Override
	public boolean overlying(IMapObject obj) {
		return obj.getType() == MapObjectType.Player || obj.getType() == MapObjectType.Pet || obj.getType() == MapObjectType.HERO;
	}

	@Override
	public boolean isEnemy(IMapObject obj) {
		if (obj == this) {
			return false;
		} else if (obj.getType() == MapObjectType.Player) {
			return true;
		} else return obj.getType() == MapObjectType.Pet;
	}

	@Override
	public boolean isFriend(IMapObject obj) {
		return false;
	}

}
