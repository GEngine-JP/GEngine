/**
 * 创建日期:  2017年08月22日 10:19
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel;

import info.xiaomo.gameCore.config.ConfigContainer;
import info.xiaomo.gameCore.config.IConfigWrapper;
import info.xiaomo.gameCore.config.beans.ColumnDesc;
import info.xiaomo.gameCore.config.beans.TableDesc;
import info.xiaomo.gameCore.config.util.BeanUtils;
import info.xiaomo.gameCore.config.util.ExcelUtils;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * excel配置包装器
 *
 * @author YangQiang
 */
@Data
public class ExcelConfigWrapper implements IConfigWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelConfigWrapper.class);

    private ConfigContainer container;
    private String excelFilePath;
    private String excelFileSuffix = ".xlsx";
    private String keyDelimiter = "_";
    private TableDesc tableDesc;

    public ExcelConfigWrapper(String excelFilePath, TableDesc tableDesc) {
        this.excelFilePath = excelFilePath;
        this.tableDesc = tableDesc;
        this.container = new ConfigContainer<>();
    }

    @Override
    public <T> T getConfig(Object key) {
        return (T) container.getConfig(key);
    }

    @Override
    public <T> List<T> getList() {
        return container.getList();
    }


    @Override
    public void build() {
        String filePath = excelFilePath + File.separatorChar + tableDesc.getName() + excelFileSuffix;
        Workbook workbook = ExcelUtils.getWorkbook(filePath);
        if (workbook == null) {
            throw new RuntimeException(String.format("【%s】打开excel文件失败", filePath));
        }
        Class clz = tableDesc.getClz();
        Map<String, ColumnDesc> columns = tableDesc.getColumns();

        String[] primaryKeys = tableDesc.getPrimaryKeys();
        if (primaryKeys == null || primaryKeys.length == 0) {
            throw new RuntimeException(String.format("【%s】没有主键信息", clz.getName()));
        }
        for (String primaryKey : primaryKeys) {
            if (primaryKey == null) {
                throw new RuntimeException(String.format("【%s】主键错误", clz.getName()));
            }
            try {
                clz.getDeclaredField(primaryKey);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(String.format("【%s】无效的主键【%s】", clz.getName(), primaryKey));
            }
        }

        Map<String, String> warnKey = new HashMap<>();
        Map<String, String> errorKey = new HashMap<>();

        List<Map<Integer, String>> table = ExcelUtils.readExcelSheet(workbook.getSheetAt(0));
        Map<Integer, String> header = table.remove(0);
        Map<String, Object> collect = table.stream().map(rowData -> {
            try {
                Object config = clz.newInstance();
                header.forEach((columnIndex, columnName) -> {
                    String cellData = rowData.get(columnIndex);
                    ColumnDesc columnDesc = columns.get(columnName);
                    if (columnDesc == null) {
                        warnKey.put(columnName, String.format("[%s]中没有找到[%s]属性", clz.getName(), columnName));
                        return;
                    }

                    if (columnDesc.isNotNull() && cellData == null) {
                        errorKey.put(columnName, String.format("[%s]中[%s]列不允许为null", tableDesc.getName(), columnName));
                        return;
                    }
                    Object newValue = cellData;
                    Field field = columnDesc.getField();

                    try {
                        if (columnDesc.getConverter() != null) {
                            newValue = columnDesc.getConverter().convert(newValue);
                        }
                    } catch (Exception e) {
                        errorKey.put(columnName, String.format("[%s]中[%s]的转换器错误", clz.getName(), field.getName()));
                        return;
                    }
                    try {
                        field.setAccessible(true);
                        Object oldValue = field.get(config);
                        if (newValue != null) {
                            BeanUtils.setProperty(config, field.getName(), newValue);
                            newValue = field.get(config);
                        }
                        // 如果最后解析成null而默认值不是null则还原回去
                        if (newValue == null && oldValue != null) {
                            field.set(config, oldValue);
                        }
                    } catch (Exception e) {
                        errorKey.put(columnName, String.format("[%s]中[%s]设值失败[%s]", clz.getName(), field.getName(), cellData));
                        e.printStackTrace();
                    }
                });
                return config;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toMap(config -> {
            StringBuilder sb = new StringBuilder();
            for (String primaryKey : primaryKeys) {
                Field field = null;
                try {
                    field = clz.getDeclaredField(primaryKey);
                } catch (NoSuchFieldException e) {
                    errorKey.put(primaryKey, String.format("[%s]中无效的主键[%s]", clz.getName(), primaryKey));
                }
                field.setAccessible(true);
                try {
                    Object value = field.get(config);
                    sb.append(value).append(getKeyDelimiter());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return sb.substring(0, sb.length() - 1);
        }, Function.identity()));

        if (!warnKey.isEmpty()) {
            warnKey.forEach((key, warn) -> LOGGER.warn(warn));
        }

        if (!errorKey.isEmpty()) {
            errorKey.forEach((key, error) -> LOGGER.error(error));
            throw new RuntimeException(String.format("[%s]解析失败", tableDesc.getName()));
        }

        collect.forEach((key, config) -> {
            container.getConfigMap().put(key, config);
            container.getConfigList().add(config);
        });
    }
}
