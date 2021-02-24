package info.xiaomo.gengine.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import info.xiaomo.gengine.logger.desc.ColumnDesc;

class TableCompareUtil {

    /** 属性变更规则 */
    private static final Map<String, List<String>> FIELD_CHANGE_RULES = new HashMap<>(10);

    static {
        List<String> bigintList = new ArrayList<>();
        bigintList.add("varchar");
        bigintList.add("longtext");
        bigintList.add("text");
        bigintList.add("bigint");
        FIELD_CHANGE_RULES.put("bigint", bigintList);

        List<String> bitList = new ArrayList<>();
        bitList.add("longtext");
        bitList.add("varchar");
        bitList.add("text");
        bitList.add("bigint");
        bitList.add("integer");
        bitList.add("int");
        bitList.add("int unsigend");
        bitList.add("bit");
        FIELD_CHANGE_RULES.put("bit", bitList);

        List<String> intList = new ArrayList<>();
        intList.add("longtext");
        intList.add("varchar");
        intList.add("text");
        intList.add("bigint");
        intList.add("integer");
        intList.add("int");
        intList.add("int unsigned");
        FIELD_CHANGE_RULES.put("int", intList);

        FIELD_CHANGE_RULES.put("integer", intList);

        List<String> shortlist = new ArrayList<>();
        shortlist.add("longtext");
        shortlist.add("varchar");
        shortlist.add("text");
        shortlist.add("bigint");
        shortlist.add("int");
        shortlist.add("integer");
        shortlist.add("short");
        FIELD_CHANGE_RULES.put("short", shortlist);

        List<String> bytelist = new ArrayList<>();
        bytelist.add("longtext");
        bytelist.add("varchar");
        bytelist.add("text");
        bytelist.add("bigint");
        bytelist.add("int");
        bytelist.add("short");
        bytelist.add("integer");
        FIELD_CHANGE_RULES.put("byte", bytelist);

        List<String> varcharList = new ArrayList<>();
        varcharList.add("longtext");
        varcharList.add("varchar");
        varcharList.add("text");
        varcharList.add("int");
        varcharList.add("bigint");
        FIELD_CHANGE_RULES.put("varchar", varcharList);

        List<String> text = new ArrayList<>();
        text.add("longtext");
        text.add("text");
        text.add("varchar");
        FIELD_CHANGE_RULES.put("text", text);

        List<String> longtextList = new ArrayList<>();
        longtextList.add("longtext");
        FIELD_CHANGE_RULES.put("longtext", longtextList);

        List<String> blobList = new ArrayList<>();
        blobList.add("blob");
        FIELD_CHANGE_RULES.put("blob", blobList);
    }

    public static List<String> compare(
            String tableName, List<ColumnDesc> newColumns, List<ColumnDesc> oldColumns)
            throws Exception {
        HashMap<String, ColumnDesc> oldColumnMap = new HashMap<>(10);
        List<String> ret = new ArrayList<>();
        for (ColumnDesc columnInfo : oldColumns) {
            oldColumnMap.put(columnInfo.getName(), columnInfo);
        }
        for (ColumnDesc newCol : newColumns) {
            ColumnDesc oldCol = oldColumnMap.get(newCol.getName());
            // 原表中 没有这一列,新增
            if (oldCol == null) {
                ret.add("ALTER TABLE `" + tableName + "` ADD COLUMN " + newCol.toDDL() + ";");
                // 修改
            } else if (ableChange(newCol, oldCol)) {
                String sql = getChangeSql(newCol, oldCol);
                if (!"".equals(sql)) {
                    ret.add("ALTER TABLE `" + tableName + "` MODIFY COLUMN " + sql + ";");
                }
            } else {
                throw new Exception(
                        tableName + " " + newCol.toString() + " to " + oldCol + "列类型不匹配  无法自动变更");
            }
        }
        return ret;
    }

    public static String getChangeSql(ColumnDesc newColumn, ColumnDesc oldColumn) {

        // 长度只能往大修改，如果变小，不修改
        if (newColumn.getType().equals(oldColumn.getType())
                && !sizeChanged(newColumn, oldColumn)
                && newColumn.isAllowNull() == oldColumn.isAllowNull()) {
            return "";
        }
        return newColumn.toDDL();
    }

    private static boolean sizeChanged(ColumnDesc newColumn, ColumnDesc oldColumn) {
        if (isNumber(newColumn) && isNumber(oldColumn)) {
            return false;
        }
        return newColumn.getSize() > oldColumn.getSize();
    }

    private static boolean isNumber(ColumnDesc column) {
        String type = column.getType();
        return type.equalsIgnoreCase(FieldType.TINYINT.name())
                || type.equalsIgnoreCase(FieldType.SMALLINT.name())
                || type.equalsIgnoreCase(FieldType.INT.name())
                || type.equalsIgnoreCase(FieldType.BIT.name())
                || type.equalsIgnoreCase(FieldType.BIGINT.name());
    }

    /**
     * 是否可以变更
     *
     * @param newCol
     * @param oldCol
     * @return
     */
    private static boolean ableChange(ColumnDesc newCol, ColumnDesc oldCol) {
        List<String> list = FIELD_CHANGE_RULES.get(newCol.getType());
        if (list == null) {
            return false;
        }
        return list.contains(oldCol.getType());
    }
}
