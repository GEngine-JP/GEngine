package info.xiaomo.core.network.mina.handler;

import com.google.protobuf.Message;
import info.xiaomo.core.common.handler.HandlerEntity;
import info.xiaomo.core.common.handler.IHandler;
import info.xiaomo.core.common.handler.TcpHandler;
import info.xiaomo.core.common.utils.IntUtil;
import info.xiaomo.core.common.utils.MsgUtil;
import info.xiaomo.core.network.mina.config.MinaServerConfig;
import info.xiaomo.core.script.ScriptManager;
import info.xiaomo.core.server.BaseServerConfig;
import info.xiaomo.core.server.GameService;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏前端消息处理器
 * <p>
 * 包长度（2）+消息ID（4）+消息长度（4）+消息内容
 *
 * <br>
 * decoder 已去掉包长度
 * </p>
 *
 * 
 * @version $Id: $Id
 * @date 2017-04-01
 */
public class ClientProtocolHandler extends DefaultProtocolHandler {

	private static final Logger log = LoggerFactory.getLogger(ClientProtocolHandler.class);
	protected GameService<MinaServerConfig> gameService;

	/**
	 * <p>Constructor for ClientProtocolHandler.</p>
	 *
	 * @param messageHeaderLength a int.
	 */
	public ClientProtocolHandler(int messageHeaderLength) {
		super(messageHeaderLength);
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
			int mid = IntUtil.bigEndianByteToInt(bytes, 0, 4); // 消息ID
			// int protoLength=IntUtil.bigEndianByteToInt(bytes, 6, 4); //TODO
			// 消息长度,不需要？

			if (ScriptManager.getInstance().tcpMsgIsRegister(mid)) {
				Class<? extends IHandler> handlerClass = ScriptManager.getInstance().getTcpHandler(mid);
				HandlerEntity handlerEntity = ScriptManager.getInstance().getTcpHandlerEntity(mid);
				if (handlerClass != null) {
//					log.info("{} {} bytes:{}",messageHeaderLenght, bytes.length, bytes );
					Message message = MsgUtil.buildMessage(handlerEntity.msg(), bytes, messageHeaderLength,
							bytes.length - messageHeaderLength);
					TcpHandler handler = (TcpHandler) handlerClass.newInstance();
					messageHandler(session, handlerEntity, message, handler);
					return;
				}
			}
			forward(session, mid, bytes);

		} catch (Exception e) {
			log.error("messageReceived", e);
		}
	}

	@Override
	public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void forward(IoSession session, int msgID, byte[] bytes) {
		log.warn("消息[{}]未实现", msgID);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameService<? extends BaseServerConfig> getService() {
		return gameService;
	}

	/**
	 * <p>Setter for the field <code>service</code>.</p>
	 *
	 * @param gameService a {@link com.jzy.game.engine.server.Service} object.
	 */
	public void setService(GameService<MinaServerConfig> gameService) {
		this.gameService = gameService;
	}

}
