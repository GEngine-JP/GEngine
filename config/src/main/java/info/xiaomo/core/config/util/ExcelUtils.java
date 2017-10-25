/**
 * 创建日期:  2017年08月12日 15:12
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.config.util;

import info.xiaomo.core.config.excel.reader.ExcelSheetListReader;
import info.xiaomo.core.config.excel.reader.ExcelSheetMapReader;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具集
 *
 * @author YangQiang
 */
public class ExcelUtils {
    /**
     * 获取指定文件的工作薄
     *
     * @param fileName
     * @return
     */
    public static Workbook getWorkbook(String fileName) {
        Objects.requireNonNull(fileName, "Excel fileName must not be null");
        try {
            return getWorkbook(new FileInputStream(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定输入流的工作薄
     *
     * @param inputStream
     * @return
     */
    public static Workbook getWorkbook(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "Excel inputStream must not be null");
        try {
            return WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return 单元格的值，否则null
     */
    public static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String retValue = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    }
                } else {
                    double value = cell.getNumericCellValue();
                    //为整数的小数去掉后面的0
                    if (value % 1 == 0) {
                        return String.valueOf(Double.valueOf(value).longValue());
                    }
                    return String.valueOf(value);
                }
                break;
            case Cell.CELL_TYPE_STRING:
                retValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_FORMULA:
                try {
                    // 先按照字符串格式获取 如果是生成的是数字 会抛出异常
                    retValue = cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        // 按照数字类型获取结果
                        double value = cell.getNumericCellValue();
                        //为整数的小数去掉后面的0
                        if (value % 1 == 0) {
                            return String.valueOf(Double.valueOf(value).longValue());
                        }
                        return String.valueOf(value);
                    } catch (Exception e1) {
                        // 直接读取原公式
                        retValue = cell.getCellFormula();
                    }
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                retValue = null;
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                retValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                retValue = null;
                break;
            default:
                retValue = null;
                break;
        }
        return retValue;
    }

    /**
     * 读取excel页 结果以List封装每行数据以Map封装(key是列数,value是字符串值)
     *
     * @param sheet
     * @return
     */
    public static List<Map<Integer, String>> readExcelSheetToList(Sheet sheet) {
        List<Map<Integer, String>> sheetResult = new ArrayList<>();
        new ExcelSheetListReader(() -> sheetResult).read(sheet);
        return sheetResult;
    }

    /**
     * 读取excel页 结果以Map封装, key是行号, 每行数据以Map封装(key是列数,value是字符串值)
     * @param sheet
     * @return
     */
    public static Map<Integer,Map<Integer, String>> readExcelSheetToMap(Sheet sheet) {
        Map<Integer, Map<Integer, String>> sheetResult = new HashMap<>(10);
        new ExcelSheetMapReader(() -> sheetResult).read(sheet);
        return sheetResult;
    }
}