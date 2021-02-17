package info.xiaomo.gengine.network.handler;

import com.google.protobuf.Message;
import info.xiaomo.gengine.entity.AbsPerson;
import info.xiaomo.gengine.network.mina.message.IDMessage;

/**
 * Tcp 处理器
 * <br>也可能处理udp请求
 *
 * @version $Id: $Id
 */
public abstract class TcpHandler extends AbsHandler {
	protected long rid; // 角色ID
	protected AbsPerson person; // 角色
	private Message message;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message getMessage() {
		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMessage(Object message) {
		this.message = (Message) message;
	}

	/**
	 * 获取消息
	 *
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Message> T getMsg() {
		return (T) message;
	}

	/**
	 * <p>Getter for the field <code>person</code>.</p>
	 *
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbsPerson> T getPerson() {
		return (T) person;
	}

	/**
	 * <p>Setter for the field <code>person</code>.</p>
	 */
	public void setPerson(AbsPerson person) {
		this.person = person;
	}

	/**
	 * <p>Getter for the field <code>rid</code>.</p>
	 *
	 * @return a long.
	 */
	public long getRid() {
		return rid;
	}

	/**
	 * <p>Setter for the field <code>rid</code>.</p>
	 *
	 * @param rid a long.
	 */
	public void setRid(long rid) {
		this.rid = rid;
	}

	/**
	 * 发送带ID的消息
	 *
	 * @param object a {@link Object} object.
	 */
	public void sendIdMsg(Object object) {
		if (getSession() != null && getSession().isConnected()) {
			getSession().write(new IDMessage(session, object, rid));
		} else if (getChannel() != null && getChannel().isActive()) {
			getChannel().writeAndFlush(new IDMessage(channel, object, rid, null));
		}
	}

}
