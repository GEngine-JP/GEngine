package info.xiaomo.gengine.network.mina.code;

import info.xiaomo.gengine.common.utils.MsgUtil;
import info.xiaomo.gengine.network.mina.message.MassMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群发消息
 *
 * @author wzyi
 * @version $Id: $Id
 */
public class MassProtocolEncoder extends ProtocolEncoderImpl {

    /**
     * Constant <code>log</code>
     */
    protected static final Logger log = LoggerFactory.getLogger(MassProtocolEncoder.class);

    /**
     * <p>Constructor for MassProtocolEncoder.</p>
     */
    public MassProtocolEncoder() {
    }

    /**
     * {@inheritDoc}
     * <p>
     * 编码，格式：数据长度|数据部分
     */
    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out)
            throws Exception {
        if (getOverScheduledWriteBytesHandler() != null && session.getScheduledWriteMessages() > getMaxScheduledWriteMessages() && getOverScheduledWriteBytesHandler().test(session)) {
            return;
        }
        IoBuffer buf = null;
        if (obj instanceof MassMessage) {
            buf = MsgUtil.toIOBuffer((MassMessage) obj);
        } else {
            log.warn("未知的数据类型");
            return;
        }
        if (buf != null && session.isConnected()) {
            buf.rewind();
            out.write(buf);
            out.flush();
        }
    }
}
