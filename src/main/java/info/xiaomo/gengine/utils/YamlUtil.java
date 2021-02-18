package info.xiaomo.gengine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

@Slf4j
public class YamlUtil {

	public static <T> T read(String file, Class<T> clazz) {
		InputStream input;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.error("读取yaml文件失败:{}", file, e);
			return null;
		}
		Yaml yaml = new Yaml();
		return yaml.loadAs(input, clazz);
	}
}
