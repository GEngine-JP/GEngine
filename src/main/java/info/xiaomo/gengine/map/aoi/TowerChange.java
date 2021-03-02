package info.xiaomo.gengine.map.aoi;

import java.util.List;

public class TowerChange {

	private List<Tower> removeTowers;

	private List<Tower> addTowers;

	private List<Tower> unChangeTowers;

	public TowerChange(List<Tower> removeTowers, List<Tower> addTowers, List<Tower> unChangeTowers) {
		this.removeTowers = removeTowers;
		this.addTowers = addTowers;
		this.unChangeTowers = unChangeTowers;
	}

	public List<Tower> getRemoveTowers() {
		return removeTowers;
	}

	public void setRemoveTowers(List<Tower> removeTowers) {
		this.removeTowers = removeTowers;
	}

	public List<Tower> getAddTowers() {
		return addTowers;
	}

	public void setAddTowers(List<Tower> addTowers) {
		this.addTowers = addTowers;
	}

	public List<Tower> getUnChangeTowers() {
		return unChangeTowers;
	}

	public void setUnChangeTowers(List<Tower> unChangeTowers) {
		this.unChangeTowers = unChangeTowers;
	}

}
