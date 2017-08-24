package com.yangqiang.beans;

import com.yangqiang.converters.IntegerArrayConverter;
import com.yangqiang.converters.IntegerMapConverter;
import com.yangqiang.converters.Matrix3IntConverter;
import info.xiaomo.gameCore.config.IConfig;
import info.xiaomo.gameCore.config.annotation.Column;
import info.xiaomo.gameCore.config.annotation.Config;
import info.xiaomo.gameCore.config.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@ToString
@Data
@Config
@Table(name = "cfg_item", primaryKey = {"id", "secondId"})
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