/**
 * 创建日期:  2017年08月12日 15:50
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.beans;

import java.util.Map;

/**
 * 表描述
 *
 * @author YangQiang
 */
public class TableDesc {
    private Class clz;
    private String name;
    private String[] primaryKeys;
    private Map<String, ColumnDesc> columns;

    public TableDesc(Class clz) {
        this.clz = clz;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(String[] primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public Map<String, ColumnDesc> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnDesc> columns) {
        this.columns = columns;
    }
}
