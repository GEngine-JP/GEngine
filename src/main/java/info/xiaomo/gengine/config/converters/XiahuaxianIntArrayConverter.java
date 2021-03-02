package info.xiaomo.gengine.config.converters;


import info.xiaomo.gengine.config.IConverter;

/**
 * 下划线int数组
 */
public class XiahuaxianIntArrayConverter implements IConverter {

	@Override
	public Object convert(Object source) {
		String str = (String) source;
		if (str == null || str.trim().isEmpty()) {
			return new int[0];
		}

		String[] strArray = str.split("_");
		int[] ret = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			ret[i] = Integer.parseInt(strArray[i]);
		}
		return ret;
	}

}
