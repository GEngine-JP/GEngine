package info.xiaomo.gengine.ai.fsm;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Data;

@Data
public class AIData {

	/**
	 * 已经睡眠的时间，整个AI系统都要用，主要是用于战斗和移动
	 */
	private int sleepTime = 0;


	/**
	 * 上次寻找周围怪物的时间
	 */
	private int findNearestFightAbleTime = 0;


	/**
	 * 随机走动的参数 (1w-2w之间)
	 */
	private int randomMoveTime = (int) (ThreadLocalRandom.current().nextDouble() * 10000 + 10000);

	/**
	 * 仇恨清除时间
	 */
	private int clearThreatTime = 13500;

	/**
	 * 蓝血恢复时间
	 */
	private int recoverHpAndMapTime = 0;

	/**
	 * 死亡时间
	 */
	private long dieTime = 0;

	/**
	 * 下一次复活时间
	 */
	private long nextReliveTime = 0;


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


	public void updateFightAITime(int dt) {
		this.findNearestFightAbleTime -= dt;
		this.recoverHpAndMapTime -= dt;
		this.clearThreatTime -= dt;
		this.sleepTime -= dt;
	}

	public void updateActiveAITime(int dt) {
		this.findNearestFightAbleTime -= dt;
		this.randomMoveTime -= dt;
		this.recoverHpAndMapTime -= dt;
		this.sleepTime -= dt;

	}

	public void updatePetFollowAITime(int dt) {
		this.sleepTime -= dt;
		this.recoverHpAndMapTime -= dt;
	}

	public void updatePetFightAITime(int dt) {
		this.sleepTime -= dt;
		this.recoverHpAndMapTime -= dt;
	}


	public void updateMyTargetOrAttackedMe(int dt) {

		if (this.whoAttackMe > 0) {
			this.whoAttackMeTime -= dt;
			if (this.whoAttackMeTime <= 0) {
				this.whoAttackMe = 0;
			}
		}

		if (this.whoMyTarget > 0) {
			this.whoMyTargetTime -= dt;
			if (this.whoMyTargetTime <= 0) {
				this.whoMyTarget = 0;
			}
		}
	}

	public void clearMyTargetOrAttackedMe() {

		this.whoAttackMeTime = 0;
		this.whoAttackMe = 0;

		this.whoMyTargetTime = 0;
		this.whoMyTarget = 0;
	}


}
