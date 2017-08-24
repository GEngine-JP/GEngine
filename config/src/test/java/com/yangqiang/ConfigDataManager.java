/**
 * 创建日期:  2017年08月24日 16:38
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package com.yangqiang;

import info.xiaomo.gameCore.config.FileDataManagerConfig;
import info.xiaomo.gameCore.config.FileConfigDataManager;
import info.xiaomo.gameCore.config.IConfigDataManager;
import info.xiaomo.gameCore.config.excel.ExcelConfigDataManager;

import java.util.List;

/**
 * @author YangQiang
 */
public class ConfigDataManager implements IConfigDataManager {
    private static final ConfigDataManager INSTANCE = new ConfigDataManager();
    private FileConfigDataManager fileConfigDataManager = new ExcelConfigDataManager(new DataManagerConfig());

    private ConfigDataManager() {
    }

    public static ConfigDataManager getInstance() {
        return INSTANCE;
    }

    @Override
    public <T> T getConfig(Class<T> clz, Object... primaryKey) {
        return fileConfigDataManager.getConfig(clz, primaryKey);
    }

    @Override
    public <T> List<T> getConfigs(Class<T> clz) {
        return fileConfigDataManager.getConfigs(clz);
    }

    @Override
    public <T> T getConfigCache(Class<T> clz) {
        return fileConfigDataManager.getConfigCache(clz);
    }

    @Override
    public void init() throws Exception {
        fileConfigDataManager.init();
    }
}
