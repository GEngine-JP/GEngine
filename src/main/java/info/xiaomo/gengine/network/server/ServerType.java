package info.xiaomo.gengine.network.server;

/**
 * 服务器类型
 * <p>
 * <p>
 * 2017-03-30
 */
public enum ServerType {
	NONE(-1),
	/**
	 * 网关
	 */
	GATE(1),

	/**
	 * 游戏
	 */
	GAME(2),
	/**
	 * 集群管理服
	 */
	CLUSTER(3),
	LOG(4),
	/**
	 * 聊天
	 */
	CHAT(5),
	PAY(6),
	/**
	 * 大厅
	 */
	HALL(7),

	// 游戏100开始

	//备用 1000开始
	GAME_1(1001),
	GAME_2(1002),
	GAME_3(1003),
	GAME_4(1004),
	GAME_5(1005),
	GAME_6(1006),
	GAME_7(1007),
	GAME_8(1008),


	;


	private final int type;

	ServerType(int type) {
		this.type = type;
	}

	public static ServerType valueOf(int type) {
		for (ServerType t : ServerType.values()) {
			if (t.getType() == type) {
				return t;
			}
		}
		return NONE;
	}

	public int getType() {
		return type;
	}
}
