package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import io.netty.channel.ChannelHandler;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张力 on 2017/6/26.
 */
@Data
public class ClientBuilder {

    /**
     * 网络线程池线程数量
     */
    private int nioEventLoopCount;


    /**
     * 监听端口
     */
    private int port;

    /**
     * 消息池
     */
    private MessagePool msgPool;

    /**
     * 网络消费者
     */
    private NetworkConsumer consumer;


    private NetworkEventListener listener;

    /**
     * 额外的handler
     */
    private List<ChannelHandler> extraHandlers = new ArrayList<>();


    /**
     * 主机ip
     */
    private String host;


    /**
     * 是否使用连接池
     */
    private boolean pooled;


    /**
     * 连接池大小
     */
    private int poolMaxCount;

    /**
     * 心跳时间
     */
    private int heartTime;

    /**
     * 最大闲置时间（超过这个值就认为连接异常，主动断开，然后重连
     */
    private int maxIdleTime;

    private ClientHeart.PingMessageFactory pingMessageFactory;

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
