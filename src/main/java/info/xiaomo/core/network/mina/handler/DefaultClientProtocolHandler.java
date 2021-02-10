package info.xiaomo.core.network.mina.handler;

import info.xiaomo.core.common.utils.IntUtil;
import info.xiaomo.core.network.mina.service.MinaClientService;
import org.apache.mina.core.session.IoSession;

/**
 * 默认内部客户端消息处理器
 *
 * @author JiangZhiYong
 * @version $Id: $Id
 * @date 2017-04-09 QQ:359135103
 */
public class DefaultClientProtocolHandler extends DefaultProtocolHandler {

	private final MinaClientService service;

	/**
	 * <p>Constructor for DefaultClientProtocolHandler.</p>
	 */
	public DefaultClientProtocolHandler(MinaClientService service) {
		super(4);
		this.service = service;
	}


	/**
	 * <p>Constructor for DefaultClientProtocolHandler.</p>
	 *
	 * @param messageHeaderLength a int.
	 */
	public DefaultClientProtocolHandler(int messageHeaderLength, MinaClientService service) {
		super(messageHeaderLength);
		this.service = service;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionOpened(IoSession session) {
		super.sessionOpened(session);
		getService().onIoSessionConnect(session);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void forward(IoSession session, int msgID, byte[] bytes) {
		log.warn("无法找到消息处理器：msgID{},bytes{}", msgID, IntUtil.BytesToStr(bytes));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinaClientService getService() {
		return service;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) {
		super.sessionClosed(session);
		getService().onIoSessionClosed(session);
	}

}
