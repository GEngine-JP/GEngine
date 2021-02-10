package info.xiaomo.core.network.mina.message;

import java.util.Collection;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 群发消息
 *
 * @author wzyi
 * @version $Id: $Id
 */
public class MassMessage {

	private final IoBuffer buffer;
	private final Collection<Long> targets;    //用户ID

	/**
	 * <p>Constructor for MassMessage.</p>
	 *
	 * @param buffer  a {@link IoBuffer} object.
	 * @param targets a {@link Collection} object.
	 */
	public MassMessage(IoBuffer buffer, Collection<Long> targets) {
		this.buffer = buffer;
		this.targets = targets;
		this.buffer.rewind();
	}

	/**
	 * <p>getLength.</p>
	 *
	 * @return a int.
	 */
	public int getLength() {
		return buffer.remaining() + targets.size() * 8;
	}

	/**
	 * <p>getBuffLength.</p>
	 *
	 * @return a int.
	 */
	public int getBuffLength() {
		return buffer.remaining();
	}

	/**
	 * <p>Getter for the field <code>buffer</code>.</p>
	 *
	 * @return the buffer
	 */
	public IoBuffer getBuffer() {
		return buffer;
	}

	/**
	 * <p>Getter for the field <code>targets</code>.</p>
	 *
	 * @return the targets
	 */
	public Collection<Long> getTargets() {
		return targets;
	}
}
