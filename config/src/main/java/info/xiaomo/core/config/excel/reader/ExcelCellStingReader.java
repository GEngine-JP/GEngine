/**
 * 创建日期:  2017年08月12日 10:24
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.config.excel.reader;

import info.xiaomo.core.config.ICellReader;
import info.xiaomo.core.config.IConverter;
import info.xiaomo.core.config.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 字符串单元格读取器
 *
 * @author YangQiang
 */
public class ExcelCellStingReader implements ICellReader<Cell, String> {

    @Override
    public String getDefaultValue(Cell cell) {
        return null;
    }

    @Override
    public IConverter<Cell, String> getConverter() {
        return ExcelUtils::getCellStringValue;
    }
}
