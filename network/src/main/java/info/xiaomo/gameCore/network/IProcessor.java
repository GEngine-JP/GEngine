package info.xiaomo.gameCore.network;


import info.xiaomo.gameCore.base.concurrent.command.IQueueDriverCommand;

public interface IProcessor {
	
	void process(IQueueDriverCommand handler);

}
