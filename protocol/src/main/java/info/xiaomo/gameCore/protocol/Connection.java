package info.xiaomo.gameCore.protocol;

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
 * Date  : 2017/8/12 15:24
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public interface Connection {
    void write(Object paramObject);

    void close();

    void putAttribute(String paramString, Object paramObject);

    Object getAttribute(String paramString);

    SocketAddress remoteAddress();

    SocketAddress localAddress();
}

