package info.xiaomo.gengine.network.netty;

import info.xiaomo.gengine.network.netty.code.DefaultClientChannelInitializer;
import info.xiaomo.gengine.network.netty.config.NettyClientConfig;
import info.xiaomo.gengine.network.netty.service.NettyClientService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty多连接tcp，连接多个服务器
 * 
 *
 *
 * 2017年8月28日 下午1:52:40
 */
public class NettyMutilTcpClient {
	private static final Logger LOGGER=LoggerFactory.getLogger(NettyMutilTcpClient.class);
	
	private final Map<Integer, NettyTcpClient> tcpClients=new ConcurrentHashMap<>();

	public NettyMutilTcpClient() {
    }
	
	/**
	 * 添加客户端
	 * 
	 * @param service
	 * @param config
	 */
	public void addTcpClient(NettyClientService service, NettyClientConfig config, ChannelInitializer<SocketChannel> channelInitializer) {
		NettyTcpClient client = null;
		if (tcpClients.containsKey(config.getId())) {
			client = tcpClients.get(config.getId());
			client.setNettyClientConfig(config);
			return;
		}
		client = new NettyTcpClient(service, channelInitializer,config);
//		new Thread(client).start();
		tcpClients.put(config.getId(), client);
	}

	/**
	 * 添加客户端
	 * 
	 * @param service
	 * @param config
	 */
	public void addTcpClient(NettyClientService service, NettyClientConfig config) {
        addTcpClient(service, config, new DefaultClientChannelInitializer(service));
	}

	public NettyTcpClient getTcpClient(Integer id) {
		if (!tcpClients.containsKey(id)) {
			return null;
		}
		return tcpClients.get(id);
	}

	public void removeTcpClient(Integer id) {
		tcpClients.remove(id);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public boolean containsKey(Integer id) {
		return tcpClients.containsKey(id);
	}

	/**
	 * 向服务器发送数据
	 *
	 * @param sid
	 *            客户端ID
	 * @param obj 消息
	 * @return
	 */
	public boolean sendMsg(Integer sid, Object obj) {
		if (!tcpClients.containsKey(sid)) {
			return false;
		}
		NettyTcpClient client = tcpClients.get(sid);
		if (client == null) {
			return false;
		}
		return client.getService().sendMsg(obj);
	}
	
	
	/**
	 * 监测服务器状态
	 *
	 *
	 * 2017年8月28日 下午5:31:05
	 */
	public void checkStatus(){
		tcpClients.forEach((id,client)->client.checkStatus());
	}

	public Map<Integer, NettyTcpClient> getTcpClients() {
		return tcpClients;
	}
}
