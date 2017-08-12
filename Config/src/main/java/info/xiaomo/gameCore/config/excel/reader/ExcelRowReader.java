/**
 * 创建日期:  2017年08月12日 10:26
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel.reader;

import info.xiaomo.gameCore.config.ICellReader;
import info.xiaomo.gameCore.config.IRowReader;
import info.xiaomo.gameCore.config.BiConverter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * excel行读取器
 *
 * @author YangQiang
 */
public class ExcelRowReader<R> implements IRowReader<Row, R> {
    protected Supplier<? extends R> resultSupplier;
    protected ICellReader defaultCellReader;
    protected BiConverter<? super R, Object, ? extends R> cellParser;
    protected Map<Integer, ICellReader> cellReaders = new HashMap<>();

    public ExcelRowReader(Supplier<? extends R> resultSupplier, ICellReader defaultCellReader, BiConverter<? super R, Object, ? extends R> cellParser, Map<Integer, ICellReader> cellReaders) {
        this.resultSupplier = resultSupplier;
        this.defaultCellReader = defaultCellReader;
        this.cellParser = cellParser;
        this.cellReaders = cellReaders;
    }

    @Override
    public <C, V> ICellReader<? super C, ? extends V> getDefaultCellReader() {
        return defaultCellReader;
    }

    @Override
    public <C, V> ICellReader<? super C, ? extends V> getCellReader(int index) {
        if (cellReaders == null) {
            return defaultCellReader;
        }
        return cellReaders.getOrDefault(index, defaultCellReader);
    }

    @Override
    public Supplier<? extends R> getResultSupplier() {
        return resultSupplier;
    }

    @Override
    public BiConverter<? super R, Object, ? extends R> getCellParser() {
        return cellParser;
    }

    @Override
    public R read(Row row) {
        R result = getResultSupplier().get();
        if (result == null) {
            return result;
        }
        BiConverter<? super R, Object, ? extends R> cellParser = getCellParser();
        if (cellParser == null) {
            return result;
        }
        for (int cellIndex = row.getFirstCellNum(), lastCellIndex = row.getLastCellNum(); cellIndex < lastCellIndex; cellIndex++) {
            Object cellValue = null;
            Cell cell = row.getCell(cellIndex);
            ICellReader cellReader = getCellReader(cellIndex);
            if (cellReader != null) {
                cellValue = cellReader.read(cell);
            }
            result = cellParser.apply(result, new Object[]{cell, cellIndex, cellValue});
        }
        return result;
    }
}
