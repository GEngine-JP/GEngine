package info.xiaomo.core.network.mina.service;

import java.util.Map;
import info.xiaomo.core.network.mina.MinaTcpClient;
import info.xiaomo.core.network.mina.code.ProtocolCodecFactoryImpl;
import info.xiaomo.core.network.mina.config.MinaClientConfig;
import info.xiaomo.core.network.mina.handler.DefaultClientProtocolHandler;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;

/**
 * 单个tcp连接客户端
 *
 * @author JiangZhiYong
 * @version $Id: $Id
 * @date 2017-04-09 QQ:359135103
 */
public class SingleMinaTcpClientService extends MinaClientService {
	private final MinaTcpClient tcpClient;


	/**
	 * <p>Constructor for SingleMinaTcpClientService.</p>
	 *
	 * @param ioHandler a {@link IoHandler} object.
	 * @param filters   a {@link Map} object.
	 */
	public SingleMinaTcpClientService(MinaClientConfig minaClientConfig, ProtocolCodecFactoryImpl factory, IoHandler ioHandler, Map<String, IoFilter> filters) {
		super(minaClientConfig);
		tcpClient = new MinaTcpClient(this, minaClientConfig, ioHandler, factory, filters);
	}


	/**
	 * <p>Constructor for SingleMinaTcpClientService.</p>
	 *
	 * @param ioHandler 消息处理器
	 */
	public SingleMinaTcpClientService(MinaClientConfig minaClientConfig, ProtocolCodecFactoryImpl factory, IoHandler ioHandler) {
		super(minaClientConfig);
		tcpClient = new MinaTcpClient(this, minaClientConfig, ioHandler, factory);
	}

	/**
	 * <p>Constructor for SingleMinaTcpClientService.</p>
	 *
	 * @param ioHandler 消息处理器
	 */
	public SingleMinaTcpClientService(MinaClientConfig minaClientConfig, IoHandler ioHandler) {
		super(minaClientConfig);
		tcpClient = new MinaTcpClient(this, minaClientConfig, ioHandler);
	}

	/**
	 * 默认消息处理器
	 */
	public SingleMinaTcpClientService(MinaClientConfig minaClientConfig) {
		super(minaClientConfig);
		tcpClient = new MinaTcpClient(this, minaClientConfig, new DefaultClientProtocolHandler(this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void running() {
		tcpClient.run();

	}

	/**
	 * <p>Getter for the field <code>tcpClient</code>.</p>
	 */
	public MinaTcpClient getTcpClient() {
		return tcpClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkStatus() {
		tcpClient.checkStatus();
	}
	

}
