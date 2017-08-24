/**
 * 创建日期:  2017年08月24日 16:38
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package com.yangqiang;

import info.xiaomo.gameCore.config.annotation.ConfigFileScan;
import info.xiaomo.gameCore.config.annotation.PackageScan;
import info.xiaomo.gameCore.config.excel.ExcelConfigDataManager;

/**
 * @author YangQiang
 */
@ConfigFileScan(value = "E:\\ChessGame\\GameCore\\config\\src\\test\\resources", suffix = ".xlsx")
@PackageScan("com.yangqiang.beans")
public class ConfigDataManager extends ExcelConfigDataManager {
    private static final ConfigDataManager INSTANCE = new ConfigDataManager();

    private ConfigDataManager() {
    }

    public static ConfigDataManager getInstance() {
        return INSTANCE;
    }
}
