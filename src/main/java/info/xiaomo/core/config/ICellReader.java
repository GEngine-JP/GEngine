package info.xiaomo.core.config; /**
 * 创建日期:  2017年08月11日 19:06
 * 创建作者:  杨 强  <281455776@qq.com>
 */

/**
 * 单元格读取器
 *
 * @author YangQiang
 */
public interface ICellReader<T, R> extends IReader<T, R> {

    /**
     * 获取默认值
     *
     * @param t t
     * @return R
     */
    R getDefaultValue(T t);

    /**
     * 获取转换器
     * @return IConverter
     */
    IConverter<T, R> getConverter();

    /**
     * 读取
     * @param t t
     * @return R
     */
    @Override
    default R read(T t) {
        IConverter<T, R> converter = getConverter();
        if (converter != null) {
            return converter.convert(t);
        }
        return getDefaultValue(t);
    }
}
