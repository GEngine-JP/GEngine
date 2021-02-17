package info.xiaomo.gengine.persist.redis.redisson;

import java.io.IOException;
import info.xiaomo.gengine.utils.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.bson.BsonReader;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.redisson.client.codec.Codec;
import org.redisson.client.protocol.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义fastjson序列化，为了兼容之前jedis序列化方式 <br>
 * 不同序列化工具，序列化字符串不一样 <br>
 * <p>
 * key：为字符串 value:为json字符串
 * </p>
 * TODO 待完善
 *
 * 
 * 2017年9月29日 下午5:36:35
 */
public class FastJsonCodec implements Codec {
	private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonCodec.class);
	private final Encoder encoder = new Encoder() {
		@Override
		public ByteBuf encode(Object in) throws IOException {
			ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
//			out.writeCharSequence(JsonUtil.toJSONStringWriteClassNameWithFiled(in), CharsetUtil.UTF_8);
			return out;
		}
	};
	private Class<?> keyClass;
	private Class<?> valueClass;
	private final org.bson.codecs.Decoder<Object> decoder = new Decoder<Object>() {
		@Override
		public Object decode(BsonReader bsonReader, DecoderContext decoderContext) {
			String str = bsonReader.readString();
			try {
				if (valueClass != null && str.startsWith("{")) {
					return JsonUtil.parseObject(str, valueClass);
				} else if (keyClass != null && !str.startsWith("{")) {
					if (keyClass.isAssignableFrom(Long.class)) {
						return Long.parseLong(str);
					} else if (keyClass.isAssignableFrom(Integer.class)) {
						return Integer.parseInt(str);
					}
				} else {
					return str;
				}
			} catch (Exception e) {
				LOGGER.error(String.format("反序列化%s失败", str), e);
			}
			return "";
		}


	};

	public FastJsonCodec(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	/**
	 * key对象类型
	 *
	 * @param valueClass value对象类型
	 */
	public FastJsonCodec(Class<?> keyClass, Class<?> valueClass) {
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}

	public FastJsonCodec() {
	}

	@Override
	public org.redisson.client.protocol.Decoder<Object> getMapValueDecoder() {
		return null;
	}

	@Override
	public Encoder getMapValueEncoder() {
		return encoder;
	}

	@Override
	public org.redisson.client.protocol.Decoder<Object> getMapKeyDecoder() {
		return null;
	}

	@Override
	public Encoder getMapKeyEncoder() {
		return encoder;
	}

	@Override
	public org.redisson.client.protocol.Decoder<Object> getValueDecoder() {
		return null;
	}


	@Override
	public Encoder getValueEncoder() {
		return encoder;
	}

}
