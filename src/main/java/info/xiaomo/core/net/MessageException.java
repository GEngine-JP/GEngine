
package info.xiaomo.core.net;

/**
 * General Kryo RuntimeException.
 * 
 * @author Nathan Sweet <misc@n4te.com>
 */
public class MessageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6666173322042239859L;

	private StringBuffer trace;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		if (trace == null)
			return super.getMessage();
		StringBuilder buffer = new StringBuilder(512);
		buffer.append(super.getMessage());
		if (buffer.length() > 0)
			buffer.append('\n');
		buffer.append("Serialization trace:");
		buffer.append(trace);
		return buffer.toString();
	}

	/**
	 * Adds information to the exception message about where in the the object
	 * graph serialization failure occurred. {@link Serializer Serializers} can
	 * catch {@link MessageException}, add trace information, and rethrow the
	 * exception.
	 */
	public void addTrace(String info) {
		if (info == null)
			throw new IllegalArgumentException("info cannot be null.");
		if (trace == null)
			trace = new StringBuffer(512);
		trace.append('\n');
		trace.append(info);
	}
}
