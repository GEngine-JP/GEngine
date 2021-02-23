package info.xiaomo.gengine.network;

import lombok.Data;

@Data
public class Message {
    public static final byte HEAD_TCP = -128;
    public static final byte HEAD_UDP = 0;
    public static final byte HEAD_NEED_ACK = 64;
    public static final byte HEAD_ACK = 44;
    public static final byte HEAD_PROTOCOL_MASK = 3;
    public static final byte PROTOCOL_PROTOBUF = 0;
    public static final byte PROTOCOL_JSON = 1;
    private final byte head;
    private final short sid;
    private final int msgId;
    private final byte[] bytes;

    public Message(byte head, int cmd, byte[] bytes) {
        this(head, (short) 0, cmd, bytes);
    }

    public Message(byte head, short sid, int msgId, byte[] bytes) {
        this.msgId = msgId;
        this.bytes = bytes;
        this.head = head;
        this.sid = sid;
    }
}
