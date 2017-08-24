/**
 * 创建日期:  2017年08月24日 17:54
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package com.yangqiang;

import info.xiaomo.gameCore.config.FileDataManagerConfig;
import info.xiaomo.gameCore.config.annotation.ConfigFileScan;
import info.xiaomo.gameCore.config.annotation.PackageScan;

/**
 * @author YangQiang
 */
@ConfigFileScan(value = "E:\\ChessGame\\GameCore\\config\\src\\test\\resources", suffix = ".xlsx")
@PackageScan("com.yangqiang.beans")
public class DataManagerConfig extends FileDataManagerConfig {
}
