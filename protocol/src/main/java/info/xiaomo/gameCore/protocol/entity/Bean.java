package info.xiaomo.gameCore.protocol.entity;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/12 15:29
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public abstract class Bean {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Bean.class);

    public abstract void read(ByteBuf paramByteBuf);

    public abstract void write(ByteBuf paramByteBuf);

    protected boolean readBoolean(ByteBuf byteBuf) {
        return byteBuf.readBoolean();
    }

    protected void writeBoolean(ByteBuf byteBuf, boolean value) {
        byteBuf.writeBoolean(value);
    }

    protected byte readByte(ByteBuf byteBuf) {
        return byteBuf.readByte();
    }

    protected void writeByte(ByteBuf byteBuf, int value) {
        byteBuf.writeByte(value);
    }

    protected short readShort(ByteBuf byteBuf) {
        return byteBuf.readShort();
    }

    protected void writeShort(ByteBuf byteBuf, int value) {
        byteBuf.writeShort(value);
    }

    protected int readInt(ByteBuf byteBuf) {
        return byteBuf.readInt();
    }

    protected void writeInt(ByteBuf byteBuf, int value) {
        byteBuf.writeInt(value);
    }

    protected long readLong(ByteBuf byteBuf) {
        return byteBuf.readLong();
    }

    protected void writeLong(ByteBuf byteBuf, long value) {
        byteBuf.writeLong(value);
    }

    protected String readString(ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        if (length < 0) {
            return null;
        }
        if (length == 0) {
            return "";
        }
        if (byteBuf.readableBytes() < length) {
            return null;
        }
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("解析字符串失败", e);
        }
        return null;
    }

    protected void writeString(ByteBuf byteBuf, String value) {
        if (value == null) {
            byteBuf.writeInt(-1);
            return;
        }
        if (value.isEmpty()) {
            byteBuf.writeInt(0);
            return;
        }
        try {
            byte[] bytes = value.getBytes("UTF-8");
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("编码字符串失败", e);
        }
    }

    protected <T extends Bean> T readBean(ByteBuf byteBuf, Class<T> cls) {
        boolean isNull = byteBuf.readBoolean();
        if (isNull) {
            return null;
        }
        try {
            T bean = cls.newInstance();
            bean.read(byteBuf);
            return bean;
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("解析Bean失败", e);
        }
        return null;
    }

    protected <T extends Bean> void writeBean(ByteBuf byteBuf, T bean) {
        if (bean == null) {
            byteBuf.writeBoolean(true);
            return;
        }
        byteBuf.writeBoolean(false);
        bean.write(byteBuf);
    }
}
