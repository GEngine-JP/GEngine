package info.xiaomo.gengine.network;

import lombok.Data;

@Data
public class MsgPack {
    public static final byte HEAD_TCP = -128;

    private Integer sequence = 1;

    private final byte head;
    private final int msgId;
    private final byte[] bytes;
    private Object msg;

    public MsgPack(byte head, int msgId, byte[] bytes) {
        this.head = head;
        this.msgId = msgId;
        this.bytes = bytes;
    }

    public MsgPack(byte head, int msgId, byte[] bytes, Object msg) {
        this.head = head;
        this.msgId = msgId;
        this.bytes = bytes;
        this.msg = msg;
    }
}
