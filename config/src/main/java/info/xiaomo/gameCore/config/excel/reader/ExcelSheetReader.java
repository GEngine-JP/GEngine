/**
 * 创建日期:  2017年08月12日 10:53
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel.reader;

import info.xiaomo.gameCore.config.BiConverter;
import info.xiaomo.gameCore.config.IRowReader;
import info.xiaomo.gameCore.config.ITableReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author YangQiang
 */
public class ExcelSheetReader<R> implements ITableReader<Sheet, R> {
    protected Supplier<? extends R> resultSupplier;
    protected IRowReader defaultRowReader;
    protected BiConverter<? super R, Object, ? extends R> rowParser;
    protected Map<Integer, IRowReader> rowReaders = new HashMap<>();

    public ExcelSheetReader(Supplier<? extends R> resultSupplier, IRowReader defaultRowReader, BiConverter<? super R, Object, ? extends R> rowParser, Map<Integer, IRowReader> rowReaders) {
        this.resultSupplier = resultSupplier;
        this.defaultRowReader = defaultRowReader;
        this.rowParser = rowParser;
        this.rowReaders = rowReaders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <L, V> IRowReader<? super L, ? extends V> getDefaultRowReader() {
        return defaultRowReader;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <L, V> IRowReader<? super L, ? extends V> getRowReader(int index) {
        if (rowReaders == null) {
            return defaultRowReader;
        }
        return rowReaders.getOrDefault(index, defaultRowReader);
    }

    @Override
    public Supplier<? extends R> getResultSupplier() {
        return resultSupplier;
    }

    @Override
    public BiConverter<? super R, Object, ? extends R> getRowParser() {
        return rowParser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        R result = getResultSupplier().get();
        if (result == null) {
            return null;
        }
        BiConverter<? super R, Object, ? extends R> rowParser = getRowParser();
        if (rowParser == null) {
            return result;
        }
        for (int rowIndex = sheet.getFirstRowNum(), lastRowIndex = sheet.getLastRowNum(); rowIndex <= lastRowIndex; rowIndex++) {
            Object rowValue = null;
            Row row = sheet.getRow(rowIndex);
            IRowReader rowReader = getRowReader(rowIndex);
            if (rowReader != null) {
                rowValue = rowReader.read(row);
            }
            result = rowParser.apply(result, new Object[]{row, rowIndex, rowValue});

        }
        return result;
    }
}
