package info.xiaomo.core.network;


import info.xiaomo.core.base.concurrent.command.IQueueDriverCommand;

public interface IProcessor {
	
	void process(IQueueDriverCommand handler);

}
