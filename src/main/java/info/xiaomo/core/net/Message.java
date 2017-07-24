package info.xiaomo.core.net;


import info.xiaomo.core.concurrent.IQueueDriverCommand;
import io.netty.buffer.ByteBuf;

/**
 * 网络请求的消息，该消息继承了队列执行命令接口，可以直接放入QueueDriver中执行
 * @author xiaomo
 *
 */
public interface Message extends IQueueDriverCommand {
	
	void decode(ByteBuf buf);
	
	byte[] encode();
	
	int length();
	
	void setLength(int length);
	
	int getId();
	
	int getQueueId();
	
	void setSequence(short sequence);
	
	short getSequence();
	
	
}
