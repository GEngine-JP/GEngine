package info.xiaomo.gameCore.protocol.entity;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;

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
 * Date  : 2017/7/11 16:59
 * desc  : session
 * Copyright(©) 2017 by xiaomo.
 */
@Data
public class Session {

    protected Channel channel;

    public ChannelFuture close() {
        return channel.close();
    }
}
