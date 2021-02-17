package info.xiaomo.gengine.common.struct;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.reflect.Method;
import java.util.Map;
import info.xiaomo.gengine.common.utils.JsonUtil;
import info.xiaomo.gengine.common.utils.ReflectUtil;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.key.HallKey;
import info.xiaomo.gengine.struct.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mongodb.morphia.annotations.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 大厅玩家角色实体 <br>
 * 大厅角色数据实时存储,小心消息覆盖
 *
 * @note 其他子游戏单独在另外定义角色实体，要求各子游戏数据独立，分开
 */
@EqualsAndHashCode(callSuper = true)
@Entity(value = "role", noClassnameStored = true)
@Data
public class UserRole extends Person {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(UserRole.class);

	/**
	 * setter 方法集合
	 */
	@JSONField(serialize = false)
	protected static final transient Map<String, Method> WRITEMETHODS = ReflectUtil.getReadMethod(UserRole.class);

	/**
	 * 所在游戏服类型
	 */
	@JSONField
	private int gameType;

	/**
	 * 存储玩家基本属性到redis
	 *
	 * @param propertiesName
	 */
	@Override
	public void saveToRedis(String propertiesName) {
		if (id < 1) {
			return;
		}
		String key = HallKey.Role_Map_Info.getKey(id);
		Method method = WRITEMETHODS.get(propertiesName);
		if (method != null) {
			try {
				Object value = method.invoke(this);
				if (value != null) {
					// 使用redisson
					JedisManager.getJedisCluster().hset(key, propertiesName, value.toString());
					// RMap<String, Object> map = RedissonManager.getRedissonClient().getMap(key);
					// map.put(propertiesName, value);
				} else {
					LOGGER.warn("属性{}值为null", propertiesName);
				}

			} catch (Exception e) {
				LOGGER.error("属性存储", e);
			}
		} else {
			LOGGER.warn("属性：{}未找到对应方法", propertiesName);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		if (this.nick == null || !this.nick.equals(nick)) {
			saveToRedis("nick");
		}
		this.nick = nick;
	}


	/**
	 * 道具数量
	 * <p>
	 * <p>
	 * 2017年10月24日 上午10:23:57
	 *
	 * @return
	 */
	public long getItemCount() {
		String key = HallKey.Role_Map_Packet.getKey(id);
		return JedisManager.getJedisCluster().hlen(key);
	}


	/**
	 * 获取道具
	 * <p>
	 * <p>
	 * 2017年10月24日 上午10:21:18
	 *
	 * @param itemId
	 * @return
	 */
	public GameItem getItem(long itemId) {
		String key = HallKey.Role_Map_Packet.getKey(id);
		return JedisManager.getInstance().hget(key, itemId, GameItem.class);
	}

	/**
	 * 所有道具
	 * <p>
	 * <p>
	 * 2017年10月24日 上午10:36:11
	 *
	 * @return
	 */
	public Map<Long, GameItem> getItems() {
		String key = HallKey.Role_Map_Packet.getKey(id);
		return JedisManager.getInstance().hgetAll(key, Long.class, GameItem.class);
	}


	/**
	 * 角色存redis key
	 * <p>
	 * <p>
	 * 2017年9月26日 下午5:02:40
	 *
	 * @return
	 */
	public String getRoleRedisKey() {
		return HallKey.Role_Map_Info.getKey(id);
	}

	/**
	 * 存储整个role对象
	 * <p>
	 * <p>
	 * 2017年9月26日 下午5:06:51
	 */
	public void saveToRedis() {
		JedisManager.getJedisCluster().hmset(getRoleRedisKey(), JsonUtil.object2Map(this));
	}
}
