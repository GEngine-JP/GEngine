package info.xiaomo.core.network.mina.code;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * http消息解码编码工厂
 *
 * 
 * @version $Id: $Id
 * @date 2017-03-31
 */
public class HttpServerCodecImpl extends ProtocolCodecFilter {

	/**
	 * Key for decoder current state
	 */
	private static final String DECODER_STATE_ATT = "http.ds";

	/**
	 * Key for the partial HTTP requests head
	 */
	private static final String PARTIAL_HEAD_ATT = "http.ph";
	private static final ProtocolEncoder encoder = new HttpServerEncoderImpl();
	private static final ProtocolDecoder decoder = new HttpServerDecoderImpl();

	/**
	 * <p>Constructor for HttpServerCodecImpl.</p>
	 */
	public HttpServerCodecImpl() {
		super(encoder, decoder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		super.sessionClosed(nextFilter, session);
		session.removeAttribute(DECODER_STATE_ATT);
		session.removeAttribute(PARTIAL_HEAD_ATT);
	}
}
