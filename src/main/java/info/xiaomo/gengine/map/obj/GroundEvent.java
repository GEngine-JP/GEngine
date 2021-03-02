package info.xiaomo.gengine.map.obj;


/** 
 * 地面事件
 * @author zhangli
 * 2017年5月18日 下午8:35:02   
 */
public class GroundEvent extends NonPerformer{
	
	private boolean removed;

	@Override
	public int getType() {
		return MapObjectType.EVENT;
	}
	
	
	@Override
	public boolean penetrate(IMapObject obj) {
		return true;
	}



	@Override
	public boolean overlying(IMapObject obj) {
		return true;
	}



	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

}
