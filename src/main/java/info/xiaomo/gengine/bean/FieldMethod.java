package info.xiaomo.gengine.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.Data;

/**
 * 属性的set get方法对象封装
 *
 *
 * <p>
 * 2017年7月7日 上午11:42:55
 */
@Data
public class FieldMethod {

	private final Method setMethod;
	private final Method getMethod;
	private final Field field;

	public FieldMethod(Method getMethod, Method setMethod, Field field) {
		this.getMethod = getMethod;
		this.field = field;
		this.setMethod = setMethod;
	}

	public String getName() {
		return field == null ? "null" : field.getName();
	}


}
