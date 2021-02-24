package info.xiaomo.gengine.network;

import io.netty.channel.Channel;

/** Copyright(©) 2017 by xiaomo. */
public interface ISession {

    /**
     * channel
     *
     * @param channel channel
     */
    void setChannel(Channel channel);
}
