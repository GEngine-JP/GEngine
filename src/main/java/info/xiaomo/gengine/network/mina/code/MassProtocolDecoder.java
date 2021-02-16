package info.xiaomo.gengine.network.mina.code;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群发消息
 *
 * @author wzyi
 * @version $Id: $Id
 */
public class MassProtocolDecoder extends ProtocolDecoderImpl {

	/**
	 * Constant <code>log</code>
	 */
	protected static final Logger log = LoggerFactory.getLogger(MassProtocolDecoder.class);

	/**
	 * <p>Constructor for MassProtocolDecoder.</p>
	 */
	public MassProtocolDecoder() {
		maxReadSize = 1024 * 1024 * 5;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer ib, ProtocolDecoderOutput out) throws Exception {
		if (ib.remaining() < 4) {
			return false;
		}
		ib.mark();
		int length = ib.getInt();
		if (length < 1 || length > maxReadSize) {
			int id = ib.getInt();
			ib.clear();
			log.warn("消息解析异常：长度{},id {}, 大于长度 maxReadSize {}", length, id, maxReadSize);
			session.closeNow();
			return false;
		}

		if (ib.remaining() < length) {
			ib.reset();
			return false;
		}
		decodeBytes(length, ib, out);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 不包括消息长度
	 */
	protected void decodeBytes(int length, IoBuffer ib, ProtocolDecoderOutput out) {
		byte[] bytes = new byte[length];
		ib.get(bytes);
		out.write(bytes);
	}
}
