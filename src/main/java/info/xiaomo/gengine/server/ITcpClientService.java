package info.xiaomo.gengine.server;

/**
 * 客户端接口
 *
 * 
 * <p>
 * 2017年8月29日 上午10:31:07
 */
public interface ITcpClientService<T extends BaseServerConfig> extends Runnable {

	/**
	 * 发送消息
	 *
	 * @param object
	 * @return
	 * 
	 * <p>
	 * 2017年8月29日 上午10:28:04
	 */
	boolean sendMsg(Object object);

	/**
	 * 检测服务器状态
	 *
	 * 
	 * <p>
	 * 2017年8月29日 上午10:43:07
	 */
	void checkStatus();
}
