package info.xiaomo.gengine.map.obj;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NPC extends NonPerformer {

	private int mapNpcId;

	@Override
	public int getType() {
		return MapObjectType.NPC;
	}


	@Override
	public boolean penetrate(IMapObject obj) {
		return false;
	}
}
