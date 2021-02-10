package info.xiaomo.core.network.mina.code;

/**
 * 默认消息解析工厂
 *
 * @author JiangZhiYong
 * @version $Id: $Id
 * @date 2017-03-30
 * QQ:359135103
 */
public class DefaultProtocolCodecFactory extends ProtocolCodecFactoryImpl {

	/**
	 * <p>Constructor for DefaultProtocolCodecFactory.</p>
	 */
	public DefaultProtocolCodecFactory() {
		super(new ProtocolDecoderImpl(), new ProtocolEncoderImpl());
	}
}
