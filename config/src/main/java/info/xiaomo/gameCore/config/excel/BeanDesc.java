/**
 * 工 程 名:  excelbean
 * 创建日期:  2017年02月13日 13:54
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel;

import org.apache.commons.beanutils.IntrospectionContext;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * java bean信息
 *
 * @author YangQiang
 */
public class BeanDesc implements IntrospectionContext {
    /** Constant for an empty array of property descriptors. */
    private static final PropertyDescriptor[] EMPTY_DESCRIPTORS = new PropertyDescriptor[0];

    /** The current class for bean. */
    private final Class<?> currentClass;

    /** A map for storing the already added property descriptors. */
    private final Map<String, PropertyDescriptor> descriptors;

    /**
     * Creates a new instance of <code>BeanContext</code> and sets the current class for bean.
     *
     * @param cls the current class
     */
    public BeanDesc(Class<?> cls) {
        currentClass = cls;
        descriptors = new HashMap<>();
    }

    public Class<?> getTargetClass() {
        return currentClass;
    }

    public void addPropertyDescriptor(PropertyDescriptor desc) {
        if (desc == null) {
            throw new IllegalArgumentException("Property descriptor must not be null!");
        }
        descriptors.put(desc.getName(), desc);
    }

    public void addPropertyDescriptors(PropertyDescriptor[] descs) {
        if (descs == null) {
            throw new IllegalArgumentException("Array with descriptors must not be null!");
        }

        for (int i = 0; i < descs.length; i++) {
            addPropertyDescriptor(descs[i]);
        }
    }

    public boolean hasProperty(String name) {
        return descriptors.containsKey(name);
    }

    public PropertyDescriptor getPropertyDescriptor(String name) {
        return descriptors.get(name);
    }

    public void removePropertyDescriptor(String name) {
        descriptors.remove(name);
    }

    public Set<String> propertyNames() {
        return descriptors.keySet();
    }

    /**
     * Returns an array with all descriptors added to this context. This method
     * is used to obtain the results of bean.
     *
     * @return an array with all known property descriptors
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return descriptors.values().toArray(EMPTY_DESCRIPTORS);
    }
}
