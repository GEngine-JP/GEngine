package info.xiaomo.gengine.config; /**
 * 创建日期:  2017年08月12日 10:18
 * 创建作者:  杨 强  <281455776@qq.com>
 */

import java.util.function.Supplier;

/**
 * 表读取器
 *
 * @author YangQiang
 */
public interface ITableReader<T, R> extends IReader<T, R> {

    /**
     * 获取默认的单元行读取器
     * <p>读取单元行时如果没有指定的位置的单元行读取器 则使用默认的单元行读取器</p>
     *
     * @param <>
     * @param <V>
     * @return
     */
    <L, V> IRowReader<? super L, ? extends V> getDefaultRowReader();

    /**
     * 获取指定位置的行读取器 没有则使用默认的行读取器
     *
     * @param index
     * @param <L>
     * @param <V>
     * @return
     */
    <L, V> IRowReader<? super L, ? extends V> getRowReader(int index);


    /**
     * 获取表结果装载容器
     *
     * @return
     */
    Supplier<? extends R> getResultSupplier();

    /**
     * 获取行值解析器
     *
     * @return
     */
    BiConverter<? super R, Object, ? extends R> getRowParser();
}
