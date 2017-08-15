/**
 * 创建日期:  2017年08月11日 19:06
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

/**
 * 单元格读取器
 *
 * @author YangQiang
 */
public interface ICellReader<T, R> extends Reader<T, R> {
    R getDefaultValue(T t);

    Converter<T, R> getConverter();

    @Override
    default R read(T t) {
        Converter<T, R> converter = getConverter();
        if (converter != null) {
            return converter.convert(t);
        }
        return getDefaultValue(t);
    }
}
