package info.xiaomo.gengine.utils;

import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigUtil {

	/**
	 * 获取项目目录下的game-conf文件夹目录
	 *
	 * @return
	 */
	public static String getConfigPath() {
		File file = new File(System.getProperty("user.dir"));
		if (file.getParent() != null) {
			return file.getParent() + "/game-conf/";
		}
		log.warn("获取项目根节点出错:{}", file.getPath());
		return file.getPath() + "/game-conf/";
	}
}
