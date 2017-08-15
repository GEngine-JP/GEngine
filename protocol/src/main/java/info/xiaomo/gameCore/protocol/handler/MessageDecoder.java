package info.xiaomo.gameCore.protocol.handler;

public interface MessageDecoder {
    Object decode(byte[] paramArrayOfByte);
}
