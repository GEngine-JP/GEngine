package info.xiaomo.core.network;

import io.netty.channel.ChannelHandler;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NetworkServiceBuilder {

    /**
     * 网络线程池线程数量
     */
    private int bossLoopGroupCount;
    /**
     * 工作线程池线程数量
     */
    private int workerLoopGroupCount;
    /**
     * 监听端口
     */
    private int port;

    /**
     * 网络消费者
     */
    private INetworkConsumer consumer;

    /**
     * 事件监听器
     */
    private INetworkEventListener listener;

    /**
     * 消息池
     */
    private IMessageAndHandler IMessageAndHandler;

    /**
     * 默认为false
     */
    private boolean isWebSocket = false;

    /**
     * 额外的handler
     */
    private List<ChannelHandler> extraHandlers = new ArrayList<>();

    /**
     * 添加一个handler，该handler由外部定义.</br>
     * 注意，所有handler都按照本方法调用顺序添加在默认handler的后面.</br>
     * 也就是说 对于 inbound来，该方法添加的handler是最后执行的，对于outbound该方法添加的handler是最先执行的.</br>
     *
     * @param handler handler
     */
    public void addChannelHandler(ChannelHandler handler) {
        if (handler == null) {
            throw new NullPointerException("指定的handler为空");
        }
        extraHandlers.add(handler);
    }

    public NetworkServiceImpl createService() {
        return new NetworkServiceImpl(this);
    }
}
