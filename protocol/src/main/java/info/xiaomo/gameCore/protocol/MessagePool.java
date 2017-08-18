package info.xiaomo.gameCore.protocol;


import info.xiaomo.gameCore.protocol.message.AbstractMessage;

public interface MessagePool {

	AbstractMessage getMessage(int messageId);

}
