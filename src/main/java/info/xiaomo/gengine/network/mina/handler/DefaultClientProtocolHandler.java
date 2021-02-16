package info.xiaomo.gengine.network.mina.handler;

import info.xiaomo.gengine.common.utils.IntUtil;
import info.xiaomo.gengine.network.mina.service.MinaClientGameService;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

/**
 * 默认内部客户端消息处理器
 *
 *
 * @version $Id: $Id
 * @date 2017-04-09
 */
public class DefaultClientProtocolHandler extends DefaultProtocolHandler {

	private final MinaClientGameService service;

	/**
	 * <p>Constructor for DefaultClientProtocolHandler.</p>
	 */
	public DefaultClientProtocolHandler(MinaClientGameService service) {
		super(4);
		this.service = service;
	}


	/**
	 * <p>Constructor for DefaultClientProtocolHandler.</p>
	 *
	 * @param messageHeaderLength a int.
	 */
	public DefaultClientProtocolHandler(int messageHeaderLength, MinaClientGameService service) {
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
	public MinaClientGameService getService() {
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

	@Override
	public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {

	}

}
