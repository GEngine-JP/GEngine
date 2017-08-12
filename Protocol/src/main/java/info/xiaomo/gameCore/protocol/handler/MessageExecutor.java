package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.protocol.Connection;

public interface MessageExecutor {
    void doCommand(Connection paramConnection, Object paramObject) throws Exception;

    void connected(Connection paramConnection);

    void disconnected(Connection paramConnection);

    void exceptionCaught(Connection paramConnection, Throwable paramThrowable);
}

