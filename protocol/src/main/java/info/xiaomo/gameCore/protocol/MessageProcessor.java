package info.xiaomo.gameCore.protocol;


import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;

public interface MessageProcessor {
	
	void process(IQueueDriverCommand handler);

}
