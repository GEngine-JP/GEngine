
package info.xiaomo.gengine.ai.msg;

/**
 * 提供額外信息接口
 * in a Telegram of a given type to the newly registered {@link Telegraph}.
 *
 * @author avianey
 */
public interface TelegramProvider {
	/**
	 * type.
	 *
	 * @param msg      the message type to provide
	 * @param receiver the newly registered Telegraph. Providers can provide different info depending on the targeted Telegraph.
	 * @return extra info to dispatch in a Telegram or null if nothing to dispatch
	 * @see MessageDispatcher#addListener(Telegraph, int)
	 * @see MessageDispatcher#addListeners(Telegraph, int...)
	 */
	Object provideMessageInfo(int msg, Telegraph receiver);
}
