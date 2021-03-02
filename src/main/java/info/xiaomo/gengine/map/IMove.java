package info.xiaomo.gengine.map;

import info.xiaomo.gengine.map.obj.Performer;

public interface IMove {
	void playerWalk(Performer player, int x, int y);

	void playerRun(Performer player, int x, int y);

	void performerMove(AbstractGameMap map, Performer performer, MapPoint point);

	void changeDir(Performer player, byte dir);

	void playerHorse(Performer player, int x, int y);

}