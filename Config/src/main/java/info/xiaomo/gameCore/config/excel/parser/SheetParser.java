/**
 * 创建日期:  2017年08月11日 19:44
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel.parser;

import info.xiaomo.gameCore.base.common.StringFormatter;
import info.xiaomo.gameCore.config.Converter;
import info.xiaomo.gameCore.config.excel.BeanUtils;
import info.xiaomo.gameCore.config.excel.ExcelUtils;
import info.xiaomo.gameCore.config.excel.ItemConfig;
import info.xiaomo.gameCore.config.excel.reader.ExcelSheetListReader;
import org.apache.commons.beanutils.IntrospectionContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

/**
 * excel页解析器
 *
 * @author YangQiang
 */
public class SheetParser<T> {
    private Sheet sheet;
    private Class<T> clz;
    private Map<String, Converter> converters = new HashMap<>();

    public SheetParser(Sheet sheet, Class<T> clz) {
        this.sheet = sheet;
        this.clz = clz;
    }

    public void addConverter(String fieldName, Converter converter) {
        Objects.requireNonNull(fieldName, "The fieldName can not be null");
        Objects.requireNonNull(converter, "The cellReader can not be null");
        converters.put(fieldName, converter);
    }

    public void addConverters(Map<String, Converter> converters) {
        Objects.requireNonNull(converters, "The converters can not be null");
        this.converters.putAll(converters);
    }

    private List<Map<Integer, String>> readExcel() {
        List<Map<Integer, String>> sheetResult = new ArrayList<>();
        ExcelSheetListReader reader = new ExcelSheetListReader(() -> sheetResult);
        reader.read(sheet);
        return sheetResult;
    }

    public List<T> parseBeans() throws Exception {
        List<Map<Integer, String>> sheetResult = readExcel();
        if (sheetResult == null) {
            return null;
        }
        if (sheetResult.size() <= 1) {//第一行是表头
            return new ArrayList<>();
        }
        Map<Integer, String> sheetHeader = sheetResult.get(0);
        IntrospectionContext beanDesc = BeanUtils.getBeanDesc(clz);
        List<T> beans = new ArrayList<>();
        for (int i = 1; i < sheetResult.size(); i++) {
            Map<Integer, String> row = sheetResult.get(i);
            if (row == null) {
                continue;
            }
            T bean = clz.newInstance();
            beans.add(bean);
            for (Map.Entry<Integer, String> entry : sheetHeader.entrySet()) {
                Integer index = entry.getKey();
                String name = entry.getValue();
                if (index == null || name == null) {
                    continue;
                }
                Object value = row.get(index);
                PropertyDescriptor descriptor = beanDesc.getPropertyDescriptor(name);
                if (descriptor == null) {
                    continue;
                }
                if (converters.containsKey(name)) {
                    value = converters.get(name).convert(value);
                }
                org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, value);
            }
        }

        return beans;
    }

    public List<Map<String, Object>> parseMaps() throws Exception {
        List<T> beans = parseBeans();
        if (beans == null) {
            return null;
        }
        List<Map<String, Object>> ret = new ArrayList<>();
        for (T bean : beans) {
            Map<String, Object> map = PropertyUtils.describe(bean);
            ret.add(map);
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        InputStream excelInputStream = SheetParser.class.getClassLoader().getResourceAsStream("cfg_items.xlsx");
        Workbook workbook = ExcelUtils.getWorkbook(excelInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Converter<String, int[]> jinghaoArrayConverter = value -> {
            if (value == null) {
                return null;
            }
            ArrayConverter converter = new ArrayConverter(int[].class, new IntegerConverter());
            return converter.convert(int[].class, value);
        };
        SheetParser<ItemConfig> itemConfigResolver = new SheetParser<>(sheet, ItemConfig.class);
        itemConfigResolver.addConverter("bufferparam", jinghaoArrayConverter);
        itemConfigResolver.addConverter("timelimit", jinghaoArrayConverter);
        itemConfigResolver.addConverter("skilladd", value -> {
            int[] array = jinghaoArrayConverter.convert((String) value);
            Map<Integer, Integer> ret = new HashMap<>();
            if (array != null && array.length > 1) {
                ret.put(array[0], array[1]);
            }
            return ret;
        });
        // List<Map<String, Object>> ret = itemConfigResolver.parseMaps();
        // List<ItemConfig> ret = itemConfigResolver.parseBeans();
        List<Map<Integer,String>> ret = itemConfigResolver.readExcel();
        System.out.println(StringFormatter.format("数据总量: 【{}】", ret.size()));
        ret.stream().forEach(System.out::println);
    }
}
