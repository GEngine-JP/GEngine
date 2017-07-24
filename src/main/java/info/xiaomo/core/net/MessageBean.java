package info.xiaomo.core.net;

import io.netty.buffer.ByteBuf;

/**
 * 消息Bean的基类
 *
 * @author xiaomo
 */
public abstract class MessageBean {

    /**
     * 将属性字段写入buffer中,该方法有具体的消息实现
     *
     * @param output output
     */
    public abstract boolean write(ByteBuf output);

    /**
     * 读取属性字段，该方法有具体的消息实现
     *
     * @param input input
     */
    public abstract boolean read(ByteBuf input);

    /**
     * 向IOBuff中写入一个int
     *
     * @param output output
     * @param value  value
     */
    protected void writeInt(ByteBuf output, int value) {
        output.writeInt(value);
    }

    /**
     * 向IOBuffer中写入一个字符串
     *
     * @param output output
     * @param value  value
     */
    protected void writeString(ByteBuf output, String value) {
        short length = output.readShort();
        byte[] bytes = new byte[length];
        output.readBytes(bytes);
        String str = new String(bytes);
    }

    /**
     * 写入一个long（未优化）
     *
     * @param output output
     * @param value  value
     */
    protected void writeLong(ByteBuf output, long value) {
        output.writeLong(value);
    }

    /**
     * 写入一个bean
     *
     * @param output output
     * @param value  value
     */
    protected void writeBean(ByteBuf output, MessageBean value) {
        if (value == null) {
            output.writeByte(0);
        } else {
            output.writeByte(1);
            value.write(output);
        }

    }

    /**
     * 写入一个short
     *
     * @param output output
     * @param value  value
     */
    protected void writeShort(ByteBuf output, int value) {
        output.writeShort((short) value);
    }

    /**
     * 写入一个short
     *
     * @param output output
     * @param value  value
     */
    protected void writeShort(ByteBuf output, short value) {
        output.writeShort(value);
    }

    /**
     * 写入一个byte
     *
     * @param output output
     * @param value  value
     */
    protected void writeByte(ByteBuf output, byte value) {
        output.writeByte(value);
    }

    /**
     * 写入一个字节数组
     *
     * @param output output
     * @param bytes  bytes
     */
    protected void writeBytes(ByteBuf output, byte[] bytes) {
        output.writeBytes(bytes);
    }

    protected void writeBoolean(ByteBuf output, boolean value) {
        output.writeBoolean(value);
    }


    /**
     * 读取一个int值（未优化过的int）
     *
     * @param input input
     * @return int
     */
    protected int readInt(ByteBuf input) {
        return input.readInt();
    }

    /**
     * 读取字符串
     *
     * @param input input
     * @return String
     */
    protected String readString(ByteBuf input) {
        short length = input.readShort();
        byte[] bytes = new byte[length];
        input.readBytes(bytes);
        return new String(bytes);
    }

    /**
     * 读取一个Long（未优化过的）
     *
     * @param input input
     * @return long
     */
    protected long readLong(ByteBuf input) {
        return input.readLong();
    }

    /**
     * 读取一个Bean
     *
     * @param input input
     * @param input input
     * @return MessageBean
     */
    protected MessageBean readBean(ByteBuf input, Class<? extends MessageBean> clazz) {
        byte isNull = input.readByte();
        if (isNull == 0) {
            return null;
        }
        try {
            // 首先反射建立一个Bean
            MessageBean bean = clazz.newInstance();
            // 读取Bean
            bean.read(input);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取short
     *
     * @param input input
     * @return short
     */
    protected short readShort(ByteBuf input) {
        return input.readShort();
    }

    /**
     * 读取byte
     *
     * @param input input
     * @return byte
     */
    protected byte readByte(ByteBuf input) {
        return input.readByte();
    }

    /**
     * @param input input
     * @return input
     */
    protected boolean readBoolean(ByteBuf input) {
        return input.readBoolean();
    }
}