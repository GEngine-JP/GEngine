package info.xiaomo.core.network.mina.service;

import java.util.concurrent.PriorityBlockingQueue;
import info.xiaomo.core.network.mina.config.MinaClientConfig;
import info.xiaomo.core.server.ITcpClientService;
import info.xiaomo.core.server.Service;
import info.xiaomo.core.thread.ThreadPoolExecutorConfig;
import org.apache.mina.core.session.IoSession;

/**
 * 内部客户端服务
 *
 * @author JiangZhiYong
 * @version $Id: $Id
 * @date 2017-04-01 QQ:359135103
 */
public abstract class MinaClientService extends Service<MinaClientConfig> implements ITcpClientService<MinaClientConfig> {

	private final MinaClientConfig minaClientConfig;

	/**
	 * 连接会话
	 */
	private final PriorityBlockingQueue<IoSession> sessions = new PriorityBlockingQueue<>(128,
			(IoSession o1, IoSession o2) -> {
				int res = o1.getScheduledWriteMessages() - o2.getScheduledWriteMessages();
				if (res == 0) {
					res = (int) (o1.getWrittenBytes() - o2.getWrittenBytes());
				}
				return res;
			});

	/**
	 * 无线程池
	 */
	public MinaClientService(MinaClientConfig minaClientConfig) {
		this(null, minaClientConfig);
	}

	/**
	 * <p>Constructor for MinaClientService.</p>
	 */
	public MinaClientService(ThreadPoolExecutorConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig) {
		super(threadPoolExecutorConfig);
		this.minaClientConfig = minaClientConfig;
	}

	/**
	 * 连接建立
	 *
	 * @param session a {@link IoSession} object.
	 */
	public void onIoSessionConnect(IoSession session) {
		sessions.add(session);
	}

	/**
	 * 连接关闭移除
	 *
	 * @param session a {@link IoSession} object.
	 */
	public void onIoSessionClosed(IoSession session) {
		sessions.remove(session);
	}

	/**
	 * <p>isSessionEmpty.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSessionEmpty() {
		return sessions.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 发送消息
	 */
	public boolean sendMsg(Object obj) {
		IoSession session = getMostIdleIoSession();
		if (session != null) {
			session.write(obj);
			return true;
		}
		return false;
	}

	/**
	 * 获取连接列表中最空闲的有效的连接
	 *
	 * @return a {@link IoSession} object.
	 */
	public IoSession getMostIdleIoSession() {
		IoSession session = null;
		while (session == null && !sessions.isEmpty()) {
			session = sessions.peek();
			if (session != null && session.isConnected()) {
				break;
			} else {
				sessions.poll();
			}
		}
		return session;
	}

	/**
	 * <p>Getter for the field <code>minaClientConfig</code>.</p>
	 */
	public MinaClientConfig getMinaClientConfig() {
		return minaClientConfig;
	}

}
