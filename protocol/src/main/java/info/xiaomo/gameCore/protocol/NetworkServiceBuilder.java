package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
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
     * 编码器
     */
    private MessageEncoder encoder;

    /**
     * 解码器
     */
    private MessageDecoder decoder;

    /**
     * 消息执行器
     */
    private MessageExecutor executor;

    /**
     * 额外的handler
     */
    private List<ChannelHandler> channelHandlerList = new ArrayList<>();

    public NetworkService createService() {
        return new NetworkService(this);
    }

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
        channelHandlerList.add(handler);
    }

}
