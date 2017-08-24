/**
 * 创建日期:  2017年08月21日 18:08
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel;

import info.xiaomo.gameCore.config.AbstractConfigDataManager;
import info.xiaomo.gameCore.config.IConfig;
import info.xiaomo.gameCore.config.IConfigCache;
import info.xiaomo.gameCore.config.IConfigWrapper;
import info.xiaomo.gameCore.config.annotation.Cache;
import info.xiaomo.gameCore.config.annotation.Config;
import info.xiaomo.gameCore.config.beans.TableDesc;
import info.xiaomo.gameCore.config.parser.DataConfigXmlParser;
import info.xiaomo.gameCore.config.util.ClassFileUtils;
import info.xiaomo.gameCore.config.util.ReflectUtils;
import lombok.Data;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author YangQiang
 */
@Data
public class ExcelConfigDataManager extends AbstractConfigDataManager {
    public static final String DEFAULT_XML_CONFIG_FILE = "data_config.xml";
    /** xml文件配置路径 */
    private String xmlConfigFile;
    /** 配置类包名 */
    private String configPackageName;
    /** excel文件路径 */
    private String excelFileDir;
    /** excel文件后缀 */
    private String excelFileSuffix = ".xlsx";

    @Override
    public void init() throws Exception {
        Objects.requireNonNull(excelFileDir, "excelFileDir!");
        File excelFile = new File(excelFileDir);
        if (excelFile == null || !excelFile.isDirectory()) {
            LOGGER.error("excel文件路径错误[%s]", excelFileDir);
            throw new RuntimeException(String.format("excel文件路径错误[%s]", excelFileDir));
        }
        Map<String, Class> cacheClz = new HashMap<>();
        Map<String, TableDesc> configTable = new HashMap<>();
        if (configPackageName != null) {
            // 获取所有的配置类和缓存类
            Set<Class> clzSet = ClassFileUtils.getClasses(configPackageName, clz -> clz.isAnnotationPresent(Config.class) || clz.isAnnotationPresent(Cache.class));
            for (Iterator<Class> clzIt = clzSet.iterator(); clzIt.hasNext(); ) {
                Class clz = clzIt.next();
                if (clz.isAnnotationPresent(Config.class)) {
                    TableDesc tableDesc = ReflectUtils.getTableDesc(clz);
                    configTable.put(clz.getName(), tableDesc);
                } else if (clz.isAnnotationPresent(Cache.class)) {
                    cacheClz.put(clz.getName(), clz);
                }
            }
        }
        if (xmlConfigFile == null) {
            URL xmlConfigResource = ExcelConfigDataManager.class.getClassLoader().getResource(DEFAULT_XML_CONFIG_FILE);
            if (xmlConfigResource != null) {
                xmlConfigFile = xmlConfigResource.getFile();
            }
        }
        if (xmlConfigFile != null) {
            Map<String, TableDesc> xmlConfigs = DataConfigXmlParser.parseConfigs(xmlConfigFile);
            configTable.putAll(xmlConfigs);

            Map<String, Class> xmlCaches = DataConfigXmlParser.parseCaches(xmlConfigFile);
            cacheClz.putAll(xmlCaches);
        }


        Map<String, IConfigWrapper> tempConfigs = new HashMap<>();
        configTable.forEach((clzName, tableDesc) -> {
            IConfigWrapper wrapper = new ExcelConfigWrapper(excelFileDir + File.separatorChar + tableDesc.getName() + excelFileSuffix, tableDesc);
            wrapper.build();
            wrapper.getList().forEach(e -> {
                if (IConfig.class.isAssignableFrom(e.getClass())) {
                    IConfig config = (IConfig) e;
                    config.afterLoad();
                }
            });
            tempConfigs.put(clzName, wrapper);
        });

        Map<String, Object> tempCaches = new HashMap<>();
        cacheClz.forEach((clzName, clz) -> {
            try {
                Object obj = clz.newInstance();
                if (IConfigCache.class.isAssignableFrom(clz)) {
                    IConfigCache cache = (IConfigCache) obj;
                    cache.build();
                }
                tempCaches.put(clzName, obj);
            } catch (Exception e) {
                throw new RuntimeException(String.format("创建对象【%s】错误", clz.getName()), e);
            }
        });

        configs = tempConfigs;
        caches = tempCaches;
    }
}
