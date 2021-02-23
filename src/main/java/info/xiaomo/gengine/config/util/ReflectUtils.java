package info.xiaomo.gengine.config.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.config.IConfig;
import info.xiaomo.gengine.config.IConverter;
import info.xiaomo.gengine.config.annotation.Column;
import info.xiaomo.gengine.config.annotation.Table;
import info.xiaomo.gengine.config.beans.ColumnDesc;
import info.xiaomo.gengine.config.beans.TableDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具
 *
 * @author YangQiang
 */
public class ReflectUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtils.class);

	/**
	 * 获取指定类的表描述信息
	 *
	 * @param clz
	 * @return
	 * @throws Exception
	 */
	public static TableDesc<IConfig> getTableDesc(Class<?> clz) throws Exception {
		TableDesc<IConfig> tableDesc = new TableDesc(clz);

		tableDesc.setName(clz.getSimpleName());

		Table table = clz.getAnnotation(Table.class);
		if (table != null) {
			String tableName = table.name();
			if (!tableName.trim().isEmpty()) {
				tableDesc.setName(tableName);
			}

			String[] primaryKeys = table.primaryKey();
			if (primaryKeys.length > 0) {
				tableDesc.setPrimaryKeys(primaryKeys);
			}

			tableDesc.setHeader(table.header());
			tableDesc.setIgnoreRow(table.ignoreRow());
		}

		Map<String, ColumnDesc> columns = new HashMap<>(10);
		Field[] declaredFields = clz.getDeclaredFields();
		for (Field field : declaredFields) {
			ColumnDesc columnDesc = getColumnDesc(field);
			columns.put(columnDesc.getName(), columnDesc);
		}
		tableDesc.setColumns(columns);
		return tableDesc;
	}


	/**
	 * 获取指定属性的列描述信息
	 *
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public static ColumnDesc getColumnDesc(Field field) throws Exception {
		String fieldName = field.getName();

		ColumnDesc columnDesc = new ColumnDesc();
		columnDesc.setName(fieldName);
		columnDesc.setField(field);

		try {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				String columnName = column.name();
				// 有单独设置列名
				if (!columnName.trim().isEmpty()) {
					columnDesc.setName(columnName);
				}
				// 字段不为null
				columnDesc.setNotNull(column.notNull());

				// 将多个转换器转为一个
				Class<? extends IConverter>[] converters = column.value();
				if (converters.length > 0) {
					IConverter converter = converters[0].getConstructor().newInstance();
					for (int i = 1; i < converters.length; i++) {
						converter = converter.andThen(converters[i].getConstructor().newInstance());
					}
					columnDesc.setConverter(converter);
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("解析【%s】中的【%s】属性错误", field.getDeclaringClass().getName(), fieldName), e);
			throw e;
		}

		return columnDesc;
	}
}
