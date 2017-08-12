/**
 * 创建日期:  2017年08月11日 19:18
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

import java.util.function.Function;

/**
 * 数据转换器
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface Converter<T, R> extends Function<T, R> {
    R convert(T t);

    @Override
    default R apply(T t) {
        return convert(t);
    }
}
