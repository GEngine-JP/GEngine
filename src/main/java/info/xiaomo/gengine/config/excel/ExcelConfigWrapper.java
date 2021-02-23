package info.xiaomo.gengine.config.excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import info.xiaomo.gengine.config.ConfigContainer;
import info.xiaomo.gengine.config.IConfigWrapper;
import info.xiaomo.gengine.config.beans.ColumnDesc;
import info.xiaomo.gengine.config.beans.TableDesc;
import info.xiaomo.gengine.config.util.BeanUtils;
import info.xiaomo.gengine.config.util.ExcelUtils;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * excel配置包装器
 *
 * @author YangQiang
 */
@Data
public class ExcelConfigWrapper implements IConfigWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelConfigWrapper.class);

    private ConfigContainer container;
    private String excelFile;
    private TableDesc tableDesc;

    public ExcelConfigWrapper(String excelFile, TableDesc tableDesc) {
        this.excelFile = excelFile;
        this.tableDesc = tableDesc;
        this.container = new ConfigContainer<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getConfig(Object key) {
        return (T) container.getConfig(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList() {
        return container.getList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public ExcelConfigWrapper build() {
        LOGGER.info("加载配置表【{}】...", tableDesc.getName());
        Workbook workbook = ExcelUtils.getWorkbook(excelFile);
        if (workbook == null) {
            throw new RuntimeException(String.format("【%s】打开excel文件失败", excelFile));
        }
        Class clz = tableDesc.getClz();
        Map<String, ColumnDesc> columns = tableDesc.getColumns();

        String[] primaryKeys = tableDesc.getPrimaryKeys();
        if (primaryKeys != null && primaryKeys.length > 0) {
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
        } else {
            LOGGER.warn(String.format("[%s]中没有找到主键", clz.getName()));
        }

        if (tableDesc.getIndex() < 0) {
            throw new RuntimeException(String.format("【%s】无效的index索引【%d】", clz.getName(), tableDesc.getIndex()));
        }
        if (tableDesc.getHeader() < 0) {
            throw new RuntimeException(String.format("【%s】无效的header索引【%d】", clz.getName(), tableDesc.getHeader()));
        }
        if (tableDesc.getIgnoreRow() != null) {
            for (int row : tableDesc.getIgnoreRow()) {
                if (row < 0) {
                    throw new RuntimeException(String.format("【%s】无效的忽略行索引【%d】", clz.getName(), row));
                }
            }
        }

        Map<String, String> warnKey = new HashMap<>(10);
        Map<String, String> errorKey = new HashMap<>(10);

        Map<Integer, Map<Integer, String>> table = ExcelUtils.readExcelSheetToMap(workbook.getSheetAt(tableDesc.getIndex()));
        if (tableDesc.getIgnoreRow() != null) {
            for (int ignoreRow : tableDesc.getIgnoreRow()) {
                table.remove(ignoreRow);
            }
        }
        Map<Integer, String> header = table.remove(tableDesc.getHeader());
        table.forEach((rowIndex, rowData) -> {
            try {
                Object config = clz.getConstructor().newInstance();
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
                        BeanUtils.setProperty(config, field.getName(), newValue);
                        newValue = field.get(config);
                        // 如果最后解析成null而默认值不是null则还原回去
                        if (newValue == null && oldValue != null) {
                            field.set(config, oldValue);
                        }
                    } catch (Exception e) {
                        errorKey.put(columnName, String.format("[%s]中[%s]设值失败[%s]", clz.getName(), field.getName(), cellData));
                        e.printStackTrace();
                    }
                });
                if (primaryKeys != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < primaryKeys.length; i++) {
                        String primaryKey = primaryKeys[i];
                        Field field = null;
                        try {
                            field = clz.getDeclaredField(primaryKey);
                        } catch (NoSuchFieldException e) {
                            errorKey.put(primaryKey, String.format("[%s]中无效的主键[%s]", clz.getName(), primaryKey));
                        }
                        Objects.requireNonNull(field).setAccessible(true);
                        try {
                            Object value = field.get(config);
                            if (i == 0) {
                                sb.append(value);
                            } else {
                                sb.append(getKeyDelimiter()).append(value);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    container.getConfigMap().put(sb.toString(), config);
                }
                container.getConfigList().add(config);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        if (!warnKey.isEmpty()) {
            warnKey.forEach((key, warn) -> LOGGER.warn(warn));
        }

        if (!errorKey.isEmpty()) {
            errorKey.forEach((key, error) -> LOGGER.error(error));
            throw new RuntimeException(String.format("[%s]解析失败", tableDesc.getName()));
        }
        return this;
    }
}
