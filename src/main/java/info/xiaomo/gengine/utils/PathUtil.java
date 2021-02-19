package info.xiaomo.gengine.utils;

import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathUtil {

	/**
	 * 获取项目目录下的game-conf文件夹目录
	 *
	 * @return
	 */
	public static String getConfigPath() {
		File file = new File(System.getProperty("user.dir"));
		return file.getPath() + "/game-conf/";
	}


}
