package info.xiaomo.gengine.config; /**
 * 创建日期:  2017年08月11日 19:18
 * 创建作者:  杨 强  <281455776@qq.com>
 */

import java.util.Objects;
import java.util.function.Function;

/**
 * 数据转换器
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface IConverter<T, R> extends Function<T, R> {
    /**
     * 转换
     *
     * @param t t
     * @return R
     */
    R convert(T t);

    /**
     * 应用
     *
     * @param t
     * @return
     */
    @Override
    default R apply(T t) {
        return convert(t);
    }

    /**
     * compose
     *
     * @param before before
     * @param <V>    before
     * @return <V>
     */
    @Override
    default <V> IConverter<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * 在之后
     *
     * @param after after
     * @param <V>   <V>
     * @return <V>
     */
    @Override
    default <V> IConverter<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
}
