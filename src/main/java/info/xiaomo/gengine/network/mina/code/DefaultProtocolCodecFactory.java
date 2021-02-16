package info.xiaomo.gengine.network.mina.code;

/**
 * 默认消息解析工厂
 *
 *
 * @version $Id: $Id
 * @date 2017-03-30
 *
 */
public class DefaultProtocolCodecFactory extends ProtocolCodecFactoryImpl {

	/**
	 * <p>Constructor for DefaultProtocolCodecFactory.</p>
	 */
	public DefaultProtocolCodecFactory() {
		super(new ProtocolDecoderImpl(), new ProtocolEncoderImpl());
	}
}
