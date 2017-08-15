package info.xiaomo.gameCore.protocol;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.net.SocketAddress;

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
 * Date  : 2017/8/12 15:54
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@Data
public class NettyConnection implements Connection {
    private Channel channel;

    public NettyConnection(Channel channel) {
        this.channel = channel;
    }

    public void write(Object msg) {
        this.channel.writeAndFlush(msg);
    }

    public void close() {
        this.channel.close();
    }

    public void putAttribute(String key, Object value) {
        this.channel.attr(AttributeKey.valueOf(key)).set(value);
    }

    public Object getAttribute(String key) {
        return this.channel.attr(AttributeKey.valueOf(key)).get();
    }

    public SocketAddress remoteAddress() {
        return this.channel.remoteAddress();
    }

    public SocketAddress localAddress() {
        return this.channel.localAddress();
    }


}