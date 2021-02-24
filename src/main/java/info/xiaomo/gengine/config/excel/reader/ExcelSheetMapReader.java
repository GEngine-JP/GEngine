package info.xiaomo.gengine.config.excel.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import info.xiaomo.gengine.config.BiConverter;
import info.xiaomo.gengine.config.IRowReader;

/**
 * excel页map读取器 将行数据读取到map中 key是位置 value是行数据
 *
 * @author YangQiang
 */
public class ExcelSheetMapReader extends ExcelSheetReader<Map> {

    public ExcelSheetMapReader(Supplier<? extends Map> resultSupplier) {
        this(resultSupplier, null);
    }

    @SuppressWarnings("unchecked")
    public ExcelSheetMapReader(
            Supplier<? extends Map> resultSupplier, Map<Integer, IRowReader> rowReaders) {
        this(
                resultSupplier,
                new ExcelRowMapReader(HashMap::new),
                (map, objs) -> {
                    Object[] params = (Object[]) objs;
                    int index = (int) params[1];
                    Object value = params[2];
                    map.put(index, value);
                    return map;
                },
                rowReaders);
    }

    public ExcelSheetMapReader(
            Supplier<? extends Map> resultSupplier,
            IRowReader defaultRowReader,
            BiConverter<? super Map, Object, ? extends Map> rowParser,
            Map<Integer, IRowReader> rowReaders) {
        super(resultSupplier, defaultRowReader, rowParser, rowReaders);
    }
}
