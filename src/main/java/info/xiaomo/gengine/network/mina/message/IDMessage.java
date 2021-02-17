package info.xiaomo.gengine.network.mina.message;

import info.xiaomo.gengine.common.utils.MsgUtil;
import io.netty.channel.Channel;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * 带用户ID的消息
 * <br>
 * netty消息发送必须设置msgID,mina不能设置
 *
 * 
 * @version $Id: $Id
 *  2017-03-31
 *
 */
public final class IDMessage implements Runnable {

	private final Object msg;
	private final long userID;    //用户ID或角色ID，当角色ID不存在时，用用户ID
	private IoSession session;
	private Channel channel;
	private Integer msgId;    //消息ID

	/**
	 * netty使用
	 *
	 * @param channel a {@link Channel} object.
	 * @param msg     a {@link Object} object.
	 * @param userID  a long.
	 * @param msgId   a {@link Integer} object.
	 */
	public IDMessage(Channel channel, Object msg, long userID, Integer msgId) {
		this.channel = channel;
		this.msg = msg;
		this.userID = userID;
		this.msgId = msgId;
	}

	/**
	 * mina 使用
	 *
	 * @param session a {@link IoSession} object.
	 * @param msg     a {@link Object} object.
	 * @param userID  a long.
	 */
	public IDMessage(IoSession session, Object msg, long userID) {
		this.msg = msg;
		this.userID = userID;
		this.session = session;
	}

	/**
	 * <p>Getter for the field <code>userID</code>.</p>
	 *
	 * @return a long.
	 */
	public long getUserID() {
		return userID;
	}

	/**
	 * <p>Getter for the field <code>session</code>.</p>
	 *
	 * @return a {@link IoSession} object.
	 */
	public IoSession getSession() {
		return session;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (session != null && session.isConnected()) {
			IoBuffer buf = MsgUtil.toIOBuffer(this);
			session.write(buf);
		} else if (channel != null && channel.isActive()) {
			channel.write(this);
		}
	}

	/**
	 * <p>Getter for the field <code>msg</code>.</p>
	 *
	 * @return a {@link Object} object.
	 */
	public Object getMsg() {
		return msg;
	}

	/**
	 * <p>Getter for the field <code>msgId</code>.</p>
	 *
	 * @return a {@link Integer} object.
	 */
	public Integer getMsgId() {
		return msgId;
	}

	/**
	 * <p>Setter for the field <code>msgId</code>.</p>
	 *
	 * @param msgId a {@link Integer} object.
	 */
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}


}
