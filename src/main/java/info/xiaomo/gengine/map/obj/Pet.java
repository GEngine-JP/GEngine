package info.xiaomo.gengine.map.obj;


import info.xiaomo.gengine.ai.fsm.FSMMachine;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宠物
 *
 * @author zhangli
 * 2017年6月6日 下午9:54:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Pet extends AbstractMonster {


	private int skillLevel;

	private int skillGroup;

	private int exp;

	private FSMMachine<Pet> machine;

	/**
	 * 宠物的主人
	 */
	private Performer master;

	public Performer getMaster() {
		return master;
	}

	public void setMaster(Performer master) {
		this.master = master;
	}

	public FSMMachine<Pet> getMachine() {
		return machine;
	}

	@Override
	public int getLastX() {
		return 0;
	}

	@Override
	public void setLastX(int lastX) {

	}

	@Override
	public int getLastY() {
		return 0;
	}

	@Override
	public void setLastY(int lastY) {

	}

	@Override
	public int getLastMapId() {
		return 0;
	}

	@Override
	public void setLastMapId(int lastMapId) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setMachine(FSMMachine<? extends AbstractMonster> machine) {
		this.machine = (FSMMachine<Pet>) machine;
	}

	@Override
	public int getType() {
		return MapObjectType.Pet;
	}

	@Override
	public boolean isEnemy(IMapObject obj) {
		if (this == obj) {
			return false;
		} else {
			return master.isEnemy(obj);
		}
	}

	@Override
	public boolean isFriend(IMapObject obj) {
		return obj != this.master;
	}

	@Override
	public boolean penetrate(IMapObject obj) {
		return true;
	}
}
