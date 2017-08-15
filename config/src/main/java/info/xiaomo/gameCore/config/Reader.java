/**
 * 创建日期:  2017年08月11日 19:04
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

/**
 * 读取接口
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface Reader<T, R> extends Converter<T, R> {
    R read(T t);

    @Override
    default R convert(T t) {
        return read(t);
    }
}
