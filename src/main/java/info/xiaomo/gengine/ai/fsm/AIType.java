package info.xiaomo.gengine.ai.fsm;

public interface AIType {

	/**
	 * 不能行走和攻击的采集怪
	 * 0#采集读条需要的时间#每次采集到的个数下限#每次采集到的个数上限
	 */
	int NONE = 0;

	/**
	 * 主动出击(在攻击范围内会主动发起攻击)
	 */
	int Threat_ACTIVE = 1;

	/**
	 * 被动仇恨(被攻击了才会发起攻击)
	 */
	int THREAT_PASSIVE = 2;

	/**
	 * 有技能的怪
	 */
	int SKILL_MONSTER = 3;

	/**
	 * 脚本策略
	 */
	int SCRIPT = 4;


}
