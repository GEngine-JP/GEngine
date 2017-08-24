/**
 * 创建日期:  2017年08月24日 17:07
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

import info.xiaomo.gameCore.config.annotation.ConfigFileScan;
import info.xiaomo.gameCore.config.annotation.PackageScan;
import info.xiaomo.gameCore.config.annotation.XmlFileScan;
import lombok.Data;

/**
 * 文件配置管理器的配置
 *
 * @author YangQiang
 */
@Data
public class FileConfigDataManagerConfig {
    /** xml文件配置路径 */
    protected String xmlConfigFile;
    /** 配置类包名 */
    protected String configPackage;
    /** 文件路径 */
    protected String configFileDir;
    /** 文件后缀 */
    protected String configFileSuffix = "";

    public FileConfigDataManagerConfig() {
        PackageScan packageScan = getClass().getAnnotation(PackageScan.class);
        if (packageScan != null) {
            this.configPackage = packageScan.value();
        }
        ConfigFileScan configFileScan = getClass().getAnnotation(ConfigFileScan.class);
        if (configFileScan != null) {
            this.configFileDir = configFileScan.value();
            this.configFileSuffix = configFileScan.suffix();
        }
        XmlFileScan xmlFileScan = getClass().getAnnotation(XmlFileScan.class);
        if (xmlFileScan != null) {
            this.xmlConfigFile = xmlFileScan.value();
        }
    }
}
