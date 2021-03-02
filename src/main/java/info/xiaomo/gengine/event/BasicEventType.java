package info.xiaomo.gengine.event;

/**
 * desc : 事件类型 Copyright(©) 2017 by xiaomo.
 */
public interface BasicEventType {
	/**
	 * 登录
	 */
	int LOGIN = 1001;
	/**
	 * 登出
	 */
	int LOGOUT = 1002;
	/**
	 * 服务器分钟心跳
	 */
	int SERVER_MINUTE_HEART = 1003;
	/**
	 * 服务器零点事件
	 */
	int SERVER_MIDNIGHT = 1004;
	/**
	 * 服务器秒心跳
	 */
	int SERVER_SECOND_HEART = 1005;
	/**
	 * 关服事件
	 */
	int SERVER_SHUTDOWN = 1006;
	/**
	 * 站立事件
	 */
	int STAND = 1007;
}
