/**
 * 创建日期:  2017年08月21日 12:02
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.parser;

import info.xiaomo.gameCore.config.IConverter;
import info.xiaomo.gameCore.config.beans.ColumnDesc;
import info.xiaomo.gameCore.config.beans.TableDesc;
import info.xiaomo.gameCore.config.util.ReflectUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static info.xiaomo.gameCore.config.parser.XmlElement.*;

/**
 * xml数据配置解析
 *
 * @author YangQiang
 */
public class DataConfigXmlParser {
    /**
     * 解析xml文件中的config配置
     *
     * @param xmlFilePath xml文件地址
     * @return config配置信息
     * @throws Exception 配置错误
     */
    @SuppressWarnings("unchecked")
    public static Map<String, TableDesc> parseConfigs(String xmlFilePath) throws Exception {
        Map<String, TableDesc> tableDescs = new HashMap<>();

        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlFilePath));
        Element configDataElement = document.getRootElement();
        if (configDataElement != null) {
            // 获取configData的子节点configs
            Element configsElement = configDataElement.element(CONFIGS_ELEMENT);
            if (configsElement != null) {
                // 获取configs节点的子节点config
                Iterator configElementIt = configsElement.elementIterator(CONFIG_ELEMENT);
                while (configElementIt.hasNext()) {
                    Element configElement = (Element) configElementIt.next();

                    // 获取config节点的class属性
                    String className = configElement.attributeValue(CONFIG_CLASS_ATTRIBUTE);
                    if (className == null || className.trim().isEmpty()) {
                        throw new RuntimeException("config中class属性不能为空");
                    }
                    // 生成对应的类并解析类的TableDesc信息
                    Class clz = Class.forName(className);
                    TableDesc tableDesc = ReflectUtils.getTableDesc(clz);

                    // 获取config节点的name属性作为配置文件名
                    String configName = configElement.attributeValue(CONFIG_NAME_ATTRIBUTE);
                    if (configName != null && !configName.trim().isEmpty()) {
                        tableDesc.setName(configName);
                    }

                    // 获取config节点的primaryKeys属性解析主键信息
                    String primaryKeysStr = configElement.attributeValue(CONFIG_PRIMARY_KEYS_ATTRIBUTE);
                    if (primaryKeysStr != null && !primaryKeysStr.trim().isEmpty()) {
                        String[] primaryKeys = primaryKeysStr.split(",");
                        tableDesc.setPrimaryKeys(primaryKeys);
                    }

                    // 获取config节点的index属性解析sheet页索引
                    String index = configElement.attributeValue(CONFIG_INDEX_ATTRIBUTE);
                    if (index != null && !index.trim().isEmpty()) {
                        try {
                            tableDesc.setIndex(Integer.parseInt(index));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException(String.format("【%s】对应配置的index【{}】错误", clz.getName(), index));
                        }
                    }

                    // 获取config节点的header属性解析sheet页表头
                    String header = configElement.attributeValue(CONFIG_HEADER_ATTRIBUTE);
                    if (header != null && !header.trim().isEmpty()) {
                        try {
                            tableDesc.setHeader(Integer.parseInt(header));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException(String.format("【%s】对应配置的header【{}】错误", clz.getName(), header));
                        }
                    }

                    // 获取config节点的ignoreRow属性解析sheet页忽略行
                    String ignoreRowStr = configElement.attributeValue(CONFIG_IGNORE_ROW_ATTRIBUTE);
                    if (ignoreRowStr != null && !ignoreRowStr.trim().isEmpty()) {
                        String[] ignoreRows = ignoreRowStr.split(",");
                        int[] ignoreRow = new int[ignoreRows.length];
                        try {
                            for (int i = 0; i < ignoreRow.length; i++) {
                                ignoreRow[i] = Integer.parseInt(ignoreRows[i]);
                            }
                        } catch (NumberFormatException e) {
                            throw new RuntimeException(String.format("【%s】对应配置的ignoreRow【{}】错误", clz.getName(), ignoreRowStr));
                        }
                        tableDesc.setIgnoreRow(ignoreRow);
                    }

                    // 获取config节点的子节点field
                    Iterator fieldElementIt = configElement.elementIterator(FIELD_ELEMENT);
                    while (fieldElementIt.hasNext()) {
                        Element fieldElement = (Element) fieldElementIt.next();

                        // 获取field节点的name属性作为类的属性名
                        String fieldName = fieldElement.attributeValue(FIELD_NAME_ATTRIBUTE);
                        if (fieldName == null || fieldName.trim().isEmpty()) {
                            throw new RuntimeException(String.format("【%s】field中name属性不能为空", clz.getName()));
                        }

                        Field field;
                        try {
                            field = clz.getDeclaredField(fieldName);
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(String.format("【%s】的属性字段【%s】不存在", clz.getName(), fieldName));
                        } catch (SecurityException e) {
                            throw e;
                        }
                        ColumnDesc columnDesc = ReflectUtils.getColumnDesc(field);
                        if (columnDesc == null) {
                            throw new RuntimeException(String.format("【%s】的属性字段【%s】不存在", clz.getName(), fieldName));
                        }

                        // 获取field节点的columnName属性作为配置文件的列名
                        String columnName = fieldElement.attributeValue(FIELD_COLUMN_NAME_ATTRIBUTE);
                        if (columnName != null && !columnName.trim().isEmpty()) {
                            columnDesc.setName(columnName);
                        }

                        // 获取field节点的notNull属性验证指定列是否必须有值
                        String notNull = fieldElement.attributeValue(FIELD_NOTNULL_ATTRIBUTE);
                        if (notNull != null && !notNull.trim().isEmpty()) {
                            if ("true".equals(notNull) || "1".equals(notNull)) {
                                columnDesc.setNotNull(true);
                            } else {
                                columnDesc.setNotNull(false);
                            }
                        }

                        // 获取field节点的converters属性作为当前类属性的数据转换器 如果配置有子节点converter则子节点配置覆盖属性配置
                        String convertersStr = fieldElement.attributeValue(FIELD_CONVERTERS_ATTRIBUTE);
                        if (convertersStr != null && !convertersStr.trim().isEmpty()) {
                            String[] converters = convertersStr.split(",");
                            if (converters.length > 0) {
                                Class<? extends IConverter> converterClz = (Class<? extends IConverter>) Class.forName(converters[0]);
                                IConverter converter = converterClz.newInstance();
                                for (int i = 1; i < converters.length; i++) {
                                    converterClz = (Class<? extends IConverter>) Class.forName(converters[i]);
                                    converter = converter.andThen(converterClz.newInstance());
                                }
                                columnDesc.setConverter(converter);
                            }
                        }

                        // 获取field节点的子节点converter
                        List converters = fieldElement.elements(CONVERTER_ELEMENT);
                        if (converters != null && !converters.isEmpty()) {
                            Element converterElement = (Element) converters.get(0);

                            // 读取节点的文本值作为转换器类 如果没有值 则读取节点的class属性
                            String converterClzName = converterElement.getText();
                            if (converterClzName == null || converterClzName.trim().isEmpty()) {
                                converterClzName = converterElement.attributeValue(CONVERTER_CLASS_ATTRIBUTE);
                            }
                            if (converterClzName == null || converterClzName.trim().isEmpty()) {
                                throw new RuntimeException(String.format("【%s】的属性字段【%s】存在一个空的converter配置", clz.getName(), fieldName));
                            }

                            Class<? extends IConverter> converterClz = (Class<? extends IConverter>) Class.forName(converterClzName);
                            IConverter converter = converterClz.newInstance();
                            for (int i = 1; i < converters.size(); i++) {
                                converterElement = (Element) converters.get(i);

                                // 读取节点的文本值作为转换器类 如果没有值 则读取节点的class属性
                                converterClzName = converterElement.getText();
                                if (converterClzName == null || converterClzName.trim().isEmpty()) {
                                    converterClzName = converterElement.attributeValue(CONVERTER_CLASS_ATTRIBUTE);
                                }
                                if (converterClzName == null || converterClzName.trim().isEmpty()) {
                                    throw new RuntimeException(String.format("【%s】的属性字段【%s】存在一个空的converter配置", clz.getName(), fieldName));
                                }
                                converterClz = (Class<? extends IConverter>) Class.forName(converterClzName);
                                converter = converter.andThen(converterClz.newInstance());
                            }
                            columnDesc.setConverter(converter);
                        }

                        tableDesc.getColumns().put(columnDesc.getName(), columnDesc);
                    }

                    tableDescs.put(clz.getName(), tableDesc);
                }
            }
        }
        return tableDescs;
    }

    /**
     * 解析xml文件中cache配置
     *
     * @param xmlFilePath xml文件路径
     * @return cache信息
     * @throws Exception 解析错误
     */
    public static Map<String, Class> parseCaches(String xmlFilePath) throws Exception {
        Map<String, Class> caches = new HashMap<>();

        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlFilePath));
        Element root = document.getRootElement();
        // 获取configData节点
        Iterator rootIt = root.elementIterator(ROOT_ELEMENT);
        while (rootIt.hasNext()) {
            Element configDataElement = (Element) rootIt.next();
            // 获取configData的子节点caches
            Element cachesElement = configDataElement.element(CACHES_ELEMENT);
            if (cachesElement != null) {
                // 获取caches节点的子节点cache
                Iterator cacheElementIt = cachesElement.elementIterator(CACHE_ELEMENT);
                while (cacheElementIt.hasNext()) {
                    Element cacheElement = (Element) cacheElementIt.next();

                    // 读取节点的文本值作为转换器类 如果没有值 则读取节点的class属性
                    String className = cacheElement.getText();
                    if (className == null || className.trim().isEmpty()) {
                        className = cacheElement.attributeValue(CACHE_CLASS_ATTRIBUTE);
                    }
                    if (className == null || className.trim().isEmpty()) {
                        throw new RuntimeException("cache中未找到类配置");
                    }
                    // 生成对应的类并解析类的TableDesc信息
                    Class clz = Class.forName(className);
                    caches.put(clz.getName(), clz);
                }
            }
        }
        return caches;
    }
}

class XmlElement {
    // xml文件的所有节点
    static final String ROOT_ELEMENT = "configData";
    static final String CONFIGS_ELEMENT = "configs";
    static final String CACHES_ELEMENT = "caches";
    static final String CONFIG_ELEMENT = "config";
    static final String CACHE_ELEMENT = "cache";
    static final String FIELD_ELEMENT = "field";
    static final String CONVERTER_ELEMENT = "converter";

    // config节点属性
    static final String CONFIG_CLASS_ATTRIBUTE = "class";
    static final String CONFIG_NAME_ATTRIBUTE = "name";
    static final String CONFIG_PRIMARY_KEYS_ATTRIBUTE = "primaryKeys";
    static final String CONFIG_INDEX_ATTRIBUTE = "index";
    static final String CONFIG_HEADER_ATTRIBUTE = "header";
    static final String CONFIG_IGNORE_ROW_ATTRIBUTE = "ignoreRow";

    // field节点属性
    static final String FIELD_NAME_ATTRIBUTE = "name";
    static final String FIELD_COLUMN_NAME_ATTRIBUTE = "columnName";
    static final String FIELD_NOTNULL_ATTRIBUTE = "notNull";
    static final String FIELD_CONVERTERS_ATTRIBUTE = "converters";

    // converter节点属性
    static final String CONVERTER_CLASS_ATTRIBUTE = "class";

    // cache节点属性
    static final String CACHE_CLASS_ATTRIBUTE = "class";
}
