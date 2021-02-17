package info.xiaomo.gengine.config;

import info.xiaomo.gengine.config.annotation.ConfigFileScan;
import info.xiaomo.gengine.config.annotation.PackageScan;
import info.xiaomo.gengine.config.annotation.XmlFileScan;
import lombok.Data;

/**
 * 文件配置管理器的配置
 *
 * @author YangQiang
 */
@Data
public class FileConfigDataManagerConfig {
	/**
	 * xml文件配置路径
	 */
	protected String xmlConfigFile;
	/**
	 * 配置类包名
	 */
	protected String configPackage;
	/**
	 * 文件路径
	 */
	protected String configFileDir;
	/**
	 * 文件后缀
	 */
	protected ConfigSuffix configFileSuffix = ConfigSuffix.excel;

	public FileConfigDataManagerConfig() {
	}

	/**
	 * 使用一个外部的带有相关注解的类初始化当前类
	 *
	 * @param configClz
	 */
	public FileConfigDataManagerConfig(Class<?> configClz) {
		if (configClz == null) {
			return;
		}
		PackageScan packageScan = configClz.getAnnotation(PackageScan.class);
		if (packageScan != null) {
			this.configPackage = packageScan.value();
		}
		ConfigFileScan configFileScan = configClz.getAnnotation(ConfigFileScan.class);
		if (configFileScan != null) {
			this.configFileDir = configFileScan.value();
			this.configFileSuffix = configFileScan.suffix();
		}
		XmlFileScan xmlFileScan = configClz.getAnnotation(XmlFileScan.class);
		if (xmlFileScan != null) {
			this.xmlConfigFile = xmlFileScan.value();
		}
	}
}
