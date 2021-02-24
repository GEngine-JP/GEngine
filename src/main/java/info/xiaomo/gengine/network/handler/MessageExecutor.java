package info.xiaomo.gengine.network.handler;

import com.google.protobuf.AbstractMessage;
import java.lang.reflect.Method;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.Message;
import info.xiaomo.gengine.network.pool.MessageAndHandlerPool;
import info.xiaomo.gengine.utils.ClassUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiaomo
 */
public class MessageExecutor extends ChannelInboundHandlerAdapter {

	private final INetworkConsumer consumer;

	protected final INetworkEventListener listener;


	public MessageExecutor(INetworkConsumer consumer, INetworkEventListener listener) {
		this.consumer = consumer;
		this.listener = listener;
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		listener.onExceptionOccur(ctx, cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Message) {
			Message packet = (Message) msg;
			System.out.println("\n<<<<<<<<<<<<收到服务端协议:" + packet.getMsgId() + "<<<<<<<<<<<<");

			AbstractMessage clazz = MessageAndHandlerPool.messages.get(packet.getMsgId());

			Method m = ClassUtil.findMethod(clazz.getClass(), "getDefaultInstance");
			if (m != null) {
				AbstractMessage message = (AbstractMessage) m.invoke(null);
				msg = message.newBuilderForType().mergeFrom(packet.getBytes()).build();
				consumer.consume((Message) msg, ctx.channel());
			}
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		if (this.listener != null) {
			this.listener.onConnected(ctx);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (this.listener != null) {
			this.listener.onDisconnected(ctx);
		}
	}
}
