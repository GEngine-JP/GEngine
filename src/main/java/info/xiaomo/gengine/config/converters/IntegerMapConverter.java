package info.xiaomo.gengine.config.converters;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.config.IConverter;

/**
 * @author YangQiang
 */
public class IntegerMapConverter implements IConverter<int[], Map<Integer, Integer>> {
	@Override
	public Map<Integer, Integer> convert(int[] ints) {
		Map<Integer, Integer> map = new HashMap<>(10);
		if (ints != null) {
			int step = 2;
			for (int i = 1; i < ints.length; i += step) {
				map.put(ints[i - 1], ints[i]);
			}
		}
		return map;
	}
}
