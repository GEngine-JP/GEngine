/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.core.network.mina.code;

/**
 * 客户端消息解码
 *
 * 
 * @version $Id: $Id
 */
public class ClientProtocolCodecFactory extends ProtocolCodecFactoryImpl {

	/**
	 * <p>Constructor for ClientProtocolCodecFactory.</p>
	 */
	public ClientProtocolCodecFactory() {
		super(new ClientProtocolDecoder(), new ClientProtocolEncoder());
		//待发送的数据量过低，关闭当前连接
		encoder.overScheduledWriteBytesHandler = io -> {
			io.closeNow();
			return true;
		};
	}

}
