package info.xiaomo.gengine.network.netty.config;

import info.xiaomo.gengine.network.server.BaseServerConfig;
import info.xiaomo.gengine.network.server.ServerType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * netty 客户端配置
 * <p>
 * <p>
 * 2017年8月24日 下午8:18:20
 */
@EqualsAndHashCode(callSuper = true)
@Root
@Data
public class NettyClientConfig extends BaseServerConfig {

	// 工作组线程数
	@Element(required = false)
	private int groupThreadNum = 1;

	// 当前服务器的类型,如当前服务器是gameServer.那么对应ServerType.GameServer = 10
	@Element(required = false)
	private ServerType type = ServerType.GATE;

	// 其他配置,如配置服务器允许开启的地图
	@Element(required = false)
	private String info;

	//
	@Element(required = false)
	private boolean tcpNoDelay = true;

	// IP
	@Element(required = false)
	private String ip = "127.0.0.1";

	// 端口
	@Element(required = false)
	private int port = 8080;

	//客户端创建的最大连接数
	@Element(required = false)
	private int maxConnectCount = 1;


	public int getGroupThreadNum() {
		return groupThreadNum;
	}

	public void setGroupThreadNum(int groupThreadNum) {
		this.groupThreadNum = groupThreadNum;
	}


}
