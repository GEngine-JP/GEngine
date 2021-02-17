package info.xiaomo.gengine.network.mina.handler;

import com.google.protobuf.Message;
import java.util.concurrent.Executor;
import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.IHandler;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.utils.MsgUtil;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.network.server.BaseServerConfig;
import info.xiaomo.gengine.network.server.GameService;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认消息处理器
 * <p>
 * 消息头+消息内容 <br>
 * 消息头可能有消息长度、消息ID、用户ID
 * </p>
 *
 *
 * @version $Id: $Id
 *  2017-03-30
 */
public abstract class DefaultProtocolHandler implements IoHandler {

	/**
	 * Constant <code>log</code>
	 */
	protected static final Logger log = LoggerFactory.getLogger(DefaultProtocolHandler.class);
	protected final int messageHeaderLength; // 消息头长度

	/**
	 * <p>Constructor for DefaultProtocolHandler.</p>
	 *
	 * @param messageHeaderLength 消息头长度
	 */
	public DefaultProtocolHandler(int messageHeaderLength) {
		this.messageHeaderLength = messageHeaderLength;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// log.warn("已创建连接{}", session);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionOpened(IoSession session) {
		log.warn("已打开连接{}", session);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageSent(IoSession ioSession, Object message) throws Exception {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) {
		log.warn("连接{}已关闭sessionClosed", session);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus idleStatus) {
		log.warn("连接{}处于空闲{}", session, idleStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) {
		log.error("连接{}异常：{}", session, throwable);
		session.closeNow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inputClosed(IoSession session) throws Exception {
		log.warn("连接{}inputClosed已关闭", session);
		session.closeNow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		byte[] bytes = (byte[]) obj;
		try {
			if (bytes.length < messageHeaderLength) {
				log.error("messageReceived:消息长度{}小于等于消息头长度{}", bytes.length, messageHeaderLength);
				return;
			}
			int offset = messageHeaderLength > 4 ? 8 : 0;
			int msgID = MsgUtil.getMessageID(bytes, offset); // 消息ID

			if (ScriptManager.getInstance().tcpMsgIsRegister(msgID)) {
				Class<? extends IHandler> handlerClass = ScriptManager.getInstance().getTcpHandler(msgID);
				HandlerEntity handlerEntity = ScriptManager.getInstance().getTcpHandlerEntity(msgID);
				if (handlerClass != null) {
					Message message = MsgUtil.buildMessage(handlerEntity.msg(), bytes, messageHeaderLength,
							bytes.length - messageHeaderLength);
					TcpHandler handler = (TcpHandler) handlerClass.newInstance();
					if (handler != null) {
						if (offset > 0) { // 偏移量大于0，又发玩家ID
							long rid = MsgUtil.getMessageRID(bytes, 0);
							handler.setRid(rid);
						}
						messageHandler(session, handlerEntity, message, handler);
						return;
					}
				}
			}
			forward(session, msgID, bytes);
		} catch (Exception e) {
			log.error("messageReceived", e);
			int msgid = MsgUtil.getMessageID(bytes, 0);
			log.warn("尝试按0移位处理,id：{}", msgid);
		}
	}

	/**
	 * 消息处理
	 *
	 * @param session a {@link IoSession} object.
	 * @param message a {@link Message} object.
	 */
	protected void messageHandler(IoSession session, HandlerEntity handlerEntity, Message message, TcpHandler handler) {
		handler.setMessage(message);
		handler.setSession(session);
		handler.setCreateTime(System.currentTimeMillis());
		Executor executor = getService().getExecutor(handlerEntity.thread());
		if (executor == null) {
			// log.warn("处理器{}没有分配线程", handler.getClass().getName());
			handler.run();
			return;
		}
		executor.execute(handler);
	}

	/**
	 * 转发消息
	 *
	 * @param session a {@link IoSession} object.
	 * @param msgID   a int.
	 * @param bytes   an array of {@link byte} objects.
	 */
	protected abstract void forward(IoSession session, int msgID, byte[] bytes);

	/**
	 * <p>getService.</p>
	 */
	public abstract GameService<? extends BaseServerConfig> getService();
}
