package info.xiaomo.gengine.config; /**
 * 创建日期:  2017年08月11日 19:04
 * 创建作者:  杨 强  <281455776@qq.com>
 */

/**
 * 读取接口
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface IReader<T, R> extends IConverter<T, R> {
    /**
     * read
     *
     * @param t
     * @return
     */
    R read(T t);

    /**
     * convert
     * @param t t
     * @return
     */
    @Override
    default R convert(T t) {
        return read(t);
    }
}
