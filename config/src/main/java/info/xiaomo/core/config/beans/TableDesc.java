/**
 * 创建日期:  2017年08月12日 15:50
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.config.beans;

import lombok.Data;

import java.util.Map;

/**
 * 表描述
 *
 * @author YangQiang
 */
@Data
public class TableDesc {
    private Class clz;

    private String name;

    private int index;

    private int header;

    private int[] ignoreRow;

    private String[] primaryKeys;

    private Map<String, ColumnDesc> columns;

    public TableDesc(Class clz) {
        this.clz = clz;
    }
}
