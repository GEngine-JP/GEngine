package info.xiaomo.gengine.network.client;

import java.util.ArrayList;
import java.util.List;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.client.listener.ClientListener;
import info.xiaomo.gengine.network.pool.MessagePool;
import io.netty.channel.ChannelHandler;
import lombok.Data;

/** Created by 张力 on 2017/6/26. */
@Data
public class ClientBuilder {

    private int upLimit = 2048; // 解码大小限制

    private int downLimit = 5120; // 编码大小限制

    /** 网络线程池线程数量 */
    private int nioEventLoopCount;

    /** 监听端口 */
    private int port;

    /** 消息池 */
    private MessagePool msgPool;

    /** 网络消费者 */
    private INetworkConsumer consumer;

    /** 额外的handler */
    private List<ChannelHandler> extraHandlers = new ArrayList<>();

    /** 主机ip */
    private String host;

    /** 是否使用连接池 */
    private boolean pooled;

    /** 连接池大小 */
    private int poolMaxCount;

    /** 心跳时间 */
    private int heartTime;

    /** 最大闲置时间（超过这个值就认为连接异常，主动断开，然后重连 */
    private int maxIdleTime;

    /** 心跳消息 */
    private ClientHeart.PingMessageFactory pingMessageFactory;

    /** 客户端时间监听器 */
    private ClientListener clientListener;

    /** 网络事件监听器 */
    private INetworkEventListener eventListener;

    /** 是否需要断线重连功能 */
    private boolean needReconnect = true;

    public Client createClient() {

        if (this.pooled) {
            if (this.poolMaxCount <= 0) {
                this.poolMaxCount = 1;
            }
            return new PooledClient(this);
        } else {
            return new Client(this);
        }
    }
}
