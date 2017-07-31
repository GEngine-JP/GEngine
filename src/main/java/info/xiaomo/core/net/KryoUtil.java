package info.xiaomo.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.UnsupportedEncodingException;

public class KryoUtil {


    private final static ThreadLocal<ByteBuf> cacheOutputs = ThreadLocal.withInitial(ByteBufAllocator.DEFAULT::heapBuffer);

    public static ByteBuf getOutput() {
        ByteBuf output = cacheOutputs.get();
        if (output.writerIndex() > 0) {
            output.writerIndex(0);
            throw new RuntimeException("output出现迭代调用，或者使用之后没有清空");
        }
        return output;
    }

    public static String readString(ByteBuf buffer) {
        int length = buffer.readShort();
        byte[] req = new byte[length];
        buffer.readBytes(req);
        String body;
        try {
            body = new String(req, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            body = null;
        }
        return body;
    }

    public static boolean writeString(ByteBuf buffer, String content) {
        try {
            byte[] strByte = content.getBytes("UTF-8");
            buffer.writeShort(strByte.length);
            buffer.writeBytes(strByte);
            return true;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean writeLong(ByteBuf buffer, long value) {
        byte[] bytes = new byte[8];
        int position = 0;
        bytes[position++] = (byte) (value >>> 56);
        bytes[position++] = (byte) (value >>> 48);
        bytes[position++] = (byte) (value >>> 40);
        bytes[position++] = (byte) (value >>> 32);
        bytes[position++] = (byte) (value >>> 24);
        bytes[position++] = (byte) (value >>> 16);
        bytes[position++] = (byte) (value >>> 8);
        bytes[position++] = (byte) value;
        buffer.writeBytes(bytes);
        return true;
    }

    public static long readLong(ByteBuf buf) {
        byte[] buffer = new byte[8];
        buf.readBytes(buffer);
        return (long) buffer[0] << 56 //
                | (long) (buffer[1] & 0xFF) << 48 //
                | (long) (buffer[2] & 0xFF) << 40 //
                | (long) (buffer[3] & 0xFF) << 32 //
                | (long) (buffer[4] & 0xFF) << 24 //
                | (buffer[5] & 0xFF) << 16 //
                | (buffer[6] & 0xFF) << 8 //
                | buffer[7] & 0xFF;
    }
}
