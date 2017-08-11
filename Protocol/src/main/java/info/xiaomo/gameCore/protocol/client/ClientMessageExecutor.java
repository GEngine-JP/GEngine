package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.gameCore.protocol.MessageExecutor;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class ClientMessageExecutor extends MessageExecutor {

	protected Map<Short, ClientFuture<Message>> futureMap;

	public ClientMessageExecutor(NetworkConsumer consumer, NetworkEventListener listener,
								 Map<Short, ClientFuture<Message>> futureMap) {
		super(consumer, listener);
		this.futureMap = futureMap;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		Message m = (Message) msg;
		ClientFuture<Message> f = futureMap.get(m.getSequence());
		if(f != null) {
			if(!f.isCancelled()){
				f.result(m);
			}
		} else {
			super.channelRead(ctx, msg);
		}
		
	}
	
	
	
	
}
