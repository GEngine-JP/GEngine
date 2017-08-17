package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import lombok.Data;

@Data
public class ClientBuilder {

    protected String host;

    protected int port;

    private int poolSize;

    private boolean pooled;

    /**
     * 网络消费者
     */
    private NetworkConsumer consumer;

    /**
     * 事件监听器
     */
    private NetworkEventListener networkEventListener;

    private MessagePool messagePool;

    public Client build() {
        if (this.pooled) {
            if (this.poolSize <= 0) {
                this.poolSize = 8;
            }
            return new PooledClient(this);
        } else {
            return new Client(this);
        }
    }
}
