/*
  创建日期:  2017年08月12日 9:42
  创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.config;

import java.util.function.BiFunction;

/**
 * 双数据转换器
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface BiConverter<T, U, R> extends BiFunction<T, U, R> {
    R convert(T t, U u);

    @Override
    default R apply(T t, U u) {
        return convert(t, u);
    }
}
