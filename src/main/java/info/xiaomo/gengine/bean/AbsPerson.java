package info.xiaomo.gengine.bean;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import info.xiaomo.gengine.network.mina.message.IDMessage;
import io.netty.channel.Channel;
import lombok.Data;
import org.apache.mina.core.session.IoSession;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 人物
 * <br>
 * 组合替换继承
 * <p>
 * <p>
 * 2017年7月26日 下午1:40:11
 */
@Data
public abstract class AbsPerson {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbsPerson.class);

	@JSONField
	@Id
	protected long id;

	/**
	 * 昵称
	 */
	@JSONField
	@Indexed
	protected String nick;

	/**
	 * 用户ID
	 */
	@JSONField
	@Indexed
	protected long userId;

	/**
	 * 金币
	 */
	@JSONField
	protected long gold;

	/**
	 * 钻石
	 */
	@JSONField
	protected long gem;

	/**
	 * 所在大厅ID
	 */
	@JSONField
	protected int hallId;

	/**
	 * 所在游戏服ID
	 */
	@JSONField
	protected int gameId;

	/**
	 * 头像
	 */
	@JSONField
	protected String head;

	/**
	 * 创建时间
	 */
	@JSONField
	protected Date createTime;

	/**
	 * 登录时间
	 */
	@JSONField
	protected Date loginTime;

	/**
	 * 等级
	 */
	@JSONField
	protected int level;

	/**
	 * 连接会话
	 */
	protected transient IoSession ioSession;

	protected transient Channel channel;

	public void saveToRedis(String propertiesName) {

	}

	/**
	 * 发送消息，带ID头
	 * <p>
	 * <p>
	 * 2017年8月3日 下午2:53:28
	 */
	public boolean sendMsg(Object message) {
		if (getIoSession() != null) {
			IDMessage idm = new IDMessage(getIoSession(), message, getId());
			getIoSession().write(idm);
			return true;
		} else if (getChannel() != null) {
			getChannel().writeAndFlush(new IDMessage(channel, message, getId(), null));
		} else {
			LOGGER.warn("连接session==null | channel==null {}", message);
		}
		return false;
	}
}
