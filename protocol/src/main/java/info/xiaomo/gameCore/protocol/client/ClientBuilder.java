package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import lombok.Data;

@Data
public class ClientBuilder {

    protected String host;

    protected int port;

    private MessageEncoder encoder;

    private MessageDecoder decoder;

    private MessageExecutor executor;

    public Client build() {
        return new Client(this);
    }
}
