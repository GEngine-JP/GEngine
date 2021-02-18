package info.xiaomo.gengine.network.netty.handler;

import com.google.protobuf.Message;
import java.util.concurrent.Executor;
import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.IHandler;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.network.mina.message.IDMessage;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.network.server.BaseServerConfig;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.utils.MsgUtil;
import info.xiaomo.gengine.utils.TimeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认接收消息处理器 <br>
 * 消息直接用netty线程池处理，分发请重新实现messageHandler
 * <p>
 * <p>
 * 2017年8月25日 上午11:29:37
 */
public abstract class DefaultInBoundHandler extends SimpleChannelInboundHandler<IDMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInBoundHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IDMessage msg) throws Exception {
		if (!ScriptManager.getInstance().tcpMsgIsRegister(msg.getMsgId())) {
			forward(msg);
			return;
		}
		Class<? extends IHandler> handlerClass = ScriptManager.getInstance().getTcpHandler(msg.getMsgId());
		TcpHandler handler = (TcpHandler) handlerClass.getDeclaredConstructor().newInstance();
		handler.setCreateTime(TimeUtil.currentTimeMillis());
		HandlerEntity handlerEntity = ScriptManager.getInstance().getTcpHandlerEntity(msg.getMsgId());
		Message message = MsgUtil.buildMessage(handlerEntity.msg(), (byte[]) msg.getMsg());
		handler.setMessage(message);
		handler.setRid(msg.getUserID());
		handler.setChannel(ctx.channel());
		messageHandler(handler, handlerEntity);
	}


	/**
	 * 消息处理
	 * <p>
	 * <p>
	 * 2017年8月25日 下午12:01:04
	 *
	 * @param handler
	 */
	protected void messageHandler(TcpHandler handler, HandlerEntity handlerEntity) {
		if (getService() != null) {
			Executor executor = getService().getExecutor(handlerEntity.thread());
			if (executor != null) {
				executor.execute(handler);
				return;
			}
		}
		handler.run();
	}

	/**
	 * 消息跳转
	 * <p>
	 * <p>
	 * 2017年8月25日 下午12:01:51
	 */
	protected void forward(IDMessage msg) {
		LOGGER.info("消息{} 未实现", msg.getMsgId());
	}

	public abstract GameService<? extends BaseServerConfig> getService();

}
