package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.base.common.EncryptUtil;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
	
	private MessagePool msgPool;

	/**
	 *@param maxFrameLength  最大长度
	 * @param lengthFieldOffset 长度字段的偏移位置
	 * @param lengthFieldLength 长度字段的长度
	 * @param lengthAdjustment 长度调整，比如说 -2 那么实际上长度 就变成了 消息中的长度  - (-2)  
	 * @param initialBytesToStrip 解码返回的byte中，需要跳过的字节数，比如说可以设置跳过头部信息
	 * @throws IOException
	 */
	private MessageDecoder(MessagePool msgPool, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			 int lengthAdjustment, int initialBytesToStrip) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		this.msgPool = msgPool;
	}
	
	public MessageDecoder(MessagePool msgPool) throws IOException {
		this(msgPool, 1024 * 1024, 0, 4, -4, 0);
	}

	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		try {
			
			final int length = frame.readInt();
			// 消息
			final int id = frame.readInt();
			
			// 消息ID
			//final short validateId = in.readShort();
			
			AbstractMessage message = msgPool.getMessage(id);
			
			if(message == null){
				LOGGER.error("未注册的消息,id:" + id);
				return null;
			}
			byte[] bytes = null;
			int remainLength = frame.readableBytes();
			if(remainLength > 0){
				bytes = new byte[remainLength];
				frame.readBytes(bytes);
				bytes = EncryptUtil.decrypt(bytes);
			}
			
			message.setSize(length);
			if(bytes != null){
				message.decode(bytes);
			}
			return message;
			
		} catch (Exception e) {
			LOGGER.error(ctx.channel() + " 再处理消息的时候出错~", e);
			e.printStackTrace();
			return null;
		} finally{
			frame.release();
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
