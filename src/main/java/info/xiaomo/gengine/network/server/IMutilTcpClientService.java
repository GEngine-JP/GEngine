package info.xiaomo.gengine.network.server;

import java.util.Map;
import info.xiaomo.gengine.network.mina.message.IDMessage;

/**
 * 连接多服务器客户端接口
 *
 * 
 * <p>
 * 2017年8月29日 上午9:48:52
 */
public interface IMutilTcpClientService<T extends BaseServerConfig> extends Runnable {

	/**
	 * 移除一个客户端
	 *
	 * @param serverId
	 * 
	 * <p>
	 * 2017年8月29日 上午10:00:27
	 */
	void removeTcpClient(int serverId);

	/**
	 * 添加连接服务器
	 *
	 * @param serverInfo
	 */
	void addTcpClient(ServerInfo serverInfo, int port);

	/**
	 * 服务器列表
	 *
	 * @return
	 * 
	 * <p>
	 * 2017年8月29日 上午10:06:41
	 */
	Map<Integer, ServerInfo> getServers();

	/**
	 * 监测连接状态
	 */
	void checkStatus();

	/**
	 * 广播所有服务器消息：注意，这里并不是向每个session广播，因为有可能有多个连接到同一个服务器
	 *
	 * @param obj
	 * @return
	 */
	default boolean broadcastMsg(Object obj) {
		IDMessage idm = new IDMessage(null, obj, 0);
		getServers().values().forEach(server -> {
			server.sendMsg(idm);
		});
		return true;
	}

	/**
	 * 发送消息
	 *
	 * @param serverId 目标服务器ID
	 * @param msg
	 * @return
	 */
	boolean sendMsg(Integer serverId, Object msg);


}
