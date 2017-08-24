/**
 * 创建日期:  2017年08月24日 16:38
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package com.yangqiang;

import info.xiaomo.gameCore.config.excel.ExcelConfigDataManager;

/**
 * @author YangQiang
 */
public class ConfigDataManager extends ExcelConfigDataManager {
    private static final ConfigDataManager INSTANCE = new ConfigDataManager();

    private ConfigDataManager() {
        super(Test.class);
    }

    public static ConfigDataManager getInstance() {
        return INSTANCE;
    }
}
