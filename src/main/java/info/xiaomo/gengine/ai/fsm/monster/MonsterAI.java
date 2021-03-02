package info.xiaomo.gengine.ai.fsm.monster;


import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.obj.AbstractMonster;

/**
 * 怪物AI接口， 用于做不同的怪物AI，所有这些方法都在怪物状态机中调用
 *
 * @author zhangli
 * 2017年6月6日 下午9:55:45
 */
public interface MonsterAI {

	boolean activeEnter(AbstractGameMap map, AbstractMonster monster);

	boolean activeUpdate(AbstractGameMap map, AbstractMonster monster, int dt);

	boolean activeExit(AbstractGameMap map, AbstractMonster monster);


	boolean battleEnter(AbstractGameMap map, AbstractMonster monster);

	boolean battleUpdate(AbstractGameMap map, AbstractMonster monster, int dt);

	boolean battleExit(AbstractGameMap map, AbstractMonster monster);

	boolean dieEnter(AbstractGameMap map, AbstractMonster monster);

	boolean dieUpdate(AbstractGameMap map, AbstractMonster monster, int dt);

	boolean dieExit(AbstractGameMap map, AbstractMonster monster);

	boolean sleepEnter(AbstractGameMap map, AbstractMonster monster);

	boolean sleepUpdate(AbstractGameMap map, AbstractMonster monster, int dt);

	boolean sleepExit(AbstractGameMap map, AbstractMonster monster);


}
