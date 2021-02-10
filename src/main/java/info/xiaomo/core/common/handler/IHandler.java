package info.xiaomo.core.common.handler;

import org.apache.mina.core.session.IoSession;

/**
 * 处理器接口
 *
 *
 * @version $Id: $Id
 */
public interface IHandler extends Runnable {


	/**
	 * 会话
	 *
	 * @return a {@link IoSession} object.
	 */
	IoSession getSession();

	/**
	 * 会话
	 *
	 * @param session a {@link IoSession} object.
	 */
	void setSession(IoSession session);

	/**
	 * 请求消息
	 *
	 * @return a {@link Object} object.
	 */
	Object getMessage();

	/**
	 * 消息
	 *
	 * @param message a {@link Object} object.
	 */
	void setMessage(Object message);

	/**
	 * 创建时间
	 *
	 * @return a long.
	 */
	long getCreateTime();

	/**
	 * 创建时间
	 *
	 * @param time a long.
	 */
	void setCreateTime(long time);

	/**
	 * http 参数
	 *
	 * @return a {@link Object} object.
	 */
	Object getParameter();

	/**
	 * http 参数
	 *
	 * @param parameter a {@link Object} object.
	 */
	void setParameter(Object parameter);
}
