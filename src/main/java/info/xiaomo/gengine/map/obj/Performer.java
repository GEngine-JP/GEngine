package info.xiaomo.gengine.map.obj;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.gengine.ai.fsm.FSMMachine;
import info.xiaomo.gengine.map.MapPoint;
import info.xiaomo.gengine.map.constant.MapConst.Dir;
import info.xiaomo.gengine.map.constant.MapConst.Speed;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 有行为的对象
 *
 * @author 张力
 */

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Performer extends MapObject {

	private int level;

	private int sex;

	private int career;

	/**
	 * HP
	 */
	protected int hp = 0;

	/**
	 * MP
	 */
	protected int mp = 0;

	protected boolean dead = false;

	/**
	 * 内功
	 */
	protected int innerPower = 0;

	/**
	 * 仇恨列表
	 */
	private Map<Long, Integer> threatMap = new ConcurrentHashMap<>();

	/**
	 * 战斗对象
	 */
	private long fightTarget;

	/**
	 * 攻击我的对象
	 */
	private long whoAttackMe;
	private long whoAttackMeTime;

	/**
	 * 我的攻击目标
	 */
	private long whoMyTarget;

	private long whoMyTargetTime;


	/**
	 * 杀手ID
	 */
	private long killerId;

	/**
	 * 死亡时间
	 */
	private long deadTime;

	/**
	 * 宠物
	 */
	private List<Pet> petArray = new ArrayList<Pet>();

	// ======移动====================

	private MapPoint lastPoint;

	private int lastMoveSpeed;

	private long lastMoveTime;

	private boolean moved = false;


	public abstract FSMMachine<? extends Performer> getMachine();

	public MapPoint getMovingPoint() {

		if (lastPoint == null || this.dir == Dir.NONE.getIndex()) {
			return point;
		}
		long diff = System.currentTimeMillis() - this.lastMoveTime;
		if (this.lastMoveSpeed == Speed.WALK) {
			if (diff >= Speed.WALK / 2) { // 用时一半
				return point;
			} else {
				return lastPoint;
			}
		} else if (this.lastMoveSpeed == Speed.RUN) {
			MapPoint firstPoint = lastPoint.getNears()[this.dir]; // 一次走两个格子
			if (diff > Speed.RUN * 3 / 4) { // 走了3/4以上的时间，站在第二个点上
				return point;
			} else if (diff > Speed.RUN / 4) { // 时间过了1/4 站在第一个点上
				return firstPoint;
			} else {
				// 否则算当前还没走出第一个格子
				return lastPoint;
			}
		} else if (this.lastMoveSpeed == Speed.HORSE) {
			MapPoint first = lastPoint.getNears()[this.dir];
			MapPoint second = null;
			if (first != null)
				second = first.getNears()[this.dir];

			if (diff > Speed.HORSE * 5 / 6) {
				return point;
			} else if (diff > Speed.HORSE / 2) {
				return second;
			} else if (diff > Speed.HORSE / 6) {
				return first;
			} else
				return lastPoint;
		} else {
			return point;
		}
	}

	public void addThreat(Performer performer, int value) {

		this.threatMap.merge(performer.getId(), value, Integer::sum);
	}


	public void setWhoAttackMe(long whoAttackMe) {
		this.whoAttackMe = whoAttackMe;
		this.whoAttackMeTime = 4000;
	}

	public void setWhoMyTarget(long whoMyTarget) {
		this.whoMyTarget = whoMyTarget;
		this.whoMyTargetTime = 4000;
	}

	public abstract int getLastX();

	public abstract void setLastX(int lastX);

	public abstract int getLastY();

	public abstract void setLastY(int lastY);

	public abstract int getLastMapId();

	public abstract void setLastMapId(int lastMapId);

}
