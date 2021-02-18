package info.xiaomo.gengine.event;

/**
 * desc  : 事件类型
 * Copyright(©) 2017 by xiaomo.
 */
public interface BasicEventType {
	/**
	 * 登录
	 */
	int LOGIN = 1;
	/**
	 * 登出
	 */
	int LOGOUT = 2;
	/**
	 * 服务器分钟心跳
	 */
	int SERVER_MINUTE_HEART = 3;
	/**
	 * 服务器零点事件
	 */
	int SERVER_MIDNIGHT = 4;
	/**
	 * 服务器秒心跳
	 */
	int SERVER_SECOND_HEART = 5;
	/**
	 * 关服事件
	 */
	int SERVER_SHUTDOWN = 6;

}
