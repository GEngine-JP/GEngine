package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.base.AbstractHandler;

public interface HandlerPool {
	
	AbstractHandler getHandler(int messageId);
	
}
