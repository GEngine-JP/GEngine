/**
 * 创建日期:  2017年08月24日 17:54
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo;

import info.xiaomo.core.config.FileConfigDataManagerConfig;
import info.xiaomo.core.config.annotation.ConfigFileScan;
import info.xiaomo.core.config.annotation.PackageScan;

/**
 * excel配置管理器的配置 通过注解的形式配置
 * @author YangQiang
 */
@ConfigFileScan(value = "E:\\ChessGame\\GameCore\\config\\src\\test\\resources", suffix = ".xls")
@PackageScan("com.yangqiang.beans")
public class ExcelConfigDataManagerConfig extends FileConfigDataManagerConfig {
    public ExcelConfigDataManagerConfig() {
        super(ExcelConfigDataManagerConfig.class);
    }
}
