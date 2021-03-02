package info.xiaomo.gengine.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import info.xiaomo.gengine.concurrent.ScheduledEventDispatcher;
import info.xiaomo.gengine.concurrent.queue.QueueDriver;
import info.xiaomo.gengine.event.BasicEventType;
import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.gengine.map.aoi.AOIEventListener;
import info.xiaomo.gengine.map.aoi.TowerAOI;
import info.xiaomo.gengine.map.event.MapEvent;
import info.xiaomo.gengine.map.obj.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 场景地图，整个游戏世界中组顶层的容器，所有的对象的根节点都是该对象
 *
 * @author Administrator
 */
@Data
@Slf4j
public abstract class AbstractGameMap {

	/**
	 * 地图ID
	 */
	protected int id;

	/**
	 * 地图分线
	 */
	protected int line;

	/**
	 * 分线ID
	 */
	protected int cfgId;

	protected String name;

	protected boolean canCross = false;

	/**
	 * 玩家复活需要的时间
	 */
	protected int playerReliveTime = 5_000;


	/**
	 * 复活类型#血量
	 */
	private int[] reliveType;

	/**
	 * 存储游戏对象的map
	 */
	protected Map<Long, IMapObject> objectMap = new HashMap<>();

	protected Map<Long, Performer> playerMap = new HashMap<>();

	protected Map<Long, AbstractMonster> monsterMap = new HashMap<>();


	protected Map<Long, GroundEvent> eventMap = new HashMap<>();

	protected Map<Long, NPC> npcMap = new HashMap<>();

	protected Map<Long, Pet> petMap = new HashMap<>();

	protected Map<Integer, List<MapEvent>> pointEventMap = new HashMap<>();

	/**
	 * 地形
	 */
	protected Topography topography;

	protected TowerAOI aoi;

	/**
	 * 队列驱动(队列驱动里面包含一个队列
	 */
	protected QueueDriver driver;

	protected ScheduledEventDispatcher eventDispatcher;

	/**
	 * 路径寻找器
	 */
	protected PathFinder pathFinder;

	public IMapObject getObject(long id) {
		return objectMap.get(id);
	}

	public abstract void add(IMapObject obj);

	public abstract void remove(IMapObject obj);

	public void addView(IMapObject obj) {
		aoi.addObject(obj, obj.getPoint());
		if (obj.getType() == MapObjectType.Player) {
			aoi.addWatcher(obj, obj.getPoint());
		}
	}

	public void updateView(IMapObject obj, MapPoint oldPoint) {
		aoi.updateObject(obj, oldPoint, obj.getPoint());
		if (obj.getType() == MapObjectType.Player) {
			aoi.updateWatcher(obj, oldPoint, obj.getPoint());
		}
	}

	public void removeView(IMapObject obj) {
		if (obj.getType() == MapObjectType.Player) {
			aoi.removeWatcher(obj, obj.getPoint());
		}
		aoi.removeObject(obj, obj.getPoint());

	}

	public void enterMonster(AbstractMonster monster, MapPoint point, boolean addView) {
		monster.setMapId(this.id);
		monster.setLine(this.line);
		stand(monster, point);
		objectMap.put(monster.getId(), monster);
		monsterMap.put(monster.getId(), monster);
		if (addView) {
			aoi.addObject(monster, monster.getPoint());
		}
	}

	public abstract void enterPlayer(Performer player, MapPoint point);

	public void enterPet(Pet pet, MapPoint point) {
		pet.setMapId(this.id);
		pet.setLine(this.line);
		stand(pet, point);
		objectMap.put(pet.getId(), pet);
		petMap.put(pet.getId(), pet);
		aoi.addObject(pet, pet.getPoint());
	}

	public void enterEvent(GroundEvent event, MapPoint point) {
		event.setMapId(this.id);
		event.setLine(this.line);
		stand(event, point);
		objectMap.put(event.getId(), event);
		eventMap.put(event.getId(), event);
		aoi.addObject(event, event.getPoint());
	}


	public void enterNpc(NPC npc, MapPoint point) {
		npc.setMapId(this.id);
		npc.setLine(this.line);
		stand(npc, point);
		objectMap.put(npc.getId(), npc);
		npcMap.put(npc.getId(), npc);
		aoi.addObject(npc, npc.getPoint());
	}

	public void removeMonster(AbstractMonster monster, boolean removeView) {
		if (removeView) {
			aoi.removeObject(monster, monster.getPoint());
		}
		objectMap.remove(monster.getId());
		monsterMap.remove(monster.getId());
		stand(monster, null);
	}

	public abstract void removePlayer(Performer player);

	public void removePet(Pet pet) {
		aoi.removeObject(pet, pet.getPoint());
		objectMap.remove(pet.getId());
		petMap.remove(pet.getId());
		stand(pet, null);
	}

	public void removeEvent(GroundEvent event) {
		aoi.removeObject(event, event.getPoint());
		objectMap.remove(event.getId());
		eventMap.remove(event.getId());
		stand(event, null);
	}

	public void removeNpc(NPC npc) {
		objectMap.remove(npc.getId());
		npcMap.remove(npc.getId());
		aoi.removeObject(npc, npc.getPoint());
		stand(npc, null);
	}


	/**
	 * 站到地图的格子里
	 *
	 * @param obj
	 * @param point
	 */
	public void stand(IMapObject obj, MapPoint point) {
		MapPoint old = obj.getPoint();
		if (old != null) {
			old.removeObject(obj);
		}
		if (point != null) {
			point.addObject(obj);
		}
		obj.setPoint(point);

		if (point != null && obj instanceof Performer) {
			// 格子站立事件
			EventUtil.fireEvent(BasicEventType.STAND, obj);
		}

	}

	public void stand(IMapObject obj, int x, int y) {
		MapPoint point = getPoint(x, y);
		MapPoint old = obj.getPoint();
		if (old != null) {
			old.removeObject(obj);
		}
		if (point != null) {
			point.addObject(obj);
		}
		obj.setPoint(point);
	}

	public MapPoint getPoint(int x, int y) {
		return topography.getPoint(x, y);
	}

	public MapPoint getPoint(int id) {
		return topography.getPoint(id);
	}

	public AbstractGameMap(Topography topography, AOIEventListener listener) {
		this.topography = topography;
		this.aoi = new TowerAOI(topography.getWidth(), topography.getHeight());
		this.aoi.addListener(listener);
		this.pathFinder = new PathFinder(topography.getWidth(), topography.getHeight());
	}
}
