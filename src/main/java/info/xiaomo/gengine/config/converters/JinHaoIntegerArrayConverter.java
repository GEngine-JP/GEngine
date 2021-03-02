
package info.xiaomo.gengine.config.converters;

import info.xiaomo.gengine.config.IConverter;
import info.xiaomo.gengine.utils.SymbolUtil;

/**
 * @author YangQiang
 */
public class JinHaoIntegerArrayConverter implements IConverter<String, int[]> {
	@Override
	public int[] convert(String s) {
		String[] strs = s.split(SymbolUtil.JINHAO);
		int[] ret = new int[strs.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Integer.parseInt(strs[i]);
		}
		return ret;
	}
}
