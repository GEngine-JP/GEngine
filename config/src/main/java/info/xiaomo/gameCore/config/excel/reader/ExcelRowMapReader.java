/**
 * 创建日期:  2017年08月12日 11:05
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel.reader;

import info.xiaomo.gameCore.config.BiConverter;
import info.xiaomo.gameCore.config.ICellReader;

import java.util.Map;
import java.util.function.Supplier;

/**
 * excel行map读取器 将单元格数据读取到map中 key是位置 value是字符串
 *
 * @author YangQiang
 */
public class ExcelRowMapReader extends ExcelRowReader<Map> {

    public ExcelRowMapReader(Supplier<? extends Map> resultSupplier) {
        this(resultSupplier, null);
    }

    public ExcelRowMapReader(Supplier<? extends Map> resultSupplier, Map<Integer, ICellReader> cellReaders) {
        this(resultSupplier, new ExcelCellStingReader(), (map, objs) -> {
            Object[] params = (Object[]) objs;
            int index = (int) params[1];
            Object value = params[2];
            map.put(index, value);
            return map;
        }, cellReaders);
    }

    public ExcelRowMapReader(Supplier<? extends Map> resultSupplier, ICellReader defaultCellReader, BiConverter<? super Map, Object, ? extends Map> cellParser, Map<Integer, ICellReader> cellReaders) {
        super(resultSupplier, defaultCellReader, cellParser, cellReaders);
    }
}
