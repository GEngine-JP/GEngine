package info.xiaomo.gameCore.protocol.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.base.concurrent.AbstractCommand;
import info.xiaomo.gameCore.protocol.Connection;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
 * Date  : 2017/8/12 15:23
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MessageHandler<M> extends AbstractCommand {
    protected Connection connection;
    protected M message;

    public abstract M decode(byte[] paramArrayOfByte) throws InvalidProtocolBufferException;

    protected abstract void handMessage(M paramM);

    public void action() {
        handMessage(this.message);
    }
}

