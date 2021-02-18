package info.xiaomo.config;

import java.util.Map;
import info.xiaomo.config.beans.ItemConfig;
import info.xiaomo.gengine.config.ConfigSuffix;
import info.xiaomo.gengine.config.annotation.ConfigFileScan;
import info.xiaomo.gengine.config.annotation.PackageScan;
import info.xiaomo.gengine.utils.YamlUtil;
import org.junit.Test;

/**
 * @author YangQiang
 */
@ConfigFileScan(value = "", suffix = ConfigSuffix.excel)
@PackageScan("info.xiaomo.config.beans")
public class ConfigDataTest {

	@Test
	public void testReadDataTable() throws Exception {
		ConfigDataManager dataManager = ConfigDataManager.getInstance();
		// 初始化配置

		dataManager.init();

		// 获取半条数据，可以筛选多个id
		ItemConfig config = dataManager.getConfig(ItemConfig.class, 11, "cc");
		System.out.println(config);

		// 获取整张表的数据
		dataManager.getConfigs(ItemConfig.class).forEach(System.out::println);
	}


	@Test
	public void testYaml(){
		Map<String, Object> read = YamlUtil.read("/Users/ctwdevops/Workspace/game/GEngine/src/test/resources/test.yml");
		System.out.println(read.get("a"));

	}
}
