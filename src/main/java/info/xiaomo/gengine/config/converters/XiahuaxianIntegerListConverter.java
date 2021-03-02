package info.xiaomo.gengine.config.converters;

import com.alibaba.druid.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import info.xiaomo.gengine.config.IConverter;
import info.xiaomo.gengine.utils.Cast;

public class XiahuaxianIntegerListConverter implements IConverter {

	@Override
	public List<Integer> convert(Object source) {
		String v = (String) source;
		if (v == null || StringUtils.isEmpty(v)) {
			return Collections.emptyList();
		}

		List<Integer> ret = new ArrayList<>();
		String[] strArray = v.split("_");
		for (String str : strArray) {
			ret.add(Cast.toInteger(str));
		}
		return ret;
	}
}
