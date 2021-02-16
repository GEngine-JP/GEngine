package info.xiaomo.gengine.network.netty.handler;

import info.xiaomo.gengine.script.IScript;
import info.xiaomo.gengine.server.BaseServerConfig;
import info.xiaomo.gengine.server.GameService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * netty handler脚本
 * <p>
 * <p>
 * 2017年8月30日 上午10:33:10
 */
public interface IChannelHandlerScript extends IScript {

    /**
     * channel 激活
     * <p>
     * <p>
     * 2017年8月30日 上午10:36:11
     *
     * @param handlerClass
     * @param channel
     */
    default void channelActive(Class<? extends ChannelHandler> handlerClass, Channel channel) {

    }

    /**
     * channel 激活
     * <p>
     * <p>
     * 2017年8月30日 上午10:36:11
     *
     * @param handlerClass
     * @param channel
     */
    default void channelActive(Class<? extends ChannelHandler> handlerClass, GameService<? extends BaseServerConfig> service, Channel channel) {

    }

    /**
     * channel 空闲
     * <p>
     * <p>
     * 2017年8月30日 上午10:36:11
     *
     * @param handlerClass
     * @param channel
     */
    default void channelInActive(Class<? extends ChannelHandler> handlerClass, Channel channel) {

    }

}
