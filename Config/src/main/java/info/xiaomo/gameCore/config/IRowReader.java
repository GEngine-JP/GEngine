/**
 * 创建日期:  2017年08月12日 9:46
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

import java.util.function.Supplier;

/**
 * 行读取器
 *
 * @author YangQiang
 */
public interface IRowReader<T, R> extends Reader<T, R> {
    /**
     * 获取默认的单元格读取器
     * <p>读取单元格时如果没有指定的位置的单元格读取器 则使用默认的单元格读取器</p>
     *
     * @param <C>
     * @param <V>
     * @return
     */
    <C, V> ICellReader<? super C, ? extends V> getDefaultCellReader();

    /**
     * 获取指定位置的单元格读取器 如果没有则使用默认的读取器
     *
     * @param index
     * @param <C>
     * @param <V>
     * @return
     */
    <C, V> ICellReader<? super C, ? extends V> getCellReader(int index);

    /**
     * 获取行结果装载容器
     *
     * @return
     */
    Supplier<? extends R> getResultSupplier();

    /**
     * 获取单元格值解析器
     *
     * @return
     */
    BiConverter<? super R, Object, ? extends R> getCellParser();
}
