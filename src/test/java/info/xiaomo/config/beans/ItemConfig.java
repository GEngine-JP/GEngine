package info.xiaomo.config.beans;

import java.util.Map;
import info.xiaomo.config.converters.IntegerArrayConverter;
import info.xiaomo.config.converters.IntegerMapConverter;
import info.xiaomo.config.converters.Matrix3IntConverter;
import info.xiaomo.gengine.config.IConfig;
import info.xiaomo.gengine.config.annotation.Column;
import info.xiaomo.gengine.config.annotation.Config;
import info.xiaomo.gengine.config.annotation.Table;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Config
// @Table(name = "cfg_item", primaryKey = {"id", "secondId"}, index = 0, header = 2, ignoreRow =
// {0,1,3})
@Table(
        name = "cfg_item",
        primaryKey = {"id", "secondId"},
        ignoreRow = {1, 2})
public class ItemConfig implements IConfig {
    @Column(notNull = true)
    private int id;

    @Column(name = "id2")
    private String secondId;

    // 多个转换器共同使用 先转成int数组 然后将int数组转为map
    @Column({IntegerArrayConverter.class, IntegerMapConverter.class})
    private Map<Integer, Integer> map;

    @Column(name = "ints")
    private int[] arrays;

    @Column(Matrix3IntConverter.class)
    private int[][][] intss;
}
