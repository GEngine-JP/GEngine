package info.xiaomo.core.network;


import info.xiaomo.gameCore.base.concurrent.command.IQueueDriverCommand;

public interface IProcessor {
	
	void process(IQueueDriverCommand handler);

}
