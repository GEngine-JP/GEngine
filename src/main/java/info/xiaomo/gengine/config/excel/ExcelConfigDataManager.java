/**
 * 创建日期:  2017年08月21日 18:08
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gengine.config.excel;

import java.io.File;
import info.xiaomo.gengine.config.AbstractFileConfigDataManager;
import info.xiaomo.gengine.config.FileConfigDataManagerConfig;
import info.xiaomo.gengine.config.IConfigWrapper;
import info.xiaomo.gengine.config.beans.TableDesc;

/**
 * excel配置管理器
 *
 * @author YangQiang
 */
public class ExcelConfigDataManager extends AbstractFileConfigDataManager {

    public ExcelConfigDataManager() {
    }

    public ExcelConfigDataManager(Class<?> configClz) {
        super(configClz);
    }

    public ExcelConfigDataManager(FileConfigDataManagerConfig config) {
        super(config);
    }

    @Override
    public IConfigWrapper parseTableDesc(TableDesc tableDesc) {
        String configFile = getConfigFileDir() + File.separatorChar + tableDesc.getName() + getConfigFileSuffix();
        return new ExcelConfigWrapper(configFile, tableDesc).build();
    }
}
