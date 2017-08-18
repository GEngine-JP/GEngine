package info.xiaomo.gameCore.protocol.message;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.base.common.EncryptUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息基类
 * 
 * @author 张力
 * @date 2014-12-1
 */
@Data
public abstract class AbstractMessage {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessage.class);

	/**
	 * 校验ID
	 */
	private short validateId;

	/**
	 * 消息长度
	 */
	private int size;

	/**
	 * 解码
	 * 
	 * @param bytes bytes
	 */
	public abstract void decode(byte[] bytes) throws InvalidProtocolBufferException;

	public abstract byte[] getContent();

	public abstract int getId();

	/**
	 * 消息编码,请在调用该方法的同一线程池之中释放掉返回的ByteBuf，否则可能产生内存泄露
	 * 
	 * @return ByteBuf
	 */
	public ByteBuf encode() {
		ByteBuf buf = null;
		try {

			// length(4) + id(4) + content
			byte[] content = getContent();
			content = EncryptUtil.encrypt(content);

			// 计算长度
			int size = 8;
			size += content.length;
			setSize(size);

			buf  = ByteBufAllocator.DEFAULT.buffer(size);

			// 写入消息头
			buf.writeInt(size);
			buf.writeInt(getId());
			// 写入消息体   0001111111110111
			buf.writeBytes(content);
			return buf;
		} catch (Exception e) {
			LOGGER.error("构建消息字节数组出错,id:" + this.getId(), e);
			if (buf != null) {
				buf.release();
			}
			return null;
		}
	}



}