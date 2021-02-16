package info.xiaomo.gengine.common.handler;

import io.netty.channel.Channel;
import org.apache.mina.core.session.IoSession;

/**
 * 抽象handler
 *
 * @version $Id: $Id
 */
public abstract class AbsHandler implements IHandler {

    protected IoSession session;
    protected long createTime;
    protected Channel channel;

    /**
     * {@inheritDoc}
     */
    @Override
    public IoSession getSession() {
        return session;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSession(IoSession session) {
        this.session = session;
    }


    /**
     * <p>Getter for the field <code>channel</code>.</p>
     *
     * @return a {@link Channel} object.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * <p>Setter for the field <code>channel</code>.</p>
     *
     * @param channel a {@link Channel} object.
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCreateTime() {
        return createTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreateTime(long time) {
        createTime = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParameter() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParameter(Object parameter) {
    }
}
