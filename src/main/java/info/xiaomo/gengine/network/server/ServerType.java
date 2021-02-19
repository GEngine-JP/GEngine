package info.xiaomo.gengine.network.server;

/**
 * 服务器类型
 * <p>
 * <p>
 * 2017-03-30
 */
public interface ServerType {
	int NONE = -1;

	/**
	 * 网关
	 */
	int GATE = 1;

	/**
	 * 游戏
	 */
	int GAME = 2;

	/**
	 * 集群管理服
	 */
	int CLUSTER = 3;

	int LOG = 4;

	/**
	 * 聊天
	 */
	int CHAT = 5;

	int PAY = 6;

	/**
	 * 大厅
	 */
	int HALL = 7;

	// 游戏100开始

	//备用 1000开始
	int GAME_1 = 1001;

	int GAME_2 = 1002;


}
