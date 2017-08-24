/**
 * 创建日期:  2017年08月12日 15:51
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.beans;


import info.xiaomo.gameCore.config.IConverter;

import java.lang.reflect.Field;

/**
 * 字段描述
 *
 * @author YangQiang
 */
public class ColumnDesc {
    /** 列名 */
    private String name;
    /** 是否允许不为null */
    private boolean notNull;
    /** 对应的字段属性 */
    private Field field;
    /** 字段的转换器 */
    private IConverter converter;

    public ColumnDesc() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public IConverter getConverter() {
        return converter;
    }

    public void setConverter(IConverter converter) {
        this.converter = converter;
    }
}
